package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.node.*
import com.flipperplz.bisutils.param.statement.*
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes

abstract class ParamMutableStatement(
    slimParent: ParamElement? = null,
): ParamMutableElement(slimParent), ParamStatement

class ParamMutableDeleteStatement(
    slimParent: ParamElement? = null,
    override var slimName: String?,
    var shouldValidateTarget: Boolean = false,
    var locateTargetClass: ((ParamDeleteStatement) -> ParamExternalClass?)? = null
): ParamMutableStatement(slimParent), ParamDeleteStatement {
    override fun shouldValidateTarget(): Boolean = shouldValidateTarget
    override fun locateTargetClass(): ParamExternalClass? = locateTargetClass?.let { it(this) }
}

class ParamMutableVariableStatement(
    slimParent: ParamElement? = null,
    override var slimName: String? = null,
    override var slimValue: ParamLiteralBase? = null,
    override var slimOperator: ParamOperatorTypes? = ParamOperatorTypes.ASSIGN,
): ParamMutableStatement(slimParent), ParamVariableStatement

open class ParamMutableExternalClass(
    slimParent: ParamElement? = null,
    override var slimName: String? = null,
): ParamMutableStatement(slimParent), ParamExternalClass

class ParamMutableClass(
    slimParent: ParamElement? = null,
    className: String? = null,
    override var slimSuperClass: String? = null,
    override var slimCommands: MutableList<ParamStatement> = mutableListOf(),
    var shouldLocateSuperClass: Boolean = false,
    var locateSuperClass: ((ParamClass) -> ParamExternalClass?)?
): ParamMutableExternalClass(slimParent, className), ParamClass {
    override fun shouldValidateSuper(): Boolean = shouldLocateSuperClass
    override fun locateSuperClass(): ParamExternalClass? = locateSuperClass?.let { it(this) }
}

class ParamMutableEnum(
    slimParent: ParamElement? = null,
    override var enumValues: MutableMap<String, Int>?,
): ParamMutableStatement(slimParent), ParamEnum
