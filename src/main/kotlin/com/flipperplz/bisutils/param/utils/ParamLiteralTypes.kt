package com.flipperplz.bisutils.param.utils

import com.flipperplz.bisutils.param.directive.ParamSlimInclude
import com.flipperplz.bisutils.param.directive.ParamSlimMacro
import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.literal.RapFloat
import com.flipperplz.bisutils.param.literal.RapInt
import com.flipperplz.bisutils.param.literal.RapString
import com.flipperplz.bisutils.param.node.RapLiteralBase
import com.flipperplz.bisutils.param.literal.ParamSlimReference
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
    REFERENCE("ref", ParamSlimReference::class, RapLiteralBase::class, ParamElementTypes.L_REFERENCE),
    PREPROCESSOR_MACRO("__macro__", ParamSlimMacro::class, ParamSlimMacro::class, ParamElementTypes.L_MACRO),
    PREPROCESSOR_INCLUDE("include", ParamSlimInclude::class, ParamSlimInclude::class, ParamElementTypes.L_INCLUDE);

    inline fun <reified T> isKindOf(): Boolean = T::class.isSuperclassOf(elementType) || T::class.isSuperclassOf(baseType)

    companion object {
        inline fun <reified T> fromType(): ParamLiteralTypes? = values().firstOrNull { it.isKindOf<T>() }

    }
}