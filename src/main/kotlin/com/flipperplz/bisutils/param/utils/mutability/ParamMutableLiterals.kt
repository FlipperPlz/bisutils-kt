package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.RapFile
import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.literal.RapFloat
import com.flipperplz.bisutils.param.literal.RapInt
import com.flipperplz.bisutils.param.literal.RapString
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapLiteralBase
import com.flipperplz.bisutils.param.utils.ParamStringType


abstract class ParamMutableLiteral(
    slimParent: RapElement? = null,
): ParamMutableElement(slimParent), RapLiteralBase

class ParamMutableString(
    slimParent: RapElement? = null,
    override var slimStringType: ParamStringType = ParamStringType.QUOTED,
    override var slimValue: String? = null
): ParamMutableLiteral(slimParent), RapString

class ParamMutableInt(
    slimParent: RapElement? = null,
    override var slimValue: Int? = null,
): ParamMutableLiteral(slimParent), RapInt

class ParamMutableFloat(
    slimParent: RapElement? = null,
    override var slimValue: Float? = null,
): ParamMutableLiteral(slimParent), RapFloat

class ParamMutableArray(
    slimParent: RapElement? = null,
    override var slimValue: MutableList<RapLiteralBase>? = null,
): ParamMutableLiteral(slimParent), RapArray
