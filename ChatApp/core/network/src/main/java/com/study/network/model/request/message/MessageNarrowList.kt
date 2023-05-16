package com.study.network.model.request.message

import com.study.network.util.MessageNarrowSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
    @Transient val operandType: OperandType
) {
    override fun toString(): String = Json.encodeToString(value = this)
}

enum class OperandType { INT, STRING }

