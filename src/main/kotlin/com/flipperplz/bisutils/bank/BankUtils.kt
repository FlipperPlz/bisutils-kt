package com.flipperplz.bisutils.bank

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.pbo.BisPboDataEntry
import com.flipperplz.bisutils.pbo.BisPboFile
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.ast.directive.BoostDefineDirective

class BankProcessor(
    private val _banks: MutableList<BisPboFile>,
    boostDefines: MutableList<BoostDefineDirective> = mutableListOf(),
) {
    private val _boostPreprocessor: BoostPreprocessor = BoostPreprocessor(boostDefines, ::locateBoostFile)
    private var currentEntry: BisPboDataEntry? = null //current entry being preprocessed
    private val configFiles: MutableMap<BisPboDataEntry, ParamFile> = mutableMapOf()
    private val stringTables: MutableMap<BisPboDataEntry, String> = mutableMapOf() //TODO: stringtables implementation

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