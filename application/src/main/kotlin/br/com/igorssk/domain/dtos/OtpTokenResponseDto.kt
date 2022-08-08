package br.com.igorssk.domain.dtos

import kotlinx.serialization.Serializable

enum class DestinatioType {
    SMS,
    EMAIL
}

@Serializable
data class OtpTokenResponseDto(
    val destination: String,
    val destinationType: DestinatioType
)