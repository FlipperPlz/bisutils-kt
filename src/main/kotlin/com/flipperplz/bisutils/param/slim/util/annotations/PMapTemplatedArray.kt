package com.flipperplz.bisutils.param.slim.util.annotations

import com.flipperplz.bisutils.param.slim.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes
import java.text.Annotation
import kotlin.reflect.KClass

annotation class PMapTemplatedArray(
    vararg val template: ParamLiteralTypes
)
