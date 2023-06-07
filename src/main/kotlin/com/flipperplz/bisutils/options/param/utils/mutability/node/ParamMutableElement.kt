package com.flipperplz.bisutils.options.param.utils.mutability.node

import com.flipperplz.bisutils.options.param.ParamFile
import com.flipperplz.bisutils.options.param.ast.node.ParamElement

interface ParamMutableElement: ParamElement {

    override var containingParamFile: ParamFile?
    override var slimParent: ParamElement?

}