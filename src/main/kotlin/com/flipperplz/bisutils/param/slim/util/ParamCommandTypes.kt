package com.flipperplz.bisutils.param.slim.util

import com.flipperplz.bisutils.param.slim.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.commands.ParamSlimClass
import com.flipperplz.bisutils.param.slim.commands.ParamSlimDeleteStatement
import com.flipperplz.bisutils.param.slim.commands.ParamSlimExternalClass
import com.flipperplz.bisutils.param.slim.commands.ParamSlimVariableStatement
import kotlin.reflect.KClass

enum class ParamCommandTypes(
    val debugName: String,
    val elementType: KClass<out ParamSlimCommand>,
    val type: ParamElementTypes
) {
    DELETE("delete", ParamSlimDeleteStatement::class, ParamElementTypes.C_DELETE),
    VARIABLE("variable", ParamSlimVariableStatement::class, ParamElementTypes.C_VARIABLE),
    ARRAY("array", ParamSlimVariableStatement::class, ParamElementTypes.C_VARIABLE_ARRAY),
    EXTERNAL_CLASS("external", ParamSlimExternalClass::class, ParamElementTypes.C_CLASS_EXTERNAL),
    CLASS("class", ParamSlimClass::class, ParamElementTypes.C_CLASS);

    inline fun <reified T> isKindOf(): Boolean = T::class.isInstance(elementType)
    companion object {
        inline fun <reified T> fromType(): ParamCommandTypes? = ParamCommandTypes.values()
            .firstOrNull { it.isKindOf<T>() }

    }
}