package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.node.RapLiteralBase
import com.flipperplz.bisutils.param.node.RapProcessable
import com.flipperplz.bisutils.param.statement.RapVariableStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.ParamStringType

interface RapReference : RapLiteral<String>, RapProcessable {
    val slimStringType: ParamStringType

    fun locateReference(): RapVariableStatement?

    fun shouldValidateReference(): Boolean

    override fun isBinarizable(): Boolean =
        false

    override fun processSlim(): List<RapElement>? = locateReference()?.let {
        listOf( //TODO:// Make RapElement cloneable element::clone(parent: RapElement?)
            object : RapVariableStatement {
                override val slimParent: RapElement = this@RapReference

                override val slimName: String? = it.slimName
                override val slimOperator: ParamOperatorTypes? = it.slimOperator
                override val slimValue: RapLiteralBase? = it.slimValue
            }
        )
    }

    override fun getRapElementType(): ParamElementTypes =
        ParamElementTypes.L_REFERENCE

    override fun isCurrentlyValid(): Boolean =
        super.isCurrentlyValid() &&
        (shouldValidateReference() && locateReference() != null) &&
        slimStringType != ParamStringType.ANGLE

    override fun toParam(): String =
        "@${slimStringType.stringify(slimValue ?: "")}"
}