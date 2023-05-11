package com.flipperplz.bisutils.param.node

import com.flipperplz.bisutils.param.utils.ParamElementTypes

/**
 * Represents a root element in the param language, contrary to the name this is the root of both implementations
 * of the language used in this library.
 *
 * @property slimParent Represents the element before this one in the tree
 * @property slimCurrentlyValid A 'function' used to validate the contents of the element. In some implementations this
 * just verifies that all elements are not null, this is also here to provide a hook for linting, although currently
 * there is no difference between errors and warnings, if this returns false in theory [toParam] should not be able
 * to evaluate or may evaluate to unsafe param.
 * @property slimBinarizable Returns true if this element is supported by the current barbarization implementation. Take
 * a look at [com.flipperplz.bisutils.param.node.impl.RapFileImpl]
 * @property slimType Global element type used for the quick identification of RapElements. See [ParamElementTypes] to
 * see how the enum is laid out
 */
interface RapElement {
    val slimParent: RapElement?
    fun getRapElementType(): ParamElementTypes
    fun isCurrentlyValid(): Boolean
    fun isBinarizable(): Boolean
    fun toParam(): String
}