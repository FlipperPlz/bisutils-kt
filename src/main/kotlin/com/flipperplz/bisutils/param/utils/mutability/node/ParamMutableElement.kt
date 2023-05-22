package com.flipperplz.bisutils.param.utils.mutability.node

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.node.ParamElement

interface ParamMutableElement: ParamElement {

    override var containingParamFile: ParamFile?
    override var slimParent: ParamElement?

}