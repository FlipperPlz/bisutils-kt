package com.flipperplz.bisutils.collections

class BisMutableUpcastedList<T, R>(
    private val originalList: MutableList<T>,
    private val upcast: (T) -> R,
    private val downcast: (R) -> T
) : AbstractMutableList<R>() {
    override val size: Int get() = originalList.size
    override fun add(index: Int, element: R) = originalList.add(index, downcast(element))
    override fun removeAt(index: Int): R = originalList.removeAt(index).let(upcast)
    override fun set(index: Int, element: R): R = originalList.set(index, downcast(element)).let(upcast)
    override fun get(index: Int): R = upcast(originalList[index])
}
