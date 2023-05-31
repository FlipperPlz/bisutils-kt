package com.flipperplz.bisutils.preprocesser.enforce.ast.directive

import com.flipperplz.bisutils.preprocesser.enforce.ast.EnforceDirective

interface EnforceIncludeDirective : EnforceDirective {
    val path: String

    override fun toEnforce(): String = "#include \"$path\""
}