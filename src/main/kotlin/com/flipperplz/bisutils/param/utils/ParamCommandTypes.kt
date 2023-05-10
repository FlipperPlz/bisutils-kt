package com.flipperplz.bisutils.param.utils

import com.flipperplz.bisutils.param.directive.ParamSlimDefine
import com.flipperplz.bisutils.param.directive.ParamSlimInclude
import com.flipperplz.bisutils.param.directive.ParamSlimMacro
import com.flipperplz.bisutils.param.directive.ParamSlimUndefine
import com.flipperplz.bisutils.param.node.RapStatement
import com.flipperplz.bisutils.param.statement.RapClass
import com.flipperplz.bisutils.param.statement.RapDeleteStatement
import com.flipperplz.bisutils.param.statement.RapExternalClass
import com.flipperplz.bisutils.param.statement.RapVariableStatement
import kotlin.reflect.KClass

/**
 * Enum class that represents different types of parameter commands.
 * @property debugName the name of the command type for debug purposes.
 * @property elementType the class of the element type for this command type.
 * @property type the type of the element type for this command type.
 */
enum class ParamCommandTypes(
    val debugName: String,
    val elementType: KClass<out RapStatement>,
    val type: ParamElementTypes
) {
    DELETE("delete", RapDeleteStatement::class, ParamElementTypes.C_DELETE),
    VARIABLE("variable", RapVariableStatement::class, ParamElementTypes.C_VARIABLE),
    ARRAY("array", RapVariableStatement::class, ParamElementTypes.C_VARIABLE_ARRAY),
    EXTERNAL_CLASS("external", RapExternalClass::class, ParamElementTypes.C_CLASS_EXTERNAL),
    CLASS("class", RapClass::class, ParamElementTypes.C_CLASS),
    PREPROCESSOR_UNDEFINE("undefine", ParamSlimUndefine::class, ParamElementTypes.CP_UNDEF),
    PREPROCESSOR_INCLUDE("include", ParamSlimInclude::class, ParamElementTypes.C_INCLUDE),
    MACRO("__macro__", ParamSlimMacro::class, ParamElementTypes.C_MACRO),
    PREPROCESSOR_DEFINE("define", ParamSlimDefine::class, ParamElementTypes.CP_DEFINE);

    /**
     * Checks if the element type is a kind of T.
     * @return true if the element type is a kind of T, false otherwise.
     */
    inline fun <reified T> isKindOf(): Boolean = T::class.isInstance(elementType)
    companion object {
        inline fun <reified T> fromType(): ParamCommandTypes? = ParamCommandTypes.values()
            .firstOrNull { it.isKindOf<T>() }

    }
}