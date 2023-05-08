package com.study.network.model.request.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Serializable
enum class MessageNarrowOperator {
    @SerialName("stream")
    STREAM,

    @SerialName("topic")
    TOPIC,

    @SerialName("search")
    SEARCH

}

@Serializable
class MessageNarrowList {
    private val narrows: MutableList<MessageNarrow> = mutableListOf()
    override fun toString(): String = narrows.joinToString(prefix = "[", postfix = "]")

    fun add(messageNarrow: MessageNarrow) {
        narrows.add(messageNarrow)
    }
}

@Serializable(with = MessageNarrowSerializer::class)
class MessageNarrow(
    @SerialName("operator") val operator: MessageNarrowOperator,
    @Contextual
    @SerialName("operand") val operand: Any,
    @kotlinx.serialization.Transient val operandType: OperandType
) {
    override fun toString(): String {
        return Json.encodeToString(value = this)
    }
}

enum class OperandType { INT, STRING }
object MessageNarrowSerializer : KSerializer<MessageNarrow> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("MessageNarrow") {
        element("operator", serialDescriptor<MessageNarrowOperator>())
        element("operand", buildClassSerialDescriptor("Any"))
    }

    @Suppress("UNCHECKED_CAST")
    private val operandSerializers: Map<OperandType, KSerializer<Any>> =
        mapOf(
            OperandType.STRING to serializer<String>(),
            OperandType.INT to serializer<Int>()
        ).mapValues { (_, v) -> v as KSerializer<Any> }

    private fun getOperandSerializer(type: OperandType): KSerializer<Any> = operandSerializers[type]
        ?: throw SerializationException("Serializer for class $type is not registered in PacketSerializer")

    override fun serialize(encoder: Encoder, value: MessageNarrow) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, serializer(), value.operator)
            encodeSerializableElement(
                descriptor,
                1,
                getOperandSerializer(value.operandType),
                value.operand
            )
        }
    }

    @ExperimentalSerializationApi
    override fun deserialize(decoder: Decoder): MessageNarrow = error("Cannot be deserialized")
}

