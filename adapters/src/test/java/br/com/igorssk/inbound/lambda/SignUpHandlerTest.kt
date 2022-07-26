package br.com.igorssk.br.com.igorssk
import br.com.igorssk.domain.dtos.SignUpRequestDto
import br.com.igorssk.inbound.lambda.SignUpHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.logging.Logger

class SignUpHandlerTest {

    @Test
    fun testHandler() {
        val handler = SignUpHandler()
        val request = APIGatewayProxyRequestEvent()
        request.httpMethod = "POST"
        request.path = "/auth/user/signup"

        val dto = SignUpRequestDto("igor-lock@hotmail.com", "Teste@100", "+5534992760462", "Igor S Silva")
        request.body = Json.encodeToString(dto)

        val response = handler.execute(request)
        Logger.getLogger(this::class.java.name).info(response.toString())
        assertEquals(200, response.statusCode.toInt())
        assertEquals("{\"message\":\"Hello World\"}", response.body)
        handler.close()
    }
}
