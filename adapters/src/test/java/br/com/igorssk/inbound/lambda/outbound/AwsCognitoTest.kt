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
        user = User("878aa827-2e26-417d-8302-c2756c42e9a6", "igor-lock@hotmail.com", "Teste@100", "Igor S Silva", "+5534992760462")
    }

    @Test
    fun `should authenticate with AWS Cognito`() {
        cognito.authenticate(user.id, user.password)
    }

    @Test
    fun `should resend verification code`() {
        cognito.resendVerificationToken(user.id)
    }

    @Test
    fun `should return ok for verification code`() {
        val token = "439480"
        cognito.verifyUser(user.id, token)
    }
}