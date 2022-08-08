package br.com.igorssk.inbound.lambda

import br.com.igorssk.domain.dtos.AuthenticateRequestDto
import br.com.igorssk.ports.inbound.AuthenticatePort
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import io.micronaut.function.aws.MicronautRequestHandler
import jakarta.inject.Inject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AuthenticateHandler : MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>() {
    @Inject
    lateinit var authenticatePort: AuthenticatePort

    override fun execute(input: APIGatewayProxyRequestEvent?): APIGatewayProxyResponseEvent {
        val response = APIGatewayProxyResponseEvent()
        try {
            if(input == null || input.body == null) {
                response.statusCode = 400
                return response
            }

            val authenticateRequestDto = Json.decodeFromString<AuthenticateRequestDto>(input.body)
            val authenticateResponse = authenticatePort.handle(authenticateRequestDto.username, authenticateRequestDto.password)

            response.body = Json.encodeToString(authenticateResponse)
            response.statusCode = 200
            return response
        } catch (e: Exception) {
            response.body = e.message
            response.statusCode = 500
            return response
        }
    }

}