package br.com.igorssk.ports.outbound

import br.com.igorssk.domain.dtos.SignUpResponseDto
import br.com.igorssk.domain.entities.User

interface UserAccessManagerPort {
    fun signUp(user: User): SignUpResponseDto
    fun verifyUser(username: String, token: String)
    fun resendVerificationToken(username: String)
    fun authenticate(username: String, password: String)
}