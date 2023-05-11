package com.flipperplz.bisutils.param.node

import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapElement {
    val slimParent: RapElement?

    val slimType: ParamElementTypes

    val slimCurrentlyValid: Boolean
        get() = true

    val slimBinarizable: Boolean
        get() = true

    fun toParam(): String
}