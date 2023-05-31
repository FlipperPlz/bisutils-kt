package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.BisPboDataEntry
import com.flipperplz.bisutils.bank.BisPboFile
import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.utils.extensions.mutableParamFile
import com.flipperplz.bisutils.param.utils.extensions.mutableStringTable
import com.flipperplz.bisutils.param.utils.mutability.ParamMutableFile
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.ast.directive.BoostDefineDirective
import com.flipperplz.bisutils.stringtable.ast.StringTableFile
import com.flipperplz.bisutils.stringtable.ast.mutable.StringTableMutableFile

class BankProcessor(
    private val _banks: MutableList<BisPboFile>,
    boostDefines: MutableList<BoostDefineDirective> = mutableListOf(),
) {
    private val _boostPreprocessor: BoostPreprocessor = BoostPreprocessor(boostDefines, ::locateBoostFile)
    private var currentEntry: BisPboDataEntry? = null //current entry being preprocessed
    private val configFiles: MutableMap<BisPboDataEntry, ParamFile> = mutableMapOf()
    private val stringTables: MutableMap<BisPboDataEntry, StringTableFile> = mutableMapOf() //TODO: stringtables implementation

    private val globalConfig: ParamMutableFile = mutableParamFile("config")
    private val globalStringTable: StringTableMutableFile = mutableStringTable()

    val banks: List<BisPboFile> = _banks

    fun loadBank(bank: BisPboFile) {
        _banks.add(bank)
        indexBank(bank)
    }

    fun process(): List<BisPboFile> {
TODO()
    }

    private fun indexBank(bank: BisPboFile) {
        //TODO populate configs
    }

    private fun associateLocalPath(path: String): String {
        TODO("get full path of currentEntry relavise from there")
    }
    private fun locateBoostFile(path: String): String {
        return ""
    }
    private fun locateVFSEntry(path: String): BisPboDataEntry? {
        TODO("Locate entry in vfs")
    }
}