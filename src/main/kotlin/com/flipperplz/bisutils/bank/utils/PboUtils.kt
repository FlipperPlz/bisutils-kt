package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.PboDataEntry
import com.flipperplz.bisutils.param.lexer.ParamLexer
import java.nio.charset.Charset

fun lexerOf(entry: PboDataEntry, encoding: Charset = Charsets.UTF_8): ParamLexer =
    ParamLexer(textOf(entry, encoding))

fun textOf(entry: PboDataEntry, encoding: Charset = Charsets.UTF_8): String =
    entry.entryData.array().toString(encoding)

fun paramLexerOf(entry: PboDataEntry, encoding: Charset = Charsets.UTF_8): ParamLexer =
    ParamLexer(textOf(entry, encoding))