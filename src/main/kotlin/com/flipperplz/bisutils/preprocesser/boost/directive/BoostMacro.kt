package com.flipperplz.bisutils.preprocesser.boost.directive

interface BoostMacro {
    val macroName: String
    val macroArguments: List<String>
}