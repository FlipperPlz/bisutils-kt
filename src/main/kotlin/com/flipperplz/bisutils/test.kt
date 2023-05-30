package com.flipperplz.bisutils

import com.flipperplz.bisutils.param.ast.node.ParamNamedElement
import com.flipperplz.bisutils.param.lexer.ParamLexer
import com.flipperplz.bisutils.param.utils.extensions.parseParamFile
import com.flipperplz.bisutils.param.utils.extensions.rem
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor

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
        #define Ryann 3.14159
        
        myarray[]={ Ryann, Ryann, Ryann };
        """.trimIndent()
    )

    val param = parseParamFile(lexer, "config", BoostPreprocessor())
    println(param.toParam())
}

