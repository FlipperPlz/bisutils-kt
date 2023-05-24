package com.flipperplz.bisutils

import com.flipperplz.bisutils.param.lexer.ParamLexer
import com.flipperplz.bisutils.param.utils.extensions.parseParamFile
import com.flipperplz.bisutils.parsing.BisLexer

fun main() {

    val lexer = ParamLexer(
        """
class CfgPatches 
{
    class TestMod
    {
        requiredAddons[]=
        {
            "DZ_Data", 
        };
    };
};

class CfgMods   
{
    class TestMod
    {
        type = "mod";
        inputs = "mods\testmod\inputs\my_new_inputs.xml";
        dependencies[]={"Game"}; 

        class defs
        {
            class imageSets
            {
                files[]={"mods/testmod/gui/imagesets/mod1.imageset", "mods/testmod/gui/imagesets/mod2.imageset" };
            };

            class widgetStyles
            {
                files[]={"mods/testmod/gui/looknfeel/mod1.styles", "mods/testmod/gui/looknfeel/mod2.styles"};
            };
            class engineScriptModule
            {
                value="";
                files[]={"mods/testmod/scripts/1_Core"}; 
            };
        };
    };
};
    """.trimIndent()
    )

    parseParamFile(lexer, "config", null)
    println()
}

