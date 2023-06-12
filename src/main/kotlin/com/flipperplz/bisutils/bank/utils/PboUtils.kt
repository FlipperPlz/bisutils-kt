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
import com.flipperplz.bisutils.bank.astImpl.entry.PboDataEntry
import com.flipperplz.bisutils.bank.astImpl.entry.PboVersionEntry
import com.flipperplz.bisutils.bank.astImpl.entry.mutable.MutablePboDataEntry
import com.flipperplz.bisutils.bank.astImpl.entry.mutable.MutablePboVersionEntry
import com.flipperplz.bisutils.bank.astImpl.misc.PboProperty
import com.flipperplz.bisutils.bank.astImpl.misc.mutable.MutablePboProperty
import com.flipperplz.bisutils.bank.astImpl.mutable.MutablePboFile
import com.flipperplz.bisutils.bank.options.PboOptions
//import com.flipperplz.bisutils.param.lexer.ParamLexer
import java.nio.ByteBuffer
import java.nio.charset.Charset


//fun lexerOf(entry: IPboDataEntry, encoding: Charset = Charsets.UTF_8): ParamLexer =
//    ParamLexer(textOf(entry, encoding))

fun textOf(entry: IPboDataEntry, encoding: Charset = Charsets.UTF_8): String =
    entry.entryData.array().toString(encoding)
//
//fun paramLexerOf(entry: IPboDataEntry, encoding: Charset = Charsets.UTF_8): ParamLexer =
//    ParamLexer(textOf(entry, encoding))

val IPboDirectory.dataEntries: List<IPboDataEntry>
    get() = familyChildren.filterIsInstance<IPboDataEntry>()

val IPboDirectory.versionEntries: List<IPboVersionEntry>
    get() = familyChildren.filterIsInstance<IPboVersionEntry>()

val IMutablePboDirectory.mutableVersionEntries: List<IMutablePboVersionEntry>
    get() = familyChildren.filterIsInstance<IMutablePboVersionEntry>()

val IMutablePboDirectory.mutableDataEntries: List<IMutablePboDataEntry>
    get() = familyChildren.filterIsInstance<IMutablePboDataEntry>()

val IPboDirectory.mainVersionEntry: IPboVersionEntry?
    get() = versionEntries.firstOrNull()

val IMutablePboDirectory.mainMutableVersionEntry: IMutablePboVersionEntry?
    get() = mutableVersionEntries.firstOrNull()

val IPboFile.properties: List<IPboProperty>
    get() = mainVersionEntry?.familyChildren ?: emptyList()

val IMutablePboFile.mutableProperties: List<IMutablePboProperty>
    get() = mainMutableVersionEntry?.familyChildren ?: emptyList()

fun IPboVersionEntry.getProperty(name: String): IPboProperty? =
    familyChildren.firstOrNull { it.name == name }

fun IMutablePboVersionEntry.getMutableProperty(name: String): IMutablePboProperty? =
    familyChildren.firstOrNull { it.name == name }

fun IPboFile.getProperty(name: String): IPboProperty? =
    properties.firstOrNull {it.name == name}

fun IMutablePboFile.getMutableProperty(name: String): IPboProperty? =
    mutableProperties.firstOrNull {it.name == name}
//----------------------------------------------------------------------------------------------------------------------
operator fun IPboFile.invoke(name: String, buffer: ByteBuffer, options: PboOptions): IPboFile =
    MutablePboFile(name).apply { read(buffer, options) }

operator fun PboFile.invoke(name: String, buffer: ByteBuffer, options: PboOptions): PboFile =
    MutablePboFile(name).apply { read(buffer, options) }

operator fun MutablePboFile.invoke(name: String, buffer: ByteBuffer, options: PboOptions): MutablePboFile =
    MutablePboFile(name).apply { read(buffer, options) }
//----------------------------------------------------------------------------------------------------------------------
operator fun IPboVersionEntry.invoke(buffer: ByteBuffer, options: PboOptions, parent: IPboDirectory?, node: IPboFile? = parent?.familyNode): IPboVersionEntry =
    MutablePboVersionEntry(parent, node).apply { read(buffer, options) }

operator fun PboVersionEntry.invoke(buffer: ByteBuffer, options: PboOptions, parent: IPboDirectory?, node: IPboFile? = parent?.familyNode): PboVersionEntry =
    MutablePboVersionEntry(parent, node).apply { read(buffer, options) }

operator fun MutablePboVersionEntry.invoke(buffer: ByteBuffer, options: PboOptions, parent: IPboDirectory?, node: IPboFile? = parent?.familyNode): PboVersionEntry =
    MutablePboVersionEntry(parent, node).apply { read(buffer, options) }
//----------------------------------------------------------------------------------------------------------------------
operator fun IPboDataEntry.invoke(buffer: ByteBuffer, options: PboOptions, parent: IPboDirectory?, node: IPboFile? = parent?.familyNode): IPboDataEntry =
    MutablePboDataEntry(parent, node).apply { read(buffer, options) }

operator fun PboDataEntry.invoke(buffer: ByteBuffer, options: PboOptions, parent: IPboDirectory?, node: IPboFile? = parent?.familyNode): IPboDataEntry =
    MutablePboDataEntry(parent, node).apply { read(buffer, options) }

operator fun MutablePboDataEntry.invoke(buffer: ByteBuffer, options: PboOptions, parent: IPboDirectory?, node: IPboFile? = parent?.familyNode): IPboDataEntry =
    MutablePboDataEntry(parent, node).apply { read(buffer, options) }
//----------------------------------------------------------------------------------------------------------------------
operator fun IPboProperty.invoke(buffer: ByteBuffer, options: PboOptions, parent: IPboVersionEntry?, node: IPboFile? = parent?.familyNode): IPboProperty =
    MutablePboProperty(parent, node).apply { read(buffer, options) }

operator fun PboProperty.invoke(buffer: ByteBuffer, options: PboOptions, parent: IPboVersionEntry?, node: IPboFile? = parent?.familyNode): PboProperty =
    MutablePboProperty(parent, node).apply { read(buffer, options) }

operator fun MutablePboProperty.invoke(buffer: ByteBuffer, options: PboOptions, parent: IPboVersionEntry?, node: IPboFile? = parent?.familyNode): MutablePboProperty =
    MutablePboProperty(parent, node).apply { read(buffer, options) }