package com.flipperplz.bisutils.binarization.interfaces

import com.flipperplz.bisutils.binarization.options.IBinarizationOptions

interface IBinaryObject<in B : IBinarizationOptions, in D : IBinarizationOptions> : IBinarizable<B>, IDebinarizable<D>