package com.flipperplz.bisutils.param.rap.node

import com.flipperplz.bisutils.param.slim.node.ParamSlim

interface RapElement : ParamSlim {
    val parentElement: RapElement?
}