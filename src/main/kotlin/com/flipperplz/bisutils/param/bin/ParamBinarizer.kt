package com.flipperplz.bisutils.param.bin

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.literal.ParamArray
import com.flipperplz.bisutils.param.ast.literal.ParamFloat
import com.flipperplz.bisutils.param.ast.literal.ParamInt
import com.flipperplz.bisutils.param.ast.literal.ParamString
import com.flipperplz.bisutils.param.ast.node.ParamLiteralBase
import com.flipperplz.bisutils.param.ast.node.ParamStatement
import com.flipperplz.bisutils.param.ast.statement.ParamClass
import com.flipperplz.bisutils.param.ast.statement.ParamDeleteStatement
import com.flipperplz.bisutils.param.ast.statement.ParamExternalClass
import com.flipperplz.bisutils.param.ast.statement.ParamVariableStatement
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.extensions.childClasses
import com.flipperplz.bisutils.io.putAsciiZ
import com.flipperplz.bisutils.io.putCompactInt
import java.nio.ByteBuffer

//object ParamBinarizer {


//}
fun ParamString.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putAsciiZ(slimValue!!); true
}

fun ParamFloat.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putFloat(slimValue!!); true
}

fun ParamInt.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putInt(slimValue!!); true
}

fun ParamExternalClass.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putAsciiZ(slimName!!); true
}

fun ParamDeleteStatement.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putAsciiZ(slimName!!); true
}

fun ParamArray.write(buffer: ByteBuffer): Boolean { return if(!isCurrentlyValid()) false else {
    buffer.putCompactInt(slimValue?.count() ?: return false); true
}}


fun ParamFile.write(buffer: ByteBuffer): Boolean { return if(!isCurrentlyValid()) false else with(buffer) {
        put(0); put(114); put(97); put(80); putInt(0); putInt(8)
        val offsets = mutableListOf(ParamOffset(position(), 0xDADA))
        var i = 0
    putInt(0xDADA); put(0); putCompactInt(slimCommands.count())


        slimCommands.forEach { it.write(buffer, offsets) }
        childClasses.forEach { i = it.save(buffer, offsets, i++) }

        offsets.forEach { buffer.position(it.location); buffer.putInt(it.data) }

        true
    }
}

data class ParamOffset(var location: Int, var data: Int)

fun ParamClass.save(buffer: ByteBuffer, offsets: MutableList<ParamOffset>? = null, i: Int = 0): Int {
    offsets?.get(i)?.data = buffer.position()
    buffer.putAsciiZ(slimSuperClass ?: "")
    var ii = i
    slimCommands.forEach { it.write(buffer, offsets) }
    childClasses.forEach { ii = it.save(buffer, offsets, ii++) }
    return ii++
}

fun ParamClass.write(buffer: ByteBuffer, offsets: MutableList<ParamOffset>? = null): Boolean {
    buffer.putAsciiZ(slimName ?: return false)
    offsets?.add(ParamOffset(buffer.position(),0xDADA)); buffer.putInt(0xDADA)
    return true
}

fun ParamVariableStatement.write(buffer: ByteBuffer): Boolean {
    if(getID() == 5.toByte()) buffer.put(slimOperator?.flag ?: return false)
    buffer.putAsciiZ(slimName ?: return false)
    if(slimValue is ParamArray) (slimValue as? ParamArray)?.let {
        return it.write(buffer)
    }
    return slimValue?.write(buffer) ?: return false
}

fun ParamLiteralBase.write(buffer: ByteBuffer) = when (this) {
    is ParamString -> { buffer.put(getID()); write(buffer) }
    is ParamFloat -> { buffer.put(getID()); write(buffer) }
    is ParamInt -> { buffer.put(getID()); write(buffer) }
    is ParamArray -> { buffer.put(getID()); write(buffer) }
    else -> throw Exception("Unknown literal in context of ParamFile Binarization")
}

fun ParamStatement.write(buffer: ByteBuffer, offsets: MutableList<ParamOffset>? = null): Boolean = when(this) {
    is ParamDeleteStatement -> { buffer.put(getID()); write(buffer) }
    is ParamClass -> { buffer.put(getID()); write(buffer, offsets) }
    is ParamExternalClass -> { buffer.put(getID()); write(buffer) }
    is ParamVariableStatement -> { buffer.put(getID()); write(buffer) }
    else -> throw Exception("Unknown statement in context of ParamFile Binarization")
}


fun ParamLiteralBase.getID(): Byte = when (this) {
    is ParamString -> 0
    is ParamFloat -> 1
    is ParamInt -> 2
    is ParamArray -> 3
    else -> throw Exception("Unknown literal in context of ParamFile Binarization")
}

fun ParamStatement.getID(): Byte = when(this) {
    is ParamDeleteStatement -> 4
    is ParamClass -> 0
    is ParamExternalClass -> 3
    is ParamVariableStatement -> if(slimValue !is ParamArray) 1 else if(slimOperator != ParamOperatorTypes.ASSIGN) 5 else 2
    else -> throw Exception("Unknown statement in context of ParamFile Binarization")
}
