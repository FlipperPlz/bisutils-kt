package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.RapFile
import com.flipperplz.bisutils.param.node.*
import com.flipperplz.bisutils.param.statement.*
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes

abstract class ParamMutableStatement(
    slimParent: RapElement? = null,
    containingFile: RapFile? = slimParent?.containingFile
): ParamMutableElement(slimParent, containingFile), RapStatement

class ParamMutableDeleteStatement(
    slimParent: RapElement? = null,
    containingFile: RapFile? = slimParent?.containingFile,
    override var slimName: String?,
    var shouldValidateTarget: Boolean = false,
    var locateTargetClass: ((RapDeleteStatement) -> RapExternalClass?)? = null
): ParamMutableStatement(slimParent, containingFile), RapDeleteStatement {
    override fun shouldValidateTarget(): Boolean = shouldValidateTarget
    override fun locateTargetClass(): RapExternalClass? = locateTargetClass?.let { it(this) }
}

class ParamMutableVariableStatement(
    slimParent: RapElement? = null,
    containingFile: RapFile? = slimParent?.containingFile,
    override var slimName: String? = null,
    override var slimValue: RapLiteralBase? = null,
    override var slimOperator: ParamOperatorTypes? = ParamOperatorTypes.ASSIGN,
): ParamMutableStatement(slimParent, containingFile), RapVariableStatement

open class ParamMutableExternalClass(
    slimParent: RapElement? = null,
    containingFile: RapFile? = slimParent?.containingFile,
    override var slimName: String? = null,
): ParamMutableStatement(slimParent, containingFile), RapExternalClass

class ParamMutableClass(
    slimParent: RapElement? = null,
    containingFile: RapFile? = slimParent?.containingFile,
    className: String? = null,
    override var slimSuperClass: String? = null,
    override var slimCommands: MutableList<RapStatement> = mutableListOf(),
    var shouldLocateSuperClass: Boolean = false,
    var locateSuperClass: ((RapClass) -> RapExternalClass?)?
): ParamMutableExternalClass(slimParent, containingFile, className), RapClass {
    override fun shouldValidateSuper(): Boolean = shouldLocateSuperClass
    override fun locateSuperClass(): RapExternalClass? = locateSuperClass?.let { it(this) }
}

class ParamMutableEnum(
    slimParent: RapElement? = null,
    containingFile: RapFile? = slimParent?.containingFile,
    override var enumValues: MutableMap<String, Int>?,
): ParamMutableStatement(slimParent, containingFile), RapEnum
