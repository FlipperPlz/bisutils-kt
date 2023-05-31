package com.flipperplz.bisutils.preprocesser.boost.utils

import com.flipperplz.bisutils.preprocesser.boost.ast.directive.BoostIncludeDirective

data class BoostIncludeNotFoundException(val include: BoostIncludeDirective): Exception("[Boost] Include path <${include.path}> could not be resolved! ")