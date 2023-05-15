package com.flipperplz.bisutils.param.node

/**
 * Rap literal base - A non-specific base used for defining literals in the language. [slimValue] is abstracted to a
 * more specific type in implementations. This interface is here to prevent unnecessary usage of
 * [com.flipperplz.bisutils.param.node.RapLiteral]<*>. Use one of the literal implementations if you know what you're
 * going to be working with ([com.flipperplz.bisutils.param.literal.RapInt],
 * [com.flipperplz.bisutils.param.literal.RapFloat], [com.flipperplz.bisutils.param.literal.RapString],
 * [com.flipperplz.bisutils.param.literal.RapArray])
 */
interface RapLiteralBase : RapElement {
    companion object;
    val slimValue: Any?

    override fun isCurrentlyValid(): Boolean = slimValue != null
}