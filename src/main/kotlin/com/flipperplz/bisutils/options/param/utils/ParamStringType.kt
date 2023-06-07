package com.flipperplz.bisutils.options.param.utils

enum class ParamStringType(
    val stringify: (String) -> String,
    val destringify: (String) -> String
) {
    QUOTED(
        { "\"${it.replace("\n", "\\\n").replace("\"", "\"\"")}\"" },
        { it.trimStart('\"').trimEnd('\"').replace("\\\n", "\n").replace("\"\"", "\"")}
    ),
    UNQUOTED(
        { it.replace("\n", "").replace(";", "").replace("}", "") },
        { it }
    )
}