package com.flipperplz.bisutils.param.slim

import com.flipperplz.bisutils.param.slim.node.ParamSlimCommandHolder
import com.flipperplz.bisutils.param.slim.util.ParamElementTypes

interface ParamSlimFile : ParamSlimCommandHolder {
    val fileName: String
    val slimEnum: Map<String, Int>

    override val slimType: ParamElementTypes
        get() = ParamElementTypes.FILE

    override fun toEnforce(): String = super.toEnforce() + slimEnum.asSequence().joinToString(
        prefix = "enum {\n",
        postfix = "\n};",
        separator = ",\n"
    ) { "${it.key} = ${it.value}" }
}