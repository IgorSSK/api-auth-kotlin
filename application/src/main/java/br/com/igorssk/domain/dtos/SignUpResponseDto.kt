package br.com.igorssk.domain.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponseDto (
    val email: String,
)