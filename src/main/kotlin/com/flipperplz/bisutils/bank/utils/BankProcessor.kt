package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboDataEntry
import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.literal.ParamArray
import com.flipperplz.bisutils.param.ast.statement.ParamVariableStatement
import com.flipperplz.bisutils.param.utils.extensions.*
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
import com.flipperplz.bisutils.dayz.DayZScriptContext
import com.flipperplz.bisutils.dayz.DayZScriptModule
import java.nio.charset.Charset

class BankProcessor(
    private val _banks: MutableList<IPboFile>,
    private val startingBoostDefines: MutableList<BoostDefineDirective> = mutableListOf(),
    private val startingEnforceDefines: MutableList<EnforceDefineDirective> = mutableListOf()
) {
    private val _enforcePreprocessor: EnforcePreprocessor = EnforcePreprocessor(startingEnforceDefines, ::locateEnforceFile)
    private val _boostPreprocessor: BoostPreprocessor = BoostPreprocessor(startingBoostDefines, ::locateBoostFile)
    private val configFiles: MutableMap<IPboDataEntry, ParamFile> = mutableMapOf()
    private val stringTables: MutableMap<IPboDataEntry, StringTableFile> = mutableMapOf() //TODO: stringtables implementation
    private val globalConfig: ParamMutableFile = mutableParamFile("config")
    private val globalStringTable: StringTableMutableFile = mutableStringTable()

    val banks: List<IPboFile> = _banks
    fun loadBank(bank: IPboFile) {
        _banks.add(bank)
        indexBank(bank)
    }

    fun process(flush: Boolean = true): List<IPboFile> {
        val banks = mutableListOf<IPboFile>()
        val context = DayZScriptContext<IPboDataEntry>()
        for ((pboFile, paramFile) in configFiles) {
            (paramFile % "CfgMods")?.childClasses?.forEach { modClass ->
                (modClass % "defs")?.childClasses?.forEach { defsClass ->
                    for(module in DayZScriptModule.values()) {
                        val scriptDefinitionClass = (defsClass % module.defsTitle) ?: continue
                        (scriptDefinitionClass.childrenOfType<ParamVariableStatement>().firstOrNull {
                            it.slimName == "files" &&
                            it.slimValue is ParamArray
                        }?.slimValue as? ParamArray)?.slimValue?.forEach {
                          //todo: parse each directory or filename and add to context
                        }

                    }
                }
            }
        }

        if(flush) flush()
        return banks
    }

    private var currentEntry: IPboDataEntry? = null //current entry being preprocessed

    private fun indexBank(bank: IPboFile) {
        for (dataEntry in bank.entries.filterIsInstance<IPboDataEntry>()) {
            currentEntry = dataEntry
            //TODO: normalization of path
            var fileName = /*PboFile.normalizePath(dataEntry.fileName)*/dataEntry.entryName.split('\\').last()
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
                    val param = openParamFile(dataEntry.entryData, dataEntry.entryName, _boostPreprocessor)
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
        //TODO: Create absolute path
        return path/* it.lowestBranch?.getAbsoluteEntryPath(it)?*/.split('\\')?.toMutableList()?.let { currentDirPath ->
            currentDirPath.removeLast()
            //TODO: Normalize Path
            /*PboFile.normalizePath(path)?*/path.split('\\')?.forEach {segment ->
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
        //TODO: Path normalization
        locateVFSEntry(/*PboFile.normalizePath*/(include.path) ?: throw Exception("Error normalizing PBO path"))?.entryData?.array()?.toString(encoding) ?: throw EnforceIncludeNotFoundException(include)

    private fun locateVFSEntry(path: String): IPboDataEntry?{
        return with(pboForPath(path) ?: return null) {
            entries.filterIsInstance<IPboDataEntry>().firstOrNull {
                //TODO:  Create absolute path
                /*getAbsoluteEntryPath*/(it.entryName).equals(path, ignoreCase = true)
            }
        }
    }

    private fun pboForPath(absolutePath: String): IPboFile? {
        var target: IPboFile? = null
        var longestMatch = 0
        val path = absolutePath.trimStart('\\')
        banks.forEach {
            val prefixLn = it.prefix.length
            if(path.startsWith(it.prefix) && prefixLn >= longestMatch) {
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