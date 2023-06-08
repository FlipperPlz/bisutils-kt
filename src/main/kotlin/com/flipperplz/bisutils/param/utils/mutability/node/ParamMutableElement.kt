package com.flipperplz.bisutils.param.utils.mutability.node

import com.flipperplz.bisutils.param.IParamFile
import com.flipperplz.bisutils.param.ast.node.IParamElement

interface ParamMutableElement: IParamElement {

    override var containingParamFile: IParamFile?
    override var slimParent: IParamElement?

}