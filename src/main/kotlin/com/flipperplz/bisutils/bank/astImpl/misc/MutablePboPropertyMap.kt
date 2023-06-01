package com.flipperplz.bisutils.bank.astImpl.misc

import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.bank.ast.misc.MutablePboElement
import com.flipperplz.bisutils.bank.ast.misc.MutablePboProperty
import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.utils.BisFamily
import com.flipperplz.bisutils.utils.BisFlushable
import com.flipperplz.bisutils.utils.BisMutableFamily

class MutablePboPropertyMap(
    override var parent: BisFamily?,
    override val lowestBranch: PboFile?,
    override var properties: MutableList<MutablePboProperty>
) : PboPropertyMap(parent, lowestBranch, properties), BisFlushable {

    override fun propertyForName(name: String): MutablePboProperty? =
        properties.firstOrNull {it.name.equals(name, true)}

    override val children: MutableList<MutablePboProperty> = properties
    override fun flush() {
        properties.clear()
    }
}