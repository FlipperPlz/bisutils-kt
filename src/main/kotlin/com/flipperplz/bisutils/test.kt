package com.flipperplz.bisutils

import com.flipperplz.bisutils.param.utils.extensions.readParam
import com.flipperplz.bisutils.utils.BisLexer

fun main() {

    val v = BisLexer(
        """
        class CfgPatches {
            class MyClass : Test {
                text="you know daddy loves your right?";
                type=1;
            };
            class MyClass : TestResponse {
                text=yea I know papa 0.0;
                type=3.141592;
            };
        };
    """.trimIndent()
    )
    val clazz = readParam("config", v, null)
    println()
}

