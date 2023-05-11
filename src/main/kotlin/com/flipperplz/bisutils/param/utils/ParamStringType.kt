package com.flipperplz.bisutils.param.utils

enum class ParamStringType(
    val stringify: (String) -> String,
    val destringify: (String) -> String
) {
    QUOTED(
        { "\"${it.replace("\n", "\\\n").replace("\"", "\"\"")}\"" },
        { it.trimStart('\"').trimEnd('\"').replace("\\\n", "\n").replace("\"\"", "\"")}
    ),
    ANGLE(
        { "<${it.replace("\n", "\\\n")}>" },
        { it.trimStart('<').trimEnd('>').replace("\\\n", "\n")}
    ),
    UNQUOTED(
        { it.replace("\n", "").replace(";", "").replace("}", "") },
        { it }
    )
}