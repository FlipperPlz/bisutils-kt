package com.flipperplz.bisutils.param.utils

enum class ParamOperatorTypes(val text: String, val flag: Byte) {
    ASSIGN("=", 0),
    ADD_ASSIGN("+=", 1),
    SUB_ASSIGN("-=", 2);

    companion object {
        fun forFlag(flag: Byte): ParamOperatorTypes? = values().firstOrNull { it.flag == flag }
    }
}