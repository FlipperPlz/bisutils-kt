package com.flipperplz.bisutils.bank.ast.entry

import com.flipperplz.bisutils.bank.ast.MutablePboEntry
import com.flipperplz.bisutils.bank.ast.misc.MutablePboProperty
import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.bank.astImpl.misc.MutablePboPropertyMap
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableElement

interface MutablePboVersionEntry : PboVersionEntry, ParamMutableElement, MutableList<PboProperty> {
    override val properties: MutablePboPropertyMap
    override val children: MutableList<MutablePboProperty>
        get() = properties.children

}