package com.flipperplz.bisutils.param.slim.util

import com.flipperplz.bisutils.param.slim.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.literals.ParamSlimArray
import com.flipperplz.bisutils.param.slim.literals.ParamSlimFloat
import com.flipperplz.bisutils.param.slim.literals.ParamSlimInt
import com.flipperplz.bisutils.param.slim.literals.ParamSlimString
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

//very good circular dependency pattern**
enum class ParamLiteralTypes(
    val debugName: String,
    val elementType: KClass<out ParamSlimLiteral<*>>,
    val baseType: KClass<*>,
    val type: ParamElementTypes
) {
    STRING("string", ParamSlimString::class, String::class, ParamElementTypes.L_STRING),
    INTEGER("int", ParamSlimInt::class, Int::class, ParamElementTypes.L_INT),
    FLOAT("float", ParamSlimFloat::class, Float::class, ParamElementTypes.L_FLOAT),
    ARRAY("array", ParamSlimArray::class, List::class, ParamElementTypes.L_ARRAY);

    inline fun <reified T> isKindOf(): Boolean = T::class.isSuperclassOf(elementType) || T::class.isSuperclassOf(baseType)

    companion object {
        inline fun <reified T> fromType(): ParamLiteralTypes? = values().firstOrNull { it.isKindOf<T>() }

    }
}