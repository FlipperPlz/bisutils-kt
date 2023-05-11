package com.flipperplz.bisutils.param.node

/**
 * @see RapLiteralBase
 *
 * @param T Strict type to enforce to [slimValue]
 */
interface RapLiteral<T> : RapLiteralBase {
    override val slimValue: T?
}