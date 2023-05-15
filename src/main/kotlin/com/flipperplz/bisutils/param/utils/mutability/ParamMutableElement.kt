package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.RapFile
import com.flipperplz.bisutils.param.node.RapElement

abstract class ParamMutableElement(
    override var slimParent: RapElement? = null,
) : RapElement {
    override var containingFile: RapFile? = slimParent?.containingFile

}