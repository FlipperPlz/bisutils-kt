package com.flipperplz.bisutils.param.ast

import childClasses
import com.flipperplz.bisutils.binarization.utils.BisBinaryOffset
import com.flipperplz.bisutils.binarization.utils.writeOffsets
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.io.putAsciiZ
import com.flipperplz.bisutils.io.putCompactInt
import com.flipperplz.bisutils.io.putInt
import com.flipperplz.bisutils.param.ast.node.IParamStatement
import com.flipperplz.bisutils.param.ast.node.IParamStatementHolder
import com.flipperplz.bisutils.param.ast.statement.IParamClass
import com.flipperplz.bisutils.param.options.DEFAULT_PARAM_SIGNATURE
import com.flipperplz.bisutils.param.options.ParamOptions
import org.jetbrains.annotations.NotNull
import java.nio.ByteBuffer
import java.nio.ByteOrder

interface IParamFile : IFamilyNode, IParamStatementHolder {
    companion object;
    val paramName: String?
    override val familyParent: IFamilyParent? get() = null
    override val familyNode: IParamFile? get() = this

    override fun toParam(): String = super.toParam()

    override fun writeValidated(buffer: ByteBuffer, @NotNull options: ParamOptions?): Boolean {
        buffer.putInt(options?.fileSignature ?: DEFAULT_PARAM_SIGNATURE, options?.endianness ?: ByteOrder.LITTLE_ENDIAN )
        buffer.putLong(0); buffer.putLong(8)

        buffer.putAsciiZ("", options?.charset ?: Charsets.UTF_8)
        buffer.putCompactInt(familyChildren.count())
        val offsetMap: MutableMap<IParamClass, BisBinaryOffset> = mutableMapOf()

        var save: (IParamClass) -> Boolean = { true }
        fun writeChildren(holder: IParamStatementHolder): Boolean {
            holder.familyChildren.forEach {
                if(!it.writeValidated(buffer, options)) return false
                if(it is IParamClass) {
                    if(options?.pClass == null) throw Exception("Options needed to store offsets.")
                    offsetMap[it] = BisBinaryOffset(options.pClass!!.toLong(), 0xCAFEL)
                }
            }
            holder.childClasses.forEach { if(!save(it)) return false }
            return true
        }
        save = { clazz ->
            val currentOffset = offsetMap[clazz] ?: throw Exception("Offset not found in list, was this class not written yet?")
            currentOffset.value = buffer.position().toLong()
            buffer.putAsciiZ("", options?.charset ?: Charsets.UTF_8)
            writeChildren(clazz)
        }

        writeChildren(this)
        if(!offsetMap.values.writeOffsets(buffer, options!!)) return false
        TODO()




        return true
    }
}