package br.com.igorssk.ports.outbound

import br.com.igorssk.domain.dtos.OtpTokenResponseDto
import br.com.igorssk.domain.dtos.SignUpResponseDto
import br.com.igorssk.domain.entities.User
import br.com.igorssk.domain.entities.UserToken

interface UserAccessManagerPort {
    fun signUp(user: User): SignUpResponseDto
    fun confirmSignUp(username: String, otpToken: String)
    fun sendOtpToken(username: String): OtpTokenResponseDto
    fun authenticate(username: String, password: String): UserToken
}