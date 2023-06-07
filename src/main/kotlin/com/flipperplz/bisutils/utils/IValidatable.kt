package com.flipperplz.bisutils.utils

import com.flipperplz.bisutils.options.IOptions

interface IValidatable {
    fun isValid(options: IOptions?): Boolean
}