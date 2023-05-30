package com.flipperplz.bisutils.preprocesser.boost.impl

import com.flipperplz.bisutils.preprocesser.boost.directive.BoostMacro

data class BoostMacroImpl(
    override val macroName: String,
    override val macroArguments: List<String>

): BoostMacro
