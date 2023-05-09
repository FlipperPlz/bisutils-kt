package com.flipperplz.bisutils.param.slim

import com.flipperplz.bisutils.param.slim.util.ParamElementTypes

interface ParamSlim {
    val slimCurrentlyValid: Boolean
        get() = true
    val slimType: ParamElementTypes
    fun toEnforce(): String = "//Unknown"
}



