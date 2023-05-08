package com.flipperplz.bisutils.param.slim.util

import com.flipperplz.bisutils.param.slim.*
import com.flipperplz.bisutils.param.slim.impl.command.ParamSlimVariableStatementImpl
import kotlin.reflect.KCallable
import kotlin.reflect.full.findAnnotation
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
    @Target(AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
    annotation class PMappedCommand()

    @Target(AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
    annotation class PMappedVariableValue(val name: String, val operation: ParamOperator = ParamOperator.ASSIGN)

    override lateinit var slimCommands: List<ParamSlimCommand>

    fun remap() {
        val commands = mutableListOf<ParamSlimCommand>()
        for (property in annotatedProperties) {
            val returnType = property.returnType
            when {
                returnType.isSubtypeOf(typeOf<ParamSlimCommand>()) -> (property.call() as ParamSlimCommand?)?.let { commands.add(it) }
                returnType.isSubtypeOf(typeOf<ParamSlimLiteral<*>>()) -> {
                    val annotation =
                    property.findAnnotation<PMappedVariableValue>()?.let {
                        (property.call() as ParamSlimLiteral<*>?)?.let { literal ->
                            commands.add(ParamSlimVariableStatementImpl(this, it.operation, it.name, literal))
                        }
                    }
                }
            }
        }
        slimCommands = commands;
    }

    override fun toString(): String {
        return super.toString()
    }
}