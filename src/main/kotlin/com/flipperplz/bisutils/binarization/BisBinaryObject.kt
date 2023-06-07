package com.flipperplz.bisutils.binarization

import com.flipperplz.bisutils.binarization.interfaces.IBinaryObject
import com.flipperplz.bisutils.binarization.options.IBinarizationOptions

abstract class BisBinaryObject<in BO : IBinarizationOptions, in DO : IBinarizationOptions> protected constructor() : IBinaryObject<BO, DO>
