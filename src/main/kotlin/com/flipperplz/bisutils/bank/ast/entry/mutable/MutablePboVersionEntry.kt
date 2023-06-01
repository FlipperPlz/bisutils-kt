package com.flipperplz.bisutils.bank.ast.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.PboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.mutable.MutablePboProperty
import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.bank.astImpl.misc.mutable.MutablePboPropertyMap
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableElement

interface MutablePboVersionEntry : PboVersionEntry, ParamMutableElement {
    override val propertiesMap: MutablePboPropertyMap
    override val children: MutableList<MutablePboProperty>
        get() = propertiesMap.children

}