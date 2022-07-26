package br.com.igorssk.ports.inbound

import br.com.igorssk.domain.dtos.SignUpRequestDto
import br.com.igorssk.domain.dtos.SignUpResponseDto

interface SignUpPort {
    fun handle(request: SignUpRequestDto): SignUpResponseDto
}