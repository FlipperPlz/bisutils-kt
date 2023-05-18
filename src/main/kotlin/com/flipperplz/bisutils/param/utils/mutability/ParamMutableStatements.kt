package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.node.*
import com.flipperplz.bisutils.param.statement.*
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableElement
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder

interface ParamMutableStatement: ParamMutableElement, ParamStatement

class ParamMutableDeleteStatement(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimName: String?,
    var shouldValidateTarget: Boolean = false,
    var locateTargetClass: ((ParamDeleteStatement) -> ParamExternalClass?)? = null
): ParamMutableStatement, ParamDeleteStatement {
    override fun shouldValidateTarget(): Boolean = shouldValidateTarget
    override fun locateTargetClass(): ParamExternalClass? = locateTargetClass?.let { it(this) }
}

class ParamMutableVariableStatement(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimName: String? = null,
    override var slimValue: ParamLiteralBase? = null,
    override var slimOperator: ParamOperatorTypes? = ParamOperatorTypes.ASSIGN,
): ParamMutableStatement, ParamVariableStatement

open class ParamMutableExternalClass(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimName: String? = null,
): ParamMutableStatement, ParamExternalClass

class ParamMutableClass(
    slimParent: ParamElement?,
    containingParamFile: ParamFile? = slimParent?.containingParamFile,
    className: String? = null,
    override var slimSuperClass: String? = null,
    override var slimCommands: MutableList<ParamStatement> = mutableListOf(),
    var shouldLocateSuperClass: Boolean = false,
    var locateSuperClass: ((ParamClass) -> ParamExternalClass?)? = null
): ParamMutableExternalClass(slimParent, containingParamFile, className), ParamMutableStatementHolder, ParamClass {
    override fun shouldValidateSuper(): Boolean = shouldLocateSuperClass
    override fun locateSuperClass(): ParamExternalClass? = locateSuperClass?.let { it(this) }
}

class ParamMutableEnum(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var enumValues: MutableMap<String, Int>,
): ParamMutableStatement, ParamEnum
