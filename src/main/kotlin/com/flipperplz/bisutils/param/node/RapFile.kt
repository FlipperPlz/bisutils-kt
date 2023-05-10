package com.flipperplz.bisutils.param.node

import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapFile : RapStatementHolder {
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