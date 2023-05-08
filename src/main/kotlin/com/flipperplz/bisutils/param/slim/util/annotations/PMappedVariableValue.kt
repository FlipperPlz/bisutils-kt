package com.flipperplz.bisutils.param.slim.util.annotations

import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes


@Target(AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
annotation class PMappedVariableValue(val name: String = "", val operation: ParamOperatorTypes = ParamOperatorTypes.ASSIGN)
