package com.flipperplz.bisutils.binarization.interfaces

import com.flipperplz.bisutils.binarization.options.BinarizationOptions
import com.flipperplz.bisutils.binarization.options.DebinarizationOptions

interface IBinaryObject<in B : BinarizationOptions, in D : DebinarizationOptions> : IBinarizable<B>, IDebinarizable<D>