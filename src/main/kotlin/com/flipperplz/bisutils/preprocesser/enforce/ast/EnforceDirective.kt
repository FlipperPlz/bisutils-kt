package com.flipperplz.bisutils.preprocesser.enforce.ast

import com.flipperplz.bisutils.preprocesser.enforce.EnforcePreprocessor

interface EnforceDirective {
    val processor: EnforcePreprocessor
    fun toEnforce(): String
}