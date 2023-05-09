package com.flipperplz.bisutils.param.slim.util

import com.flipperplz.bisutils.param.slim.node.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.commands.ParamSlimClass
import com.flipperplz.bisutils.param.slim.commands.ParamSlimDeleteStatement
import com.flipperplz.bisutils.param.slim.commands.ParamSlimExternalClass
import com.flipperplz.bisutils.param.slim.commands.ParamSlimVariableStatement
import com.flipperplz.bisutils.param.slim.directive.ParamSlimInclude
import com.flipperplz.bisutils.param.slim.directive.ParamSlimMacro
import com.flipperplz.bisutils.param.slim.directive.ParamSlimUndefine
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
    CLASS("class", ParamSlimClass::class, ParamElementTypes.C_CLASS),
    PREPROCESSOR_UNDEFINE("undefine", ParamSlimUndefine::class, ParamElementTypes.CP_UNDEF),
    PREPROCESSOR_INCLUDE("include", ParamSlimInclude::class, ParamElementTypes.CP_INCLUDE),
    MACRO("__macro__", ParamSlimMacro::class, ParamElementTypes.C_MACRO);

    inline fun <reified T> isKindOf(): Boolean = T::class.isInstance(elementType)
    companion object {
        inline fun <reified T> fromType(): ParamCommandTypes? = ParamCommandTypes.values()
            .firstOrNull { it.isKindOf<T>() }

    }
}