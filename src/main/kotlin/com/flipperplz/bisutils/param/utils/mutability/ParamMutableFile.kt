package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.RapFile
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapStatement

class ParamMutableFile(
    override var fileName: String,
    override var slimCommands: MutableList<RapStatement>,
): ParamMutableElement(null, null), RapFile {
    override var slimParent: RapElement? = null
    override var containingFile: RapFile? = null
}