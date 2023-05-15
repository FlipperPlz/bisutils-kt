package com.flipperplz.bisutils.param.statement

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapStatementHolder
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapClass : RapElement, RapExternalClass, RapStatementHolder {
    companion object
    val slimSuperClass: String?

    override fun isBinarizable(): Boolean = true
    override fun getRapElementType(): ParamElementTypes = ParamElementTypes.C_CLASS

    fun locateSuperClass(): RapExternalClass?

    fun shouldValidateSuper(): Boolean

    override fun isCurrentlyValid(): Boolean = if(slimSuperClass.isNullOrBlank() || !shouldValidateSuper())
        super<RapExternalClass>.isCurrentlyValid() else
        locateSuperClass() != null

    override fun toParam(): String {
        val builder = StringBuilder(super<RapExternalClass>.toParam().trimEnd(';'))
        if (!slimSuperClass.isNullOrBlank()) builder.append(" : ").append(slimSuperClass)
        builder.append(" { \n")
        builder.append(writeSlimCommands())
        return builder.append("};").toString()
    }
}