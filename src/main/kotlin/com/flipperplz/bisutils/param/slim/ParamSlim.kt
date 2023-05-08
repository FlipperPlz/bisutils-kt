package com.flipperplz.bisutils.param.slim

sealed interface ParamSlim {
    fun toEnforce(): String
}
interface ParamSlimCommand : ParamSlim
interface ParamSlimLiteral<T> : ParamSlim { var value: T }
interface ParamSlimNumericLiteral<T: Number> : ParamSlimLiteral<T>

enum class ParamOperator(val text: String) {
    ASSIGN("="),
    ADD_ASSIGN("+="),
    SUB_ASSIGN("-=")
}

interface ParamSlimString : ParamSlimLiteral<String> {
    override fun toEnforce(): String = "\"${value.replace("\"", "\"\"")}\""
}

interface ParamSlimFloat : ParamSlimNumericLiteral<Float> {
    override fun toEnforce(): String = value.toString()
}

interface ParamSlimInt : ParamSlimNumericLiteral<Int> {
    override fun toEnforce(): String = value.toString()
}

interface ParamSlimArray : ParamSlimLiteral<MutableList<ParamSlimLiteral<*>>> {
    override fun toEnforce(): String = value.joinToString(", ", prefix = "{", postfix = "}") { it.toEnforce() }
}

interface ParamSlimExternalClass : ParamSlimCommand {
    var className: String
    override fun toEnforce(): String = "class $className;"
}

interface ParamSlimClass : ParamSlimExternalClass {
    var superClass: String?
    val slimCommands: MutableList<ParamSlimCommand>

    override fun toEnforce(): String {
        val builder = StringBuilder("class ").append(className)
        if(superClass != null) builder.append(" : ").append(superClass)
        builder.append(" { \n")
        builder.append(slimCommands.joinToString(separator = "\n", postfix = "\n"){ it.toEnforce() })
        return builder.append("};").toString()
    }
}

interface ParamSlimDeleteStatement : ParamSlimCommand {
    var target: String

    override fun toEnforce(): String = "delete $target;"
}

interface ParamSlimVariableStatement : ParamSlimCommand {
    var name: String;
    var value: ParamSlimLiteral<*>
    var operator: ParamOperator
    override fun toEnforce(): String {
        val builder = StringBuilder(name)
        if (value is ParamSlimArray) builder.append("[]")
        builder.append(' ').append(operator.text).append(' ')
        return builder.append(value.toEnforce()).append(';').toString()
    }
}



