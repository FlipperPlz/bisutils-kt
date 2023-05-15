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
    containingFile: RapFile? = slimParent?.containingFile
): ParamMutableElement(slimParent, containingFile), RapLiteralBase

class ParamMutableString(
    slimParent: RapElement? = null,
    containingFile: RapFile? = slimParent?.containingFile,
    override var slimStringType: ParamStringType = ParamStringType.QUOTED,
    override var slimValue: String? = null
): ParamMutableLiteral(slimParent, containingFile), RapString

class ParamMutableInt(
    slimParent: RapElement? = null,
    containingFile: RapFile? = slimParent?.containingFile,
    override var slimValue: Int? = null,
): ParamMutableLiteral(slimParent, containingFile), RapInt

class ParamMutableFloat(
    slimParent: RapElement? = null,
    containingFile: RapFile? = slimParent?.containingFile,
    override var slimValue: Float? = null,
): ParamMutableLiteral(slimParent, containingFile), RapFloat

class ParamMutableArray(
    slimParent: RapElement? = null,
    containingFile: RapFile? = slimParent?.containingFile,
    override var slimValue: MutableList<RapLiteralBase>? = null,
): ParamMutableLiteral(slimParent, containingFile), RapArray
