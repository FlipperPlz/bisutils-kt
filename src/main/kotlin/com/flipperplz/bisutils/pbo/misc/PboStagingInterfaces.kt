package com.flipperplz.bisutils.pbo.misc

import java.io.RandomAccessFile

interface StagedPboEntry {
    val stageBuffer: RandomAccessFile
    var metadataOffset: Long
}

interface StagedPboDataEntry : StagedPboEntry {
    var dataOffset: Long?
}