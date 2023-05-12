package com.flipperplz.bisutils.param.directive

import com.flipperplz.bisutils.param.node.RapDirective
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.node.RapProcessable
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapUndefine : RapDirective, RapNamedElement, RapProcessable {
    override fun getRapElementType(): ParamElementTypes =
        ParamElementTypes.CP_UNDEF

    override fun isCurrentlyValid(): Boolean =
        !slimName.isNullOrBlank()

    override fun toParam(): String =
        "#undef $slimName\n"
}