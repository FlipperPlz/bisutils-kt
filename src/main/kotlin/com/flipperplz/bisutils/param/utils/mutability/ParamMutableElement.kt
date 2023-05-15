package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.RapFile
import com.flipperplz.bisutils.param.node.RapElement

abstract class ParamMutableElement(
    override var slimParent: RapElement? = null,
    override var containingFile: RapFile? = slimParent?.containingFile
) : RapElement