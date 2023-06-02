package com.flipperplz.bisutils.binarization.interfaces

import com.flipperplz.bisutils.binarization.options.BinarizationOptions
import com.flipperplz.bisutils.binarization.options.DebinarizationOptions

interface IStrictBinaryObject<in B : BinarizationOptions, in D : DebinarizationOptions> : IStrictBinarizable<B>, IDebinarizable<D>