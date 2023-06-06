package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.entry.IPboDataEntry
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.options.PboEntryBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.binarization.interfaces.IStrictBinaryObject
import com.flipperplz.bisutils.param.lexer.ParamLexer
import java.nio.charset.Charset

typealias IPboBinaryObject = IStrictBinaryObject<PboEntryBinarizationOptions, PboEntryDebinarizationOptions>

fun lexerOf(entry: IPboDataEntry, encoding: Charset = Charsets.UTF_8): ParamLexer =
    ParamLexer(textOf(entry, encoding))

fun textOf(entry: IPboDataEntry, encoding: Charset = Charsets.UTF_8): String =
    entry.entryData.array().toString(encoding)

fun paramLexerOf(entry: IPboDataEntry, encoding: Charset = Charsets.UTF_8): ParamLexer =
    ParamLexer(textOf(entry, encoding))

fun List<IPboEntry>.dataEntries(): List<IPboDataEntry> =
    filterIsInstance<IPboDataEntry>()
//
//fun List<IPboEntry>.mutableDataEntries(): List<IMutablePboDataEntry> =
//    filterIsInstance<IMutablePboDataEntry>()

fun List<IPboEntry>.versionEntry(): IPboVersionEntry? =
    filterIsInstance<IPboVersionEntry>().firstOrNull()
//
//fun List<IPboEntry>.mutableVersionEntry(): IMutablePboVersionEntry? =
//    filterIsInstance<IMutablePboVersionEntry>().firstOrNull()
//
//fun List<IPboEntry>.getProperty(name: String): IPboProperty? =
//    versionEntry()?.properties?.firstOrNull {it.name == name}

