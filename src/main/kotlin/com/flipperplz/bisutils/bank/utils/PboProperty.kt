package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.PboVersionEntry

data class PboProperty(
    val propertyName: String,
    val propertyValue: String
) {
    lateinit var owner: PboVersionEntry
    fun calculateLength(): Long = propertyName.length.toLong() + propertyValue.length.toLong() + 2
}
