package com.flipperplz.bisutils.preprocesser.boost.utils

enum class BoostDirectiveType(val debugName: String, val text: String) {
    B_INCLUDE("boost::include", "include"),
    B_DEFINE("boost::define", "define"),
    B_UNDEFINE("boost::undefine", "undef"),
    B_IF("boost::if", "if"),
    B_IFDEF("boost::if.defined", "ifdef"),
    B_IFNDEF("boost::if.not-defined", "ifndef"),
    B_ELSE("boost::else", "else"),
    B_ENDIF("boost::endif", "endif");
}