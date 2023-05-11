package com.flipperplz.bisutils.param.statement

import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.node.RapStatement
import com.flipperplz.bisutils.param.utils.ParamCommandTypes

interface RapDeleteStatement : RapStatement, RapNamedElement {
    override val statementId: Byte
        get() = 4
    val slimDeleteTarget: String?

    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.DELETE
    override val slimCurrentlyValid: Boolean
        get() = super<RapStatement>.slimCurrentlyValid && !slimDeleteTarget.isNullOrBlank()

    override val slimName: String?
        get() = slimDeleteTarget

    override fun toParam(): String = "delete $slimDeleteTarget;"
}