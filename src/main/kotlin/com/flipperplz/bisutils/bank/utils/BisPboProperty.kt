package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.BisPboVersionEntry

data class BisPboProperty(
    val propertyName: String,
    val propertyValue: String
) {
    lateinit var owner: BisPboVersionEntry
    fun calculateLength(): Long = propertyName.length.toLong() + propertyValue.length.toLong() + 2
}
