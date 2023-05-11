package com.flipperplz.bisutils.param.node

/**
 * Contract for directives. Contrary to the name, these are not binarizable and are therefore not supported in the rap
 * file format.
 */
interface RapDirective : RapStatement {
    override fun isBinarizable(): Boolean = false
}