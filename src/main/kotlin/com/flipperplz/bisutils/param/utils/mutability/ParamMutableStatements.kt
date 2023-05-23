package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.node.*
import com.flipperplz.bisutils.param.ast.statement.*
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableElement
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder

interface ParamMutableStatement: ParamMutableElement, ParamStatement

class ParamMutableDeleteStatementImpl(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimName: String?,
    override var shouldValidateTarget: Boolean = false,
    override var locateTargetClass: ((ParamDeleteStatement) -> ParamExternalClass?)? = null
): ParamMutableDeleteStatement

interface ParamMutableDeleteStatement : ParamMutableStatement, ParamDeleteStatement {
    override var slimParent: ParamElement?
    override var containingParamFile: ParamFile?
    override var slimName: String?
    var shouldValidateTarget: Boolean
    var locateTargetClass: ((ParamDeleteStatement) -> ParamExternalClass?)?
    override fun shouldValidateTarget(): Boolean = shouldValidateTarget
    override fun locateTargetClass(): ParamExternalClass? = locateTargetClass?.let { it(this) }
}

class ParamMutableVariableStatementImpl(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimName: String? = null,
    override var slimValue: ParamLiteralBase? = null,
    override var slimOperator: ParamOperatorTypes? = ParamOperatorTypes.ASSIGN,
): ParamMutableVariableStatement

interface ParamMutableVariableStatement : ParamMutableStatement, ParamVariableStatement{
    override var slimParent: ParamElement?
    override var containingParamFile: ParamFile?
    override var slimName: String?
    override var slimValue: ParamLiteralBase?
    override var slimOperator: ParamOperatorTypes?
}

open class ParamMutableExternalClassImpl(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimName: String? = null,
): ParamMutableExternalClass

interface ParamMutableExternalClass: ParamMutableStatement, ParamExternalClass {
    override var slimParent: ParamElement?
    override var containingParamFile: ParamFile?
    override var slimName: String?
}

class ParamMutableClassImpl(
    override var slimParent: ParamElement?,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var slimName: String? = null,
    override var slimSuperClass: String? = null,
    override var slimCommands: MutableList<ParamStatement> = mutableListOf(),
    override var shouldLocateSuperClass: Boolean = false,
    override var locateSuperClass: ((ParamClass) -> ParamExternalClass?)? = null
): ParamMutableClass

interface ParamMutableClass : ParamMutableExternalClass, ParamMutableStatementHolder, ParamClass {
    override var slimParent: ParamElement?
    override var containingParamFile: ParamFile?
    override var slimName: String?
    override var slimSuperClass: String?
    override var slimCommands: MutableList<ParamStatement>
    var shouldLocateSuperClass: Boolean
    var locateSuperClass: ((ParamClass) -> ParamExternalClass?)?

    override fun shouldValidateSuper(): Boolean = shouldLocateSuperClass
    override fun locateSuperClass(): ParamExternalClass? = locateSuperClass?.let { it(this) }
}

class ParamMutableEnum(
    override var slimParent: ParamElement? = null,
    override var containingParamFile: ParamFile? = slimParent?.containingParamFile,
    override var enumValues: MutableMap<String, Int>,
): ParamMutableStatement, ParamEnum
