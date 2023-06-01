package com.flipperplz.bisutils.collections

class BisUpcastedList<T, R>(private val originalList: List<T>, private val castFunction: (T) -> R) : AbstractList<R>() {
    override val size: Int
        get() = originalList.size

    override operator fun get(index: Int): R {
        return castFunction(originalList[index])
    }
}