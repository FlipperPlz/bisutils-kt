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
import com.flipperplz.bisutils.stringtable.ast.StringTableFile
import com.flipperplz.bisutils.stringtable.ast.mutable.StringTableMutableFile
import com.flipperplz.bisutils.stringtable.util.StringTableFormat
import com.flipperplz.bisutils.stringtable.util.openStringtable

class BankProcessor(
    private val _banks: MutableList<PboFile>,
    boostDefines: MutableList<BoostDefineDirective> = mutableListOf(),
) {
    private val _boostPreprocessor: BoostPreprocessor = BoostPreprocessor(boostDefines, ::locateBoostFile)
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

    fun process(): List<PboFile> {
TODO()
    }



    private fun indexBank(bank: PboFile) {

        for (dataEntry in bank.pboEntries.filterIsInstance<PboDataEntry>()) {
            var fileName = dataEntry.segmentedPath.last()
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
    }

    private fun associateLocalPath(path: String): String {
        TODO("get full path of currentEntry relavise from there")
    }
    private fun locateBoostFile(path: String): String {
        return ""
    }
    private fun locateVFSEntry(path: String): PboDataEntry? {
        TODO("Locate entry in vfs")
    }
}