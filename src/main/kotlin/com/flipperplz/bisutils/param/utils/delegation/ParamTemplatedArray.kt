//package com.flipperplz.bisutils.param.utils.delegation
//
//import com.flipperplz.bisutils.param.ast.literal.IParamArray
//import kotlin.reflect.KClass
//import kotlin.reflect.full.starProjectedType
//
//class ParamTemplatedArray(
//    array: IParamArray,
//    vararg val template: KClass<IParamLiteralBase>,
//    val allowEmpty: Boolean = true
//) : IParamArray by array {
//    override fun isCurrentlyValid(): Boolean {
//        if (!super.isCurrentlyValid()) return false
//        var current = 0
//        val length = template.count()
//        if (paramValue.isNullOrEmpty()) return allowEmpty
//        paramValue.forEach { element ->
//            if (element::class.starProjectedType != template[current].starProjectedType) return false
//            if (current == length) current = 0
//            else current++
//        }
//        return false
//    }
//}
