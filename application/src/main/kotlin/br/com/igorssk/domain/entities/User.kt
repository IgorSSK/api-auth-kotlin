package br.com.igorssk.domain.entities

data class User(
    val id: String,
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: String
)