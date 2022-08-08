package br.com.igorssk.domain.entities

data class UserToken(
    val accessToken: String,
    val expiresIn: Int,
    val tokenType: String,
    val refreshToken: String
)