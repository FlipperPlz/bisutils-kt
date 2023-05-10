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

    override val slimCurrentlyValid: Boolean
        get() = super<RapExternalClass>.slimCurrentlyValid &&
                super<RapStatementHolder>.slimCurrentlyValid &&
                (slimSuperClass?.isNotEmpty() ?: true)


    override fun toEnforce(): String {
        val builder = StringBuilder(super<RapExternalClass>.toEnforce())
        if (slimSuperClass != null) builder.append(" : ").append(slimSuperClass)
        builder.append(" { \n")
        builder.append(super<RapStatementHolder>.toEnforce())
        return builder.append("};").toString()
    }
}