package com.flipperplz.bisutils.param.slim.util.delegation

import com.flipperplz.bisutils.param.slim.literals.ParamSlimArray
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

class ParamSlimTemplatedArray(
    array: ParamSlimArray,
    vararg val template: ParamLiteralTypes,
    val allowEmpty: Boolean = true
): ParamSlimArray by array {
    override val slimCurrentlyValid: Boolean
        get() {
            if(!super.slimCurrentlyValid) return false
            var current = 0
            val length = template.count()
            if(slimValue.isNullOrEmpty()) return allowEmpty
            slimValue.forEach { element ->
                if(element.slimLiteralType != template[current]) return false
                if(current == length) current = 0
                else current++
            }
            return false
        }
}
