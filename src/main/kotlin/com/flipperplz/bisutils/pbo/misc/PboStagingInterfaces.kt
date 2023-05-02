package com.flipperplz.bisutils.pbo.misc

interface StagedPboEntry {
    var metadataOffset: Long
}

interface StagedPboDataEntry : StagedPboEntry {
    var dataOffset: Long?
}