package com.flipperplz.bisutils.param.slim.util

import com.flipperplz.bisutils.param.slim.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

//very good circular dependency pattern**
enum class ParamLiteralTypes(
    val debugName: String,
    val elementType: KClass<out ParamSlimLiteral<*>>,
    val baseType: KClass<*>
) {
    STRING("string", ParamSlimString::class, String::class),
    INTEGER("int", ParamSlimInt::class, Int::class),
    FLOAT("float", ParamSlimFloat::class, Float::class),
    ARRAY("array", ParamSlimArray::class, List::class);

    inline fun <reified T> isKindOf(): Boolean = T::class.isSuperclassOf(elementType) || T::class.isSuperclassOf(baseType)

    companion object {
        inline fun <reified T> fromType(): ParamLiteralTypes? = values().firstOrNull { it.isKindOf<T>() }

    }
}