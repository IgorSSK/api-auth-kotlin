package br.com.igorssk.inbound.lambda

import br.com.igorssk.domain.dtos.SignUpRequestDto
import br.com.igorssk.domain.dtos.SignUpResponseDto
import br.com.igorssk.services.SignUpService
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import io.micronaut.function.aws.MicronautRequestHandler
import jakarta.inject.Inject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SignUpHandler : MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>() {
    @Inject
    lateinit var signUpService: SignUpService

    override fun execute(input: APIGatewayProxyRequestEvent?): APIGatewayProxyResponseEvent {
        val response = APIGatewayProxyResponseEvent()
        try {
            if (input?.body?.let { true  } != true) {
                response.statusCode = 400
                return response
            }

            val requestDto = Json.decodeFromString<SignUpRequestDto>(input.body)
            val signUpResponse = signUpService.handle(requestDto)

            response.body = Json.encodeToString(signUpResponse)
            response.statusCode = 200
            return response
        } catch (e: Exception) {
            response.body = e.message
            response.statusCode = 500
            return response
        }
    }

}