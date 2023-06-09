package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.ast.IParamFile
import com.flipperplz.bisutils.param.ast.literal.IParamArray
import com.flipperplz.bisutils.param.ast.literal.IParamFloat
import com.flipperplz.bisutils.param.ast.literal.IParamInt
import com.flipperplz.bisutils.param.ast.literal.IParamString
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.utils.ParamStringType
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableElement


interface ParamMutableLiteral: ParamMutableElement, IParamLiteralBase

class ParamMutableStringImpl(
        override var slimParent: IParamElement? = null,
        override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
        override var slimStringType: ParamStringType = ParamStringType.QUOTED,
        override var paramValue: String? = null
): ParamMutableString, CharSequence by  paramValue ?: ""

interface ParamMutableString : ParamMutableLiteral, IParamString {
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    override var slimStringType: ParamStringType
    var paramValue: String?
}

class ParamMutableIntImpl(
        override var slimParent: IParamElement? = null,
        override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
        override var paramValue: Int? = null,
): ParamMutableInt

interface ParamMutableInt : ParamMutableLiteral, IParamInt {
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    override var paramValue: Int?
}

class ParamMutableFloatImpl(
        override var slimParent: IParamElement? = null,
        override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
        override var paramValue: Float? = null,
): ParamMutableFloat

interface ParamMutableFloat : ParamMutableLiteral, IParamFloat {
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    var paramValue: Float?
}

class ParamMutableArrayImpl(
        override var slimParent: IParamElement? = null,
        override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
        override var paramValue: MutableList<IParamLiteralBase>? = mutableListOf(),
): ParamMutableArray, MutableList<IParamLiteralBase> by paramValue ?: mutableListOf<IParamLiteralBase>()

interface ParamMutableArray : ParamMutableLiteral, IParamArray, MutableList<IParamLiteralBase> {
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    override var paramValue: MutableList<IParamLiteralBase>?
}