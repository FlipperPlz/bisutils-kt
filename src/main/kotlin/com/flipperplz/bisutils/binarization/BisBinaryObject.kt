package com.flipperplz.bisutils.binarization

import com.flipperplz.bisutils.binarization.interfaces.IBinaryObject
import com.flipperplz.bisutils.binarization.options.BinarizationOptions
import com.flipperplz.bisutils.binarization.options.DebinarizationOptions

abstract class BisBinaryObject<in BO : BinarizationOptions, in DO : DebinarizationOptions> protected constructor() : IBinaryObject<BO, DO>
