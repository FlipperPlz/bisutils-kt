package com.flipperplz.bisutils.utils

interface BisFamily {
    val parent: BisFamily?
    val children: List<Any>?

    val lowestBranch: BisFamily?
}