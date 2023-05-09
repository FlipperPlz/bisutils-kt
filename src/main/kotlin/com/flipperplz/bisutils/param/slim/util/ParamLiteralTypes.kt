package com.flipperplz.bisutils.param.slim.util

import com.flipperplz.bisutils.param.slim.directive.ParamSlimMacro
import com.flipperplz.bisutils.param.slim.literals.*
import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteral
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
    ARRAY("array", ParamSlimArray::class, List::class, ParamElementTypes.L_ARRAY),
    REFERENCE("ref", ParamSlimReference::class, ParamSlimLiteral::class, ParamElementTypes.L_REFERENCE),
    MACRO("__macro__", ParamSlimMacro::class, ParamSlimMacro::class, ParamElementTypes.L_MACRO);

    inline fun <reified T> isKindOf(): Boolean = T::class.isSuperclassOf(elementType) || T::class.isSuperclassOf(baseType)

    companion object {
        inline fun <reified T> fromType(): ParamLiteralTypes? = values().firstOrNull { it.isKindOf<T>() }

    }
}