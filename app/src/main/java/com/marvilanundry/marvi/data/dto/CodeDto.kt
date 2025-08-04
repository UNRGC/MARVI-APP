package com.marvilanundry.marvi.data.dto

import com.marvilanundry.marvi.domain.model.Code
import kotlinx.serialization.Serializable

@Serializable
data class CodeDto (
    val codigo: String
)

fun Code.toCodeDto() = CodeDto(
    codigo = codigo
)