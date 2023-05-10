package com.flipperplz.bisutils.param.utils

import com.flipperplz.bisutils.param.directive.RapInclude
import com.flipperplz.bisutils.param.directive.RapMacro
import com.flipperplz.bisutils.param.literal.*
import com.flipperplz.bisutils.param.node.RapLiteralBase
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

//very good circular dependency pattern**
enum class ParamLiteralTypes(
    val debugName: String,
    val elementType: KClass<out RapLiteralBase>,
    val baseType: KClass<*>,
    val type: ParamElementTypes
) {
    STRING("string", RapString::class, String::class, ParamElementTypes.L_STRING),
    INTEGER("int", RapInt::class, Int::class, ParamElementTypes.L_INT),
    FLOAT("float", RapFloat::class, Float::class, ParamElementTypes.L_FLOAT),
    ARRAY("array", RapArray::class, List::class, ParamElementTypes.L_ARRAY),
    REFERENCE("ref", RapReference::class, RapLiteralBase::class, ParamElementTypes.L_REFERENCE),
    PREPROCESSOR_MACRO("__macro__", RapMacro::class, RapMacro::class, ParamElementTypes.L_MACRO),
    PREPROCESSOR_INCLUDE("include", RapInclude::class, RapInclude::class, ParamElementTypes.L_INCLUDE);

    inline fun <reified T> isKindOf(): Boolean =
        T::class.isSuperclassOf(elementType) || T::class.isSuperclassOf(baseType)

    companion object {
        inline fun <reified T> fromType(): ParamLiteralTypes? = values().firstOrNull { it.isKindOf<T>() }

    }
}