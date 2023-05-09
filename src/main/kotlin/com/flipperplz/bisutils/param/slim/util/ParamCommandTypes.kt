package com.flipperplz.bisutils.param.slim.util

import com.flipperplz.bisutils.param.slim.structure.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.structure.commands.ParamSlimClass
import com.flipperplz.bisutils.param.slim.structure.commands.ParamSlimDeleteStatement
import com.flipperplz.bisutils.param.slim.structure.commands.ParamSlimExternalClass
import com.flipperplz.bisutils.param.slim.structure.commands.ParamSlimVariableStatement
import kotlin.reflect.KClass

enum class ParamCommandTypes(
    val debugName: String,
    val elementType: KClass<out ParamSlimCommand>
) {
    DELETE("delete", ParamSlimDeleteStatement::class),
    VARIABLE("variable", ParamSlimVariableStatement::class),
    EXTERNAL_CLASS("external", ParamSlimExternalClass::class),
    CLASS("class", ParamSlimClass::class);

    inline fun <reified T> isKindOf(): Boolean = T::class.isInstance(elementType)
    companion object {
        inline fun <reified T> fromType(): ParamCommandTypes? = ParamCommandTypes.values()
            .firstOrNull { it.isKindOf<T>() }

    }
}