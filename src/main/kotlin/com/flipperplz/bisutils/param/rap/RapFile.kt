package com.flipperplz.bisutils.param.rap

import com.flipperplz.bisutils.param.rap.node.RapElement
import com.flipperplz.bisutils.param.rap.node.RapStatementHolder
import com.flipperplz.bisutils.param.slim.ParamSlimFile

interface RapFile: ParamSlimFile, RapStatementHolder {
    override val parentElement: RapElement?
        get() = null
    override val binaryOffset: Int
        get() = 0
}