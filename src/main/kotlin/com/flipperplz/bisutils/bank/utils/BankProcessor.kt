package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.PboDataEntry
import com.flipperplz.bisutils.bank.PboFile
import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.utils.extensions.mutableParamFile
import com.flipperplz.bisutils.param.utils.extensions.mutableStringTable
import com.flipperplz.bisutils.param.utils.extensions.openParamFile
import com.flipperplz.bisutils.param.utils.mutability.ParamMutableFile
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.ast.directive.BoostDefineDirective
import com.flipperplz.bisutils.preprocesser.boost.ast.directive.BoostIncludeDirective
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostIncludeNotFoundException
import com.flipperplz.bisutils.preprocesser.enforce.EnforcePreprocessor
import com.flipperplz.bisutils.preprocesser.enforce.ast.directive.EnforceDefineDirective
import com.flipperplz.bisutils.preprocesser.enforce.ast.directive.EnforceIncludeDirective
import com.flipperplz.bisutils.preprocesser.enforce.utils.EnforceIncludeNotFoundException
import com.flipperplz.bisutils.stringtable.ast.StringTableFile
import com.flipperplz.bisutils.stringtable.ast.mutable.StringTableMutableFile
import com.flipperplz.bisutils.stringtable.util.StringTableFormat
import com.flipperplz.bisutils.stringtable.util.openStringtable
import java.nio.charset.Charset

class BankProcessor(
    private val _banks: MutableList<PboFile>,
    private val startingBoostDefines: MutableList<BoostDefineDirective> = mutableListOf(),
    private val startingEnforceDefines: MutableList<EnforceDefineDirective> = mutableListOf()
) {
    private val _enforcePreprocessor: EnforcePreprocessor = EnforcePreprocessor(startingEnforceDefines, ::locateEnforceFile)
    private val _boostPreprocessor: BoostPreprocessor = BoostPreprocessor(startingBoostDefines, ::locateBoostFile)
    private var currentEntry: PboDataEntry? = null //current entry being preprocessed
    private val configFiles: MutableMap<PboDataEntry, ParamFile> = mutableMapOf()
    private val stringTables: MutableMap<PboDataEntry, StringTableFile> = mutableMapOf() //TODO: stringtables implementation
    private val globalConfig: ParamMutableFile = mutableParamFile("config")
    private val globalStringTable: StringTableMutableFile = mutableStringTable()

    val banks: List<PboFile> = _banks
    fun loadBank(bank: PboFile) {
        _banks.add(bank)
        indexBank(bank)
    }
    fun process(flush: Boolean = true): List<PboFile> {
        val banks = mutableListOf<PboFile>()
        //TODO: Process Banks
        if(flush) flush()
        return banks
    }

    private fun indexBank(bank: PboFile) {
        for (dataEntry in bank.pboEntries.filterIsInstance<PboDataEntry>()) {
            currentEntry = dataEntry
            var fileName = PboFile.normalizePath(dataEntry.fileName)?.split('\\')?.last() ?: continue
            val extension = fileName.split('.').last()
            fileName = fileName.removeRange(fileName.length-extension.length-1.. fileName.length)

            fun processConfig(file: ParamFile) = configFiles.putIfAbsent(dataEntry, file)

            fun processStringtable(table: StringTableFile) {
                stringTables.putIfAbsent(dataEntry, table)
                globalStringTable.append(table)
            }

            when(fileName) {
                "config" -> {
                    when(extension) {
                        "bin" -> { TODO("try to locate a cpp file in same directory, skip if found") }
                        "cpp" -> {}
                        else -> continue
                    }
                    val param = openParamFile(dataEntry.entryData, dataEntry.fileName, _boostPreprocessor)
                    processConfig(param)
                }
                "stringtable" -> {
                    val format = StringTableFormat.forExtension(extension) ?: continue
                    val stringtable = openStringtable(dataEntry.entryData, format, _boostPreprocessor)
                    processStringtable(stringtable)
                }
            }
        }
        currentEntry = null
    }

    private fun associateLocalPath(path: String): String = currentEntry?.let {
        return it.initialOwner?.getAbsoluteEntryPath(it)?.split('\\')?.toMutableList()?.let { currentDirPath ->
            currentDirPath.removeLast()
            PboFile.normalizePath(path)?.split('\\')?.forEach {segment ->
                when(segment) {
                    ".." -> currentDirPath.removeLast()
                    else -> currentDirPath.add(segment)
                }
            }
            currentDirPath.joinToString(separator = "\\")
        } ?: throw Exception("Failed to conjoin paths")
    }  ?: throw Exception("Cannot associate with current path as no current path is set")

    private fun locateBoostFile(include: BoostIncludeDirective, encoding: Charset = Charsets.UTF_8): String =
        locateVFSEntry(associateLocalPath(include.path))?.entryData?.array()?.toString(encoding) ?: throw BoostIncludeNotFoundException(include)

    private fun locateEnforceFile(include: EnforceIncludeDirective, encoding: Charset = Charsets.UTF_8): String =
        locateVFSEntry(PboFile.normalizePath(include.path) ?: throw Exception("Error normalizing PBO path"))?.entryData?.array()?.toString(encoding) ?: throw EnforceIncludeNotFoundException(include)

    private fun locateVFSEntry(path: String): PboDataEntry?{
        return with(pboForPath(path) ?: return null) {
            entries.filterIsInstance<PboDataEntry>().firstOrNull {
                getAbsoluteEntryPath(it).equals(path, ignoreCase = true)
            }
        }
    }

    private fun pboForPath(absolutePath: String): PboFile? {
        var target: PboFile? = null
        var longestMatch = 0
        val path = absolutePath.trimStart('\\')
        banks.forEach {
            val prefixLn = it.pboPrefix.length
            if(path.startsWith(it.pboPrefix) && prefixLn >= longestMatch) {
               target = it
               longestMatch = prefixLn
            }
        }
        return target
    }

    private fun flush() {
        _boostPreprocessor.flush(); _banks.clear()
        stringTables.clear(); globalStringTable.flush()
        configFiles.clear(); globalConfig.flush()
    }
}