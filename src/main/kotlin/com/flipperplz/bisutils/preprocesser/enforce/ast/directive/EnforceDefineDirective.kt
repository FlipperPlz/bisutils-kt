package com.flipperplz.bisutils.preprocesser.enforce.ast.directive

import com.flipperplz.bisutils.preprocesser.enforce.ast.EnforceDirective

interface EnforceDefineDirective : EnforceDirective {
    val defineName: String
}