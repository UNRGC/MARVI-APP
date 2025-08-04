package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.Email
import kotlinx.serialization.Serializable

@Serializable
data class EmailDto (
    val correo: String
)

fun Email.toEmailDto(): EmailDto = EmailDto(
    correo = correo,
)