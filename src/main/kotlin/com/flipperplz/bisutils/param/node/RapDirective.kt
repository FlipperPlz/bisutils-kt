package com.flipperplz.bisutils.param.node

/**
 * Contract for directives. Contrary to the name, these are not binarizable and are therefore not supported in the rap
 * file format.
 *
 * @property slimBinarizable is overwritten to false as again, directives are purely preprocessor syntax and are not
 * supported in the rap format
 * @property statementId Is -1 as this property is unused in non-binarizable element types
 */
interface RapDirective : RapStatement {
    override val slimBinarizable: Boolean
        get() = false

    override val statementId: Byte
        get() = -1
}