package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.ast.PboEntry
import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.bank.ast.entry.mutable.MutablePboDataEntry
import com.flipperplz.bisutils.bank.ast.entry.mutable.MutablePboVersionEntry
import com.flipperplz.bisutils.bank.ast.entry.PboDataEntry
import com.flipperplz.bisutils.bank.ast.entry.PboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.param.lexer.ParamLexer
import java.nio.charset.Charset

fun lexerOf(entry: PboDataEntry, encoding: Charset = Charsets.UTF_8): ParamLexer =
    ParamLexer(textOf(entry, encoding))

fun textOf(entry: PboDataEntry, encoding: Charset = Charsets.UTF_8): String =
    entry.entryData.array().toString(encoding)

fun paramLexerOf(entry: PboDataEntry, encoding: Charset = Charsets.UTF_8): ParamLexer =
    ParamLexer(textOf(entry, encoding))

fun List<PboEntry>.dataEntries(): List<PboDataEntry> =
    filterIsInstance<PboDataEntry>()

fun List<PboEntry>.mutableDataEntries(): List<MutablePboDataEntry> =
    filterIsInstance<MutablePboDataEntry>()

fun MutableList<PboEntry>.dataEntries(): List<MutablePboDataEntry> =
    filterIsInstance<MutablePboDataEntry>()

fun List<PboEntry>.immutableDataEntries(): List<PboDataEntry> =
    filterIsInstance<PboDataEntry>()

fun List<PboEntry>.versionEntry(): PboVersionEntry? =
    filterIsInstance<PboVersionEntry>().firstOrNull()

fun List<PboEntry>.mutableVersionEntry(): MutablePboVersionEntry? =
    filterIsInstance<MutablePboVersionEntry>().firstOrNull()

fun MutableList<PboEntry>.versionEntry(): MutablePboVersionEntry? =
    filterIsInstance<MutablePboVersionEntry>().firstOrNull()

fun MutableList<PboEntry>.immutableVersionEntry(): PboVersionEntry? =
    filterIsInstance<PboVersionEntry>().firstOrNull()

fun List<PboEntry>.getProperty(name: String): PboProperty? =
    versionEntry()?.properties?.propertyForName(name)

val PboFile.prefixProperty: PboProperty?
    get() = getProperty("prefix")

val PboFile.prefix: String
    get() = prefixProperty?.value ?: defaultPrefix