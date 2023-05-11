package com.flipperplz.bisutils.param.node

import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

/**
 * Represents a param element that has a name or identifier tied to it.
 * @see com.flipperplz.bisutils.param.statement.RapExternalClass
 */
interface RapNamedElement : RapElement {
    val slimName: String?
}