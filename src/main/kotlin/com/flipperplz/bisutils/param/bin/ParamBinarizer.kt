package com.flipperplz.bisutils.param.bin

import com.flipperplz.bisutils.param.ast.literal.ParamFloat
import com.flipperplz.bisutils.param.ast.literal.ParamInt
import com.flipperplz.bisutils.param.ast.literal.ParamString
import com.flipperplz.bisutils.param.ast.node.ParamElement
import com.flipperplz.bisutils.param.ast.statement.ParamDeleteStatement
import com.flipperplz.bisutils.param.ast.statement.ParamExternalClass
import com.flipperplz.bisutils.param.utils.extensions.invoke
import com.flipperplz.bisutils.utils.getAsciiZ
import com.flipperplz.bisutils.utils.getFloat
import com.flipperplz.bisutils.utils.getInt
import com.flipperplz.bisutils.utils.putAsciiZ
import java.nio.ByteBuffer
import java.nio.ByteOrder

//object ParamBinarizer {

fun ParamString.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putAsciiZ(slimValue!!)
    true
}

fun ParamFloat.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putFloat(slimValue!!)
    true
}

fun ParamInt.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putInt(slimValue!!)
    true
}

fun ParamExternalClass.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putAsciiZ(slimName!!)
    true
}

fun ParamDeleteStatement.write(buffer: ByteBuffer): Boolean = if(!isCurrentlyValid()) false else {
    buffer.putAsciiZ(slimName!!)
    true
}
//}