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

class ParamMutableString(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimStringType: ParamStringType = ParamStringType.QUOTED,
    override var slimValue: String? = null
): ParamMutableLiteral, ParamString

class ParamMutableInt(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimValue: Int? = null,
): ParamMutableLiteral, ParamInt

class ParamMutableFloat(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimValue: Float? = null,
): ParamMutableLiteral, ParamFloat

class ParamMutableArray(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimValue: MutableList<ParamLiteralBase>? = null,
): ParamMutableLiteral, ParamArray
