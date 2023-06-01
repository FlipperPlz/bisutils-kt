package com.flipperplz.bisutils.utils

class DayZScriptContext<T> {
    private val context: MutableMap<DayZScriptModule, MutableList<T>> = mutableMapOf()

    fun getScripts(module: DayZScriptModule, recursive: Boolean = false): List<T> = mutableListOf<T>().apply {
        if(recursive) context.keys.filter { it.id <= module.id }.forEach { module ->
            context[module]?.let { addAll(it) }
        } else context[module]?.let { addAll(it) }
    }

    fun addScript(module: DayZScriptModule, script: T) = addScripts(module, listOf(script))
    fun clearScripts(module: DayZScriptModule) = context[module]?.clear()
    fun removeScript(module: DayZScriptModule, script: T) = context[module]?.remove(script)

    fun addScripts(module: DayZScriptModule, scripts: List<T>) {
        var creating = false
        context.getOrPut(module) {
            creating = true
            scripts.toMutableList()
        }.apply {
            if(!creating) addAll(scripts)
        }
    }
}