package br.com.igorssk.ports.inbound

import br.com.igorssk.domain.entities.UserToken

interface AuthenticatePort {
    fun handle(username: String, password: String): UserToken
}