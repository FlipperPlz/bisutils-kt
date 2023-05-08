package com.flipperplz.bisutils.param.slim.util

import com.flipperplz.bisutils.param.slim.*
import com.flipperplz.bisutils.param.slim.impl.command.ParamSlimVariableStatementImpl
import com.flipperplz.bisutils.param.slim.util.annotations.PMapTemplatedArray
import com.flipperplz.bisutils.param.slim.util.annotations.PMappedCommand
import com.flipperplz.bisutils.param.slim.util.annotations.PMappedVariableValue
import kotlin.reflect.KCallable
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

abstract class ParamMappableClass(
    override var className: String,
    override var superClass: String?
): ParamSlimClass {
    private val annotatedProperties: Collection<KCallable<*>>
    init {
        annotatedProperties = this::class.members
            .filterIsInstance<KCallable<ParamSlim>>()
            .filter { it.annotations.any { ann -> ann is PMappedVariableValue || ann is PMappedCommand } }
        remap()
    }

    override lateinit var slimCommands: List<ParamSlimCommand>

    fun remap() {
        val commands = mutableListOf<ParamSlimCommand>()
        for (property in annotatedProperties) {
            val returnType = property.returnType
            when {
                returnType.isSubtypeOf(typeOf<ParamSlimCommand>()) -> (property.call() as ParamSlimCommand?)
                returnType.isSubtypeOf(typeOf<ParamSlimLiteral<*>>()) -> {
                    if(!property.hasAnnotation<PMappedVariableValue>() && !property.hasAnnotation<PMappedCommand>()) continue
                    val annotation = property.findAnnotation<PMappedVariableValue>()
                    val name = annotation?.name ?: property.name
                    val operator = annotation?.operation ?: ParamOperatorTypes.ASSIGN
                    val called = property.call() as ParamSlimLiteral<*>?
                    //TODO(Ryann): This should really be delegated out; this is way too much logic in one class
                    property.findAnnotation<PMapTemplatedArray>()?.let {
                        val template = it.template
                        (called as? ParamSlimArray)?.let { array ->
                            var current = 0
                            val length = template.count()
                            for (arrayElement in array.value) {
                                if(arrayElement.type != template[current])
                                    throw Exception("This array does not conform to the template")
                                if(current == length) current = 0
                                else current++
                            }
                        }
                    }
                    called?.let { literal ->
                        commands.add(ParamSlimVariableStatementImpl(this, operator, name, literal))
                    }
                }
            }
        }
        slimCommands = commands;
    }

    override fun toString(): String = super.toString()
}