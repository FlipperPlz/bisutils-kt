package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.node.ParamElement

abstract class ParamMutableElement(
    override var slimParent: ParamElement? = null,
) : ParamElement {
    override var containingFile: ParamFile? = slimParent?.containingFile

}