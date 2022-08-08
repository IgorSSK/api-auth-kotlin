package br.com.igorssk.inbound.lambda.outbound

import br.com.igorssk.domain.entities.User
import br.com.igorssk.outbound.AwsCognito
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AwsCognitoTest {
    private lateinit var cognito: AwsCognito
    private lateinit var user: User

    @BeforeAll
    fun beforeAll() {
        cognito = AwsCognito()
    }

    @BeforeEach
    fun beforeEach(){
        user = User("a37d9c7d-e4cf-443d-954e-6b1672b93984", "igor-lock@hotmail.com", "Teste@100", "Igor S Silva", "+5534992760462")
    }

    @Test
    fun `should create user with AWS Cognito`() {
        cognito.signUp(user)
    }

    @Test
    fun `should authenticate with AWS Cognito`() {
        val userToken = cognito.authenticate(user.id, user.password)

        assert(userToken.accessToken.isNotEmpty())
    }

    @Test
    fun `should resend verification code`() {
        cognito.sendOtpToken(user.id)
    }

    @Test
    fun `should return ok for verification code`() {
        val token = "923708"
        cognito.confirmSignUp(user.id, token)
    }
}