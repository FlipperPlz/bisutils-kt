package com.flipperplz.bisutils.utils

import com.flipperplz.bisutils.options.BisOptions

interface IValidatable {
    fun isValid(options: BisOptions?): Boolean
}