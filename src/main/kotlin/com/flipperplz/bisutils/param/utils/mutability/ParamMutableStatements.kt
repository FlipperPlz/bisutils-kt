package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.RapFile
import com.flipperplz.bisutils.param.node.*
import com.flipperplz.bisutils.param.statement.*
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes

abstract class ParamMutableStatement(
    slimParent: RapElement? = null,
): ParamMutableElement(slimParent), RapStatement

class ParamMutableDeleteStatement(
    slimParent: RapElement? = null,
    override var slimName: String?,
    var shouldValidateTarget: Boolean = false,
    var locateTargetClass: ((RapDeleteStatement) -> RapExternalClass?)? = null
): ParamMutableStatement(slimParent), RapDeleteStatement {
    override fun shouldValidateTarget(): Boolean = shouldValidateTarget
    override fun locateTargetClass(): RapExternalClass? = locateTargetClass?.let { it(this) }
}

class ParamMutableVariableStatement(
    slimParent: RapElement? = null,
    override var slimName: String? = null,
    override var slimValue: RapLiteralBase? = null,
    override var slimOperator: ParamOperatorTypes? = ParamOperatorTypes.ASSIGN,
): ParamMutableStatement(slimParent), RapVariableStatement

open class ParamMutableExternalClass(
    slimParent: RapElement? = null,
    override var slimName: String? = null,
): ParamMutableStatement(slimParent), RapExternalClass

class ParamMutableClass(
    slimParent: RapElement? = null,
    className: String? = null,
    override var slimSuperClass: String? = null,
    override var slimCommands: MutableList<RapStatement> = mutableListOf(),
    var shouldLocateSuperClass: Boolean = false,
    var locateSuperClass: ((RapClass) -> RapExternalClass?)?
): ParamMutableExternalClass(slimParent, className), RapClass {
    override fun shouldValidateSuper(): Boolean = shouldLocateSuperClass
    override fun locateSuperClass(): RapExternalClass? = locateSuperClass?.let { it(this) }
}

class ParamMutableEnum(
    slimParent: RapElement? = null,
    override var enumValues: MutableMap<String, Int>?,
): ParamMutableStatement(slimParent), RapEnum
