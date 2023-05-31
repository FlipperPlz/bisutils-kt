package com.flipperplz.bisutils.preprocesser.enforce.utils

import com.flipperplz.bisutils.preprocesser.enforce.ast.directive.EnforceIncludeDirective

data class EnforceIncludeNotFoundException(val include: EnforceIncludeDirective): Exception("[Enforce] Include path <${include.path}> could not be resolved! ")