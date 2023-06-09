package com.flipperplz.bisutils.param.ast.node

import com.flipperplz.bisutils.binarization.interfaces.IStrictBinaryObject
import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.param.ast.IParamFile
import com.flipperplz.bisutils.param.options.ParamOptions

interface IParamElement : IFamilyMember, IStrictBinaryObject<ParamOptions, ParamOptions> {
    override val node: IParamFile?
    fun toParam(): String

    companion object {
        val REGEX_ALPHANUM: Regex = Regex("^[a-zA-Z0-9]+$")
    }
}