package com.flipperplz.bisutils.param.statement

import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.node.RapStatement
import com.flipperplz.bisutils.param.utils.ParamCommandTypes

interface RapExternalClass : RapStatement, RapNamedElement {
    override val statementId: Byte
        get() = 3
    val slimClassName: String?

    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.EXTERNAL_CLASS
    override val slimCurrentlyValid: Boolean
        get() = super<RapStatement>.slimCurrentlyValid && !slimClassName.isNullOrBlank()

    override fun toParam(): String = "class $slimClassName;"
    override val slimName: String?
        get() = slimClassName
}