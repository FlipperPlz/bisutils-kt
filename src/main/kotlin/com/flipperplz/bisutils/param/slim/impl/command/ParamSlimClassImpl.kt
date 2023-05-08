package com.flipperplz.bisutils.param.slim.impl.command

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.ParamSlimClass
import com.flipperplz.bisutils.param.slim.ParamSlimCommand


data class ParamSlimClassImpl(
    var parentElement: ParamSlim?,
    override var className: String,
    override var superClass: String?,
    override val commands: MutableList<ParamSlimCommand>
): ParamSlimClass