package com.flipperplz.bisutils.param.node

import com.flipperplz.bisutils.param.node.RapElement

interface RapProcessable : RapElement {
    fun processSlim(): List<RapElement>?
}