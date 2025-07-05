package com.marvilanundry.marvi.data.remote

import com.marvilanundry.marvi.domain.model.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val message: String
)

fun Message.toMessageDto() = MessageDto(
    message = message
)