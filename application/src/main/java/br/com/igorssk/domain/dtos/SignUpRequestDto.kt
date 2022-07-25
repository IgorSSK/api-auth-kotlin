package br.com.igorssk.domain.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestDto(
    val email: String,
    val password: String,
    val phoneNumber: String,
    val name: String
)