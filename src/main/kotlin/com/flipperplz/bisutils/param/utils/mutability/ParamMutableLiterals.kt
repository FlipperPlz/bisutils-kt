package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.literal.ParamArray
import com.flipperplz.bisutils.param.ast.literal.ParamFloat
import com.flipperplz.bisutils.param.ast.literal.ParamInt
import com.flipperplz.bisutils.param.ast.literal.ParamString
import com.flipperplz.bisutils.param.ast.node.ParamElement
import com.flipperplz.bisutils.param.ast.node.ParamLiteralBase
import com.flipperplz.bisutils.param.utils.ParamStringType
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableElement


interface ParamMutableLiteral: ParamMutableElement, ParamLiteralBase

class ParamMutableStringImpl(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimStringType: ParamStringType = ParamStringType.QUOTED,
    override var slimValue: String? = null
): ParamMutableString

interface ParamMutableString : ParamMutableLiteral, ParamString {
    override var slimParent: ParamElement?
    override var containingParamFile: ParamFile?
    override var slimStringType: ParamStringType
    override var slimValue: String?
}

class ParamMutableIntImpl(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimValue: Int? = null,
): ParamMutableInt

interface ParamMutableInt : ParamMutableLiteral, ParamInt {
    override var slimParent: ParamElement?
    override var containingParamFile: ParamFile?
    override var slimValue: Int?
}

class ParamMutableFloatImpl(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimValue: Float? = null,
): ParamMutableFloat

interface ParamMutableFloat : ParamMutableLiteral, ParamFloat {
    override var slimParent: ParamElement?
    override var containingParamFile: ParamFile?
    override var slimValue: Float?
}

class ParamMutableArrayImpl(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimValue: MutableList<ParamLiteralBase>? = mutableListOf(),
): ParamMutableArray

interface ParamMutableArray : ParamMutableLiteral, ParamArray {
    override var slimParent: ParamElement?
    override var containingParamFile: ParamFile?
    override var slimValue: MutableList<ParamLiteralBase>?
}