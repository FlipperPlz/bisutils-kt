package com.flipperplz.bisutils.param.node

import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

/**
 * Rap literal base - A non-specific base used for defining literals in the language. [slimValue] is abstracted to a
 * more specific type in implementations. This interface is here to prevent unnecessary usage of
 * [com.flipperplz.bisutils.param.node.RapLiteral]<*>. Use one of the literal implementations if you know what you're
 * going to be working with ([com.flipperplz.bisutils.param.literal.RapInt],
 * [com.flipperplz.bisutils.param.literal.RapFloat], [com.flipperplz.bisutils.param.literal.RapString],
 * [com.flipperplz.bisutils.param.literal.RapArray])
 */
interface RapLiteralBase : RapElement {
    val slimValue: Any?
    val literalId: Byte?

    val slimLiteralType: ParamLiteralTypes
    override val slimType: ParamElementTypes
        get() = slimLiteralType.type

    override val slimCurrentlyValid: Boolean
        get() = slimValue != null
}