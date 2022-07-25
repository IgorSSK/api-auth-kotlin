package br.com.igorssk.ports.outbound

import br.com.igorssk.domain.dtos.SignUpResponseDto
import br.com.igorssk.domain.entities.User

interface UserAccessManagerPort {
    fun signUp(user: User): SignUpResponseDto
    fun verifyUser()
    fun resendVerificationToken()
    fun authenticate()
}