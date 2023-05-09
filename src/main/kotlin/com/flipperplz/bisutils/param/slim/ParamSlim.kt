package com.flipperplz.bisutils.param.slim

import com.flipperplz.bisutils.param.slim.structure.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.structure.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.structure.literals.ParamSlimArray
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes
import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes

interface ParamSlim {
    fun toEnforce(): String = "//Unknown"
    fun currentlyValid(): Boolean = true
}









