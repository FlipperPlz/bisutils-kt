package com.flipperplz.bisutils.stringtable.parser

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.utils.extensions.mutableParamFile
import com.flipperplz.bisutils.param.utils.extensions.mutableStringTable
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.BisPreprocessor
import com.flipperplz.bisutils.stringtable.ast.StringTableFile

object StringTableCSVParser {
    fun parse(lexer: BisLexer, name: String, preProcessor: BisPreprocessor? = null): StringTableFile =
        mutableStringTable().apply {

        }
}