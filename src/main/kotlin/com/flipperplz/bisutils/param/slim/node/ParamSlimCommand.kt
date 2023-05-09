package com.flipperplz.bisutils.param.slim.node

import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes
import com.flipperplz.bisutils.param.slim.util.ParamElementTypes

interface ParamSlimCommand : ParamSlim {
    val slimCommandType: ParamCommandTypes

    override val slimType: ParamElementTypes
        get() = slimCommandType.type
}
