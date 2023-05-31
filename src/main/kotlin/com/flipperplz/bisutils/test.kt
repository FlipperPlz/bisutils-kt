package com.flipperplz.bisutils

import com.flipperplz.bisutils.param.ast.node.ParamNamedElement
import com.flipperplz.bisutils.param.bin.write
import com.flipperplz.bisutils.param.lexer.ParamLexer
import com.flipperplz.bisutils.param.utils.extensions.parseParamFile
import com.flipperplz.bisutils.param.utils.extensions.rem
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import java.nio.ByteBuffer

fun main() {

//    val lexer = BisLexer(
//            """
//                /*ryann*/
//                class myclass {
//                //this is a comment remove it
//                }; /*/
//            """.trimIndent()
//    )
//    val preprocessor = BoostPreprocessor()
//    preprocessor.processText(lexer)
//    println(lexer.toString())

    val lexer = ParamLexer(
        """
        #define Ryann TM_
        class CfgPatches {
            class Ryann##Patches {
                dependencies[] = { Ryann, "Ryann", Ryann };
            };
        };
        """.trimIndent()
    )

    val param = parseParamFile(lexer, "config", BoostPreprocessor())
    println(param.toParam())
    println("Param file ${if (param.isBinarizable()) "is" else "is not"} binarizable")
}

