package com.flipperplz.bisutils.param.slim.commands.impl

import com.flipperplz.bisutils.param.slim.node.ParamSlim
import com.flipperplz.bisutils.param.slim.node.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.commands.ParamSlimClass


data class ParamSlimClassImpl(
    var parentElement: ParamSlim?,
    override var slimSuperClass: String?,
    override var slimCommands: List<ParamSlimCommand>,
    override var slimClassName: String,
): ParamSlimClass