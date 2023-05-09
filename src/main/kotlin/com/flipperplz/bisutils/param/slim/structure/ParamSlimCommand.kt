package com.flipperplz.bisutils.param.slim.structure

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes

interface ParamSlimCommand : ParamSlim {
    val commandType: ParamCommandTypes
}
