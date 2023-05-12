package com.flipperplz.bisutils.param.node

interface RapDirective : RapStatement {
    override fun isBinarizable(): Boolean = false

}