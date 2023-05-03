package com.flipperplz.bisutils.pbo.misc

import com.flipperplz.bisutils.pbo.BisPboVersionEntry

data class BisPboProperty(
    val propertyName: String,
    val propertyValue: String
) {
    lateinit var owner: BisPboVersionEntry
    fun calculateLength(): Long = propertyName.length.toLong() + propertyValue.length.toLong() + 2
}
