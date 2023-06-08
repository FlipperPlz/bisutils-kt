package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboDataEntry
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboDataEntry
import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.astImpl.PboFile
import com.flipperplz.bisutils.bank.astImpl.mutable.MutablePboFile
import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.param.lexer.ParamLexer
import java.nio.ByteBuffer
import java.nio.charset.Charset


fun lexerOf(entry: IPboDataEntry, encoding: Charset = Charsets.UTF_8): ParamLexer =
    ParamLexer(textOf(entry, encoding))

fun textOf(entry: IPboDataEntry, encoding: Charset = Charsets.UTF_8): String =
    entry.entryData.array().toString(encoding)

fun paramLexerOf(entry: IPboDataEntry, encoding: Charset = Charsets.UTF_8): ParamLexer =
    ParamLexer(textOf(entry, encoding))

val IPboDirectory.dataEntries: List<IPboDataEntry>
    get() = children.filterIsInstance<IPboDataEntry>()

val IPboDirectory.versionEntries: List<IPboVersionEntry>
    get() = children.filterIsInstance<IPboVersionEntry>()

val IMutablePboDirectory.mutableVersionEntries: List<IMutablePboVersionEntry>
    get() = children.filterIsInstance<IMutablePboVersionEntry>()

val IMutablePboDirectory.mutableDataEntries: List<IMutablePboDataEntry>
    get() = children.filterIsInstance<IMutablePboDataEntry>()

val IPboDirectory.mainVersionEntry: IPboVersionEntry?
    get() = versionEntries.firstOrNull()

val IMutablePboDirectory.mainMutableVersionEntry: IMutablePboVersionEntry?
    get() = mutableVersionEntries.firstOrNull()

val IPboFile.properties: List<IPboProperty>
    get() = mainVersionEntry?.children ?: emptyList()

val IMutablePboFile.mutableProperties: List<IMutablePboProperty>
    get() = mainMutableVersionEntry?.children ?: emptyList()

fun IPboVersionEntry.getProperty(name: String): IPboProperty? =
    children.firstOrNull { it.name == name }

fun IMutablePboVersionEntry.getMutableProperty(name: String): IMutablePboProperty? =
    children.firstOrNull { it.name == name }

fun IPboFile.getProperty(name: String): IPboProperty? =
    properties.firstOrNull {it.name == name}

fun IMutablePboFile.getMutableProperty(name: String): IPboProperty? =
    mutableProperties.firstOrNull {it.name == name}

operator fun IPboFile.invoke(name: String, buffer: ByteBuffer, options: PboOptions): IPboFile =
    MutablePboFile(name).apply { read(buffer, options) }

operator fun PboFile.invoke(name: String, buffer: ByteBuffer, options: PboOptions): PboFile =
    MutablePboFile(name).apply { read(buffer, options) }

operator fun MutablePboFile.invoke(name: String, buffer: ByteBuffer, options: PboOptions): MutablePboFile =
    MutablePboFile(name).apply { read(buffer, options) }

//
//fun List<IPboEntry>.mutableDataEntries(): List<IMutablePboDataEntry> =
//    filterIsInstance<IMutablePboDataEntry>()


//
//fun List<IPboEntry>.mutableVersionEntry(): IMutablePboVersionEntry? =
//    filterIsInstance<IMutablePboVersionEntry>().firstOrNull()
//
//fun List<IPboEntry>.getProperty(name: String): IPboProperty? =
//    versionEntry()?.properties?.firstOrNull {it.name == name}

