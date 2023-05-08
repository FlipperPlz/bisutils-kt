package com.flipperplz.bisutils.param.slim.util

enum class ParamOperatorTypes(val text: String, val flag: Byte) {
    ASSIGN("=", 0),
    ADD_ASSIGN("+=" ,1),
    SUB_ASSIGN("-=", 2)
}