package com.flipperplz.bisutils.param.utils.delegation

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

class RapTemplatedArray(
    array: RapArray,
    vararg val template: ParamLiteralTypes,
    val allowEmpty: Boolean = true
) : RapArray by array {
    override val slimCurrentlyValid: Boolean
        get() {
            if (!super.slimCurrentlyValid) return false
            var current = 0
            val length = template.count()
            if (slimValue.isNullOrEmpty()) return allowEmpty
            slimValue.forEach { element ->
                if (element.slimLiteralType != template[current]) return false
                if (current == length) current = 0
                else current++
            }
            return false
        }
}
