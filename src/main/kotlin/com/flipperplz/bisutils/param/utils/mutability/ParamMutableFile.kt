package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.node.ParamElement
import com.flipperplz.bisutils.param.node.ParamStatement

class ParamMutableFile(
    override var fileName: String,
    override var slimCommands: MutableList<ParamStatement>,
): ParamMutableElement(null), ParamFile {
    override var slimParent: ParamElement? = null
    override var containingParamFile: ParamFile? = null
}