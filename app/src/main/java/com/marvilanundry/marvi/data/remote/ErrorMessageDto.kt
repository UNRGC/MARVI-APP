package com.marvilanundry.marvi.data.remote

import com.marvilanundry.marvi.domain.model.ErrorMessage
import kotlinx.serialization.Serializable

@Serializable
class ErrorMessageDto(
    val message: String
)

fun ErrorMessageDto.toErrorMessage(): ErrorMessage = ErrorMessage(
    message = message
)