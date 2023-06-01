package com.flipperplz.bisutils.bank.astImpl.misc

import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.bank.ast.misc.PboElement
import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.utils.BisFamily
import com.flipperplz.bisutils.utils.BisStrictBinarizable
import com.flipperplz.bisutils.utils.putAsciiZ
import java.nio.ByteBuffer
import java.nio.charset.Charset

open class PboPropertyMap(
    override val parent: BisFamily?,
    override val lowestBranch: PboFile?,
    open val properties: List<PboProperty>
) : Map<String, String>, BisStrictBinarizable, PboElement {
    override val children: List<PboProperty>
        get() = properties
    override val entries: Set<Map.Entry<String, String>>
        get() = properties.map { entry ->
            object : Map.Entry<String, String> {
                override val key: String = entry.name
                override val value: String = entry.value
            }
        }.toSet()

    override val keys: Set<String>
        get() = properties.map { it.name }.toSet()

    override val size: Int
        get() = properties.size

    override val values: Collection<String>
        get() = properties.map { it.value }

    override fun containsKey(key: String): Boolean = properties.firstOrNull { it.name == key } != null

    override fun containsValue(value: String): Boolean = properties.firstOrNull { it.value == value } != null

    override fun get(key: String): String? = properties.lastOrNull { it.name == key }?.value //TODO: Maybe first is taken instead

    override fun isEmpty(): Boolean = properties.isEmpty()

    open fun propertyForName(name: String) : PboProperty? =
        properties.firstOrNull {it.name.equals(name, true)}

    override fun writeValidated(buffer: ByteBuffer, charset: Charset): Boolean {
        properties.forEach {
            buffer.putAsciiZ(it.name, charset)
            buffer.putAsciiZ(it.value, charset)
        }
        buffer.put(0)
        return true
    }

    override val binaryLength: Long
        get() = properties.sumOf { it.binaryLength } + 1L

    override fun isValid(): Boolean = properties.all { it.isValid() }
}