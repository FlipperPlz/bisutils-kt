package com.flipperplz.bisutils.param.utils.delegation

import com.flipperplz.bisutils.param.ast.literal.ParamArray
import com.flipperplz.bisutils.param.ast.node.ParamLiteralBase
import kotlin.reflect.KClass
import kotlin.reflect.full.starProjectedType

class ParamTemplatedArray(
    array: ParamArray,
    vararg val template: KClass<ParamLiteralBase>,
    val allowEmpty: Boolean = true
) : ParamArray by array {
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
