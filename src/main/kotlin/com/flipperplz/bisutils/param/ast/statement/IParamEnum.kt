package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.misc.IParamEnumValue
import com.flipperplz.bisutils.param.ast.node.IParamStatement

interface IParamEnum: IParamStatement, IFamilyParent {
    override val children: List<IParamEnumValue>?
    override fun isValid(options: IOptions?): Boolean = children?.all { it.isValid(options) } ?: true

    override fun toParam(): String =
        if(children.isNullOrEmpty()) ""
        else "enum {\n${children?.joinToString(separator = ",\n") { it.toParam() }}\n};"
}