package com.flipperplz.bisutils.param.slim.node

import com.flipperplz.bisutils.param.slim.util.ParamElementTypes

/**
 * The ParamSlim interface is the base interface for all Param language elements.
 * Everything in super interfaces should be prefixed with slim as this is a base "slim" contract
 * for all the elements in the Param language. Implementations of said contracts can then be made according
 * to the use case. The normal editable implementation for each element can be found in the impl package of the
 * elements root package.
 * @property slimCurrentlyValid  specifies if the element is currently printable (e.g. all requirements are set) , and
 * @property slimType which indicates the type of the element as a ParamElementTypes enum value.
 * This interface also includes the toEnforce() function that
 */
interface ParamSlim {

    val slimType: ParamElementTypes

    /**
     * @returns true if the current instance is in a printable state.
     * **can be used for syntax checking**
     */
    val slimCurrentlyValid: Boolean
        get() = true

    /**
     * @returns the element in the param language's default syntax.
     * **syntax rules can be enforced through slimCurrentlyValid**
     */
    fun toEnforce(): String = "//Unknown"
}



