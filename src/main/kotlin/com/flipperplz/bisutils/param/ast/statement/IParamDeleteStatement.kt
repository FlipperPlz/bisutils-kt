package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamNamedElement
import com.flipperplz.bisutils.param.ast.node.IParamStatement

interface IParamDeleteStatement : IParamStatement, IParamNamedElement {
    companion object;
    override val slimName: String?

    fun locateTargetClass(): IParamExternalClass?
    override fun isValid(options: IOptions?): Boolean =
        !slimName.isNullOrBlank() && (/*TODO: shouldValidateTarget(); /*use options*/ &&*/  locateTargetClass() != null)

    override fun isBinarizable(): Boolean = true

    override fun toParam(): String = "delete $slimName;"
}