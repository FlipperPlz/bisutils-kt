package com.flipperplz.bisutils.param.slim

import com.flipperplz.bisutils.param.slim.node.ParamSlimCommandHolder

interface ParamSlimFile : ParamSlimCommandHolder {
    val fileName: String
    val slimEnum: Map<String, Int>

    override fun toEnforce(): String = super<ParamSlimCommandHolder>.toEnforce() + slimEnum.asSequence().joinToString(
        prefix = "enum {\n",
        postfix = "\n};",
        separator = ",\n"
    ) { "${it.key} = ${it.value}" }
}