package com.flipperplz.bisutils.param.utils.delegation

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.node.RapLiteralBase
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

class RapTemplatedArray(
    array: RapArray,
    vararg val template: KClass<RapLiteralBase>,
    val allowEmpty: Boolean = true
) : RapArray by array {
    override fun isCurrentlyValid(): Boolean {
        if (!super.isCurrentlyValid()) return false
        var current = 0
        val length = template.count()
        if (slimValue.isNullOrEmpty()) return allowEmpty
        slimValue.forEach { element ->
            if (element::class.starProjectedType != template[current].starProjectedType) return false
            if (current == length) current = 0
            else current++
        }
        return false
    }
}
