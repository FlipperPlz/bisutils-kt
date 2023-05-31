package com.flipperplz.bisutils.preprocesser.boost.ast.element

import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor

interface BoostElement {
    val processor: BoostPreprocessor
    fun toBoost(): String
}