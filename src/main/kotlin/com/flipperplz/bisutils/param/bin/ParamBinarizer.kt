package com.flipperplz.bisutils.param.bin

import com.flipperplz.bisutils.param.IParamFile
import com.flipperplz.bisutils.param.ast.literal.IParamArray
import com.flipperplz.bisutils.param.ast.literal.IParamFloat
import com.flipperplz.bisutils.param.ast.literal.IParamInt
import com.flipperplz.bisutils.param.ast.literal.IParamString
import com.flipperplz.bisutils.param.ast.node.IParamLiteralBase
import com.flipperplz.bisutils.param.ast.node.IParamStatement
import com.flipperplz.bisutils.param.ast.statement.IParamClass
import com.flipperplz.bisutils.param.ast.statement.IParamDeleteStatement
import com.flipperplz.bisutils.param.ast.statement.IParamExternalClass
import com.flipperplz.bisutils.param.ast.statement.IParamVariableStatement
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.extensions.childClasses
import com.flipperplz.bisutils.io.putAsciiZ
import com.flipperplz.bisutils.io.putCompactInt
import java.nio.ByteBuffer

//object ParamBinarizer {


//}
fun IParamString.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putAsciiZ(slimValue!!); true
}

fun IParamFloat.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putFloat(slimValue!!); true
}

fun IParamInt.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putInt(slimValue!!); true
}

fun IParamExternalClass.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putAsciiZ(slimName!!); true
}

fun IParamDeleteStatement.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putAsciiZ(slimName!!); true
}

fun IParamArray.write(buffer: ByteBuffer): Boolean { return if(!isCurrentlyValid()) false else {
    buffer.putCompactInt(slimValue?.count() ?: return false); true
}}


fun IParamFile.write(buffer: ByteBuffer): Boolean { return if(!isCurrentlyValid()) false else with(buffer) {
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

fun IParamClass.save(buffer: ByteBuffer, offsets: MutableList<ParamOffset>? = null, i: Int = 0): Int {
    offsets?.get(i)?.data = buffer.position()
    buffer.putAsciiZ(slimSuperClass ?: "")
    var ii = i
    slimCommands.forEach { it.write(buffer, offsets) }
    childClasses.forEach { ii = it.save(buffer, offsets, ii++) }
    return ii++
}

fun IParamClass.write(buffer: ByteBuffer, offsets: MutableList<ParamOffset>? = null): Boolean {
    buffer.putAsciiZ(slimName ?: return false)
    offsets?.add(ParamOffset(buffer.position(),0xDADA)); buffer.putInt(0xDADA)
    return true
}

fun IParamVariableStatement.write(buffer: ByteBuffer): Boolean {
    if(getID() == 5.toByte()) buffer.put(slimOperator?.flag ?: return false)
    buffer.putAsciiZ(slimName ?: return false)
    if(slimValue is IParamArray) (slimValue as? IParamArray)?.let {
        return it.write(buffer)
    }
    return slimValue?.write(buffer) ?: return false
}

fun IParamLiteralBase.write(buffer: ByteBuffer) = when (this) {
    is IParamString -> { buffer.put(getID()); write(buffer) }
    is IParamFloat -> { buffer.put(getID()); write(buffer) }
    is IParamInt -> { buffer.put(getID()); write(buffer) }
    is IParamArray -> { buffer.put(getID()); write(buffer) }
    else -> throw Exception("Unknown literal in context of ParamFile Binarization")
}

fun IParamStatement.write(buffer: ByteBuffer, offsets: MutableList<ParamOffset>? = null): Boolean = when(this) {
    is IParamDeleteStatement -> { buffer.put(getID()); write(buffer) }
    is IParamClass -> { buffer.put(getID()); write(buffer, offsets) }
    is IParamExternalClass -> { buffer.put(getID()); write(buffer) }
    is IParamVariableStatement -> { buffer.put(getID()); write(buffer) }
    else -> throw Exception("Unknown statement in context of ParamFile Binarization")
}


fun IParamLiteralBase.getID(): Byte = when (this) {
    is IParamString -> 0
    is IParamFloat -> 1
    is IParamInt -> 2
    is IParamArray -> 3
    else -> throw Exception("Unknown literal in context of ParamFile Binarization")
}

fun IParamStatement.getID(): Byte = when(this) {
    is IParamDeleteStatement -> 4
    is IParamClass -> 0
    is IParamExternalClass -> 3
    is IParamVariableStatement -> if(slimValue !is IParamArray) 1 else if(slimOperator != ParamOperatorTypes.ASSIGN) 5 else 2
    else -> throw Exception("Unknown statement in context of ParamFile Binarization")
}
