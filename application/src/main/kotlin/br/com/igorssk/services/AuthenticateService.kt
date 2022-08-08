package br.com.igorssk.services

import br.com.igorssk.domain.entities.UserToken
import br.com.igorssk.ports.inbound.AuthenticatePort
import br.com.igorssk.ports.outbound.UserAccessManagerPort
import jakarta.inject.Inject

class AuthenticateService : AuthenticatePort {
    @Inject
    private lateinit var userAccessManager: UserAccessManagerPort

    override fun handle(username: String, password: String): UserToken {
        try {
            return userAccessManager.authenticate(username, password)
        } catch (e: Exception) {
            throw e
        }
    }
}