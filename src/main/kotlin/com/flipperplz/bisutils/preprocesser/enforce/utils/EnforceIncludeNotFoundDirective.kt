package com.flipperplz.bisutils.preprocesser.enforce.utils

data class EnforceIncludeNotFoundDirective(val path: String): Exception("[Enforce] Include path <$path> could not be resolved! ")