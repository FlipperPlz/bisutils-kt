package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.ast.IParamFile
import com.flipperplz.bisutils.param.ast.node.*
import com.flipperplz.bisutils.param.ast.statement.*
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableElement
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder

interface ParamMutableStatement: ParamMutableElement, IParamStatement

class ParamMutableDeleteStatementImpl(
        override var slimParent: IParamElement? = null,
        override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
        override var slimName: String?,
        override var shouldValidateTarget: Boolean = false,
        override var locateTargetClass: ((IParamDeleteStatement) -> IParamExternalClass?)? = null
): ParamMutableDeleteStatement

interface ParamMutableDeleteStatement : ParamMutableStatement, IParamDeleteStatement {
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    override var slimName: String?
    var shouldValidateTarget: Boolean
    var locateTargetClass: ((IParamDeleteStatement) -> IParamExternalClass?)?
    override fun shouldValidateTarget(): Boolean = shouldValidateTarget
    override fun locateTargetClass(): IParamExternalClass? = locateTargetClass?.let { it(this) }
}

class ParamMutableVariableStatementImpl(
        override var slimParent: IParamElement? = null,
        override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
        override var slimName: String? = null,
        override var paramValue: IParamLiteralBase? = null,
        override var paramOperator: ParamOperatorTypes? = ParamOperatorTypes.ASSIGN,
): ParamMutableVariableStatement

interface ParamMutableVariableStatement : ParamMutableStatement, IParamVariableStatement{
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    override var slimName: String?
    override var paramValue: IParamLiteralBase?
    override var paramOperator: ParamOperatorTypes?
}

open class ParamMutableExternalClassImpl(
        override var slimParent: IParamElement? = null,
        override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
        override var slimName: String? = null,
): ParamMutableExternalClass

interface ParamMutableExternalClass: ParamMutableStatement, IParamExternalClass {
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    override var slimName: String?
}

class ParamMutableClassImpl(
        override var slimParent: IParamElement?,
        override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
        override var slimName: String? = null,
        override var paramSuperClassname: String? = null,
        override var slimCommands: MutableList<IParamStatement> = mutableListOf(),
        override var shouldLocateSuperClass: Boolean = false,
        override var locateSuperClass: ((IParamClass) -> IParamExternalClass?)? = null
): ParamMutableClass

interface ParamMutableClass : ParamMutableExternalClass, ParamMutableStatementHolder, IParamClass {
    override var slimParent: IParamElement?
    override var containingParamFile: IParamFile?
    override var slimName: String?
    override var paramSuperClassname: String?
    override var slimCommands: MutableList<IParamStatement>
    var shouldLocateSuperClass: Boolean
    var locateSuperClass: ((IParamClass) -> IParamExternalClass?)?

    override fun shouldValidateSuper(): Boolean = shouldLocateSuperClass
    override fun locateSuperClass(): IParamExternalClass? = locateSuperClass?.let { it(this) }
}

class ParamMutableEnum(
        override var slimParent: IParamElement? = null,
        override var containingParamFile: IParamFile? = slimParent?.containingParamFile,
        override var enumValues: MutableMap<String, IParamNumerical>,
): ParamMutableStatement, IParamEnum
