package com.flipperplz.bisutils.preprocesser.boost.utils

data class BoostIncludeNotFoundException(val path: String): Exception("[Boost] Include path <$path> could not be resolved! ")