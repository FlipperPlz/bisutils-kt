package com.flipperplz.bisutils.pbo.misc

import java.io.RandomAccessFile

interface StagedPboEntry {
    val stageBuffer: RandomAccessFile
    var metadataOffset: Long
    var synced: Boolean

    fun onEditsMade() { synced = false }
}

interface StagedPboDataEntry : StagedPboEntry {
    var dataOffset: Long?
}