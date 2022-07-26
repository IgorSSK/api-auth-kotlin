package br.com.igorssk.services

import br.com.igorssk.domain.dtos.SignUpRequestDto
import br.com.igorssk.domain.dtos.SignUpResponseDto
import br.com.igorssk.domain.entities.User
import br.com.igorssk.ports.inbound.SignUpPort
import br.com.igorssk.ports.outbound.UserAccessManagerPort
import jakarta.inject.Inject

class SignUpService : SignUpPort {
    @Inject
    private lateinit var userAccessManager: UserAccessManagerPort

    override fun handle(request: SignUpRequestDto): SignUpResponseDto {
        try {
            val user = User("", request.email, request.password, request.name, request.phoneNumber)
            return userAccessManager.signUp(user)
        } catch (e: Exception) {
            throw e
        }
    }
}