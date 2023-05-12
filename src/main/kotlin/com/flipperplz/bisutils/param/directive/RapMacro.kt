package com.flipperplz.bisutils.param.directive

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.node.*
import com.flipperplz.bisutils.param.node.RapProcessable
import com.flipperplz.bisutils.param.statement.RapVariableStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapMacro : RapDirective, RapLiteral<List<RapElement>>, RapNamedElement, RapProcessable {
    val slimMacroArguments: List<String>

    override val slimValue: List<RapElement>?
        get() = processSlim()

    fun locateMacro(): RapDefine?

    fun isLiteralValue(): Boolean =
        slimParent is RapArray ||
        slimParent is RapVariableStatement

    fun shouldValidateMacro(): Boolean

    override fun processSlim(): List<RapElement>? =
        locateMacro()?.evaluateMacro(slimMacroArguments)

    override fun isCurrentlyValid(): Boolean =!slimName.isNullOrBlank() && (
        shouldValidateMacro() &&
        locateMacro()?.isCurrentlyValid() ?: false
    )

    override fun getRapElementType(): ParamElementTypes =
        if(isLiteralValue())
            ParamElementTypes.L_INCLUDE else
            ParamElementTypes.C_INCLUDE

    override fun toParam(): String = slimName + slimMacroArguments.joinToString(
        prefix = "(",
        separator = ",",
        postfix = if (!isLiteralValue())
            ");" else
            ")"
    ) { it }
}