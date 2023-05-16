package com.study.network.util

import com.study.network.model.request.message.MessageNarrow
import com.study.network.model.request.message.MessageNarrowOperator
import com.study.network.model.request.message.OperandType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.serializer

internal object MessageNarrowSerializer : KSerializer<MessageNarrow> {
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
