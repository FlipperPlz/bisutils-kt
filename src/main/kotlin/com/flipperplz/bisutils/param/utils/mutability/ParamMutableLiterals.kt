package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.IParamFile
import com.flipperplz.bisutils.param.ast.literal.IParamArray
import com.flipperplz.bisutils.param.ast.literal.IParamFloat
import com.flipperplz.bisutils.param.ast.literal.IParamInt
import com.flipperplz.bisutils.param.ast.literal.IParamString
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.ast.node.IParamLiteralBase
import com.flipperplz.bisutils.param.utils.ParamStringType
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableElement


interface ParamMutableLiteral: ParamMutableElement, IParamLiteralBase

class ParamMutableStringImpl(
    override var slimParent: IParamElement? = null,
    override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
    override var slimStringType: ParamStringType = ParamStringType.QUOTED,
    override var slimValue: String? = null
): ParamMutableString, CharSequence by  slimValue ?: ""

interface ParamMutableString : ParamMutableLiteral, IParamString {
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    override var slimStringType: ParamStringType
    override var slimValue: String?
}

class ParamMutableIntImpl(
    override var slimParent: IParamElement? = null,
    override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
    override var slimValue: Int? = null,
): ParamMutableInt

interface ParamMutableInt : ParamMutableLiteral, IParamInt {
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    override var slimValue: Int?
}

class ParamMutableFloatImpl(
    override var slimParent: IParamElement? = null,
    override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
    override var slimValue: Float? = null,
): ParamMutableFloat

interface ParamMutableFloat : ParamMutableLiteral, IParamFloat {
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    override var slimValue: Float?
}

class ParamMutableArrayImpl(
    override var slimParent: IParamElement? = null,
    override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
    override var slimValue: MutableList<IParamLiteralBase>? = mutableListOf(),
): ParamMutableArray, MutableList<IParamLiteralBase> by slimValue ?: mutableListOf<IParamLiteralBase>()

interface ParamMutableArray : ParamMutableLiteral, IParamArray, MutableList<IParamLiteralBase> {
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    override var slimValue: MutableList<IParamLiteralBase>?
}