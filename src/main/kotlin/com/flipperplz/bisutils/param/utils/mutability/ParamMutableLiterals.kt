package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.literal.ParamArray
import com.flipperplz.bisutils.param.literal.ParamFloat
import com.flipperplz.bisutils.param.literal.ParamInt
import com.flipperplz.bisutils.param.literal.ParamString
import com.flipperplz.bisutils.param.node.ParamElement
import com.flipperplz.bisutils.param.node.ParamLiteralBase
import com.flipperplz.bisutils.param.utils.ParamStringType


abstract class ParamMutableLiteral(
    slimParent: ParamElement? = null,
): ParamMutableElement(slimParent), ParamLiteralBase

class ParamMutableString(
    slimParent: ParamElement? = null,
    override var slimStringType: ParamStringType = ParamStringType.QUOTED,
    override var slimValue: String? = null
): ParamMutableLiteral(slimParent), ParamString

class ParamMutableInt(
    slimParent: ParamElement? = null,
    override var slimValue: Int? = null,
): ParamMutableLiteral(slimParent), ParamInt

class ParamMutableFloat(
    slimParent: ParamElement? = null,
    override var slimValue: Float? = null,
): ParamMutableLiteral(slimParent), ParamFloat

class ParamMutableArray(
    slimParent: ParamElement? = null,
    override var slimValue: MutableList<ParamLiteralBase>? = null,
): ParamMutableLiteral(slimParent), ParamArray
