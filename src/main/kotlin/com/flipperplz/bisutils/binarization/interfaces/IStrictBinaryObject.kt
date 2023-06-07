package com.flipperplz.bisutils.binarization.interfaces

import com.flipperplz.bisutils.binarization.options.IBinarizationOptions

interface IStrictBinaryObject<in B : IBinarizationOptions, in D : IBinarizationOptions> : IStrictBinarizable<B>, IDebinarizable<D>