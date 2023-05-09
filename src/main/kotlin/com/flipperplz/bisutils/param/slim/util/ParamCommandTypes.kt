package com.flipperplz.bisutils.param.slim.util

import com.flipperplz.bisutils.param.slim.structure.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.structure.literals.ParamSlimString
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

enum class ParamCommandTypes(
    val debugName: String,
    val elementType: KClass<out ParamSlimLiteral<*>>
) {
    DELETE("delete", ParamSlimString::class);

    inline fun <reified T> isKindOf(): Boolean = T::class.isSuperclassOf(elementType)

    companion object {
        inline fun <reified T> fromType(): ParamLiteralTypes? = com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes.values()
            .firstOrNull { it.isKindOf<T>() }

    }
}