package com.flipperplz.bisutils.preprocesser.enforce.astImpl.directive

import com.flipperplz.bisutils.preprocesser.enforce.EnforcePreprocessor
import com.flipperplz.bisutils.preprocesser.enforce.ast.directive.EnforceIncludeDirective

class EnforceIncludeDirectiveImpl(override val processor: EnforcePreprocessor, override val path: String) : EnforceIncludeDirective