package com.flipperplz.bisutils.param.statement

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapStatementHolder
import com.flipperplz.bisutils.param.utils.ParamCommandTypes

interface RapClass : RapElement, RapExternalClass, RapStatementHolder {
    override val statementId: Byte
        get() = 0
    val slimSuperClass: String?
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.CLASS

    //TODO(Ryann): Factor in if superClass exists at interface level
    override val slimCurrentlyValid: Boolean
        get() = super<RapExternalClass>.slimCurrentlyValid &&
                super<RapStatementHolder>.slimCurrentlyValid &&
                (slimSuperClass?.isNotEmpty() ?: true)


    override fun toParam(): String {
        val builder = StringBuilder(super<RapExternalClass>.toParam().trimEnd(';'))
        if (!slimSuperClass.isNullOrBlank()) builder.append(" : ").append(slimSuperClass)
        builder.append(" { \n")
        builder.append(super<RapStatementHolder>.toParam())
        return builder.append("};").toString()
    }
}