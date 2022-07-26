package br.com.igorssk.outbound

import aws.sdk.kotlin.services.cognitoidentityprovider.CognitoIdentityProviderClient
import aws.sdk.kotlin.services.cognitoidentityprovider.model.AttributeType
import aws.sdk.kotlin.services.cognitoidentityprovider.model.SignUpRequest
import br.com.igorssk.domain.dtos.SignUpResponseDto
import br.com.igorssk.domain.entities.User
import br.com.igorssk.ports.outbound.UserAccessManagerPort
import br.com.igorssk.extensions.safeAwait
import io.micronaut.context.annotation.Bean
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val POOL_ID = ""
private const val CLIENT_ID = "5dr4jrkncgbdn7191ctjdip0gj"
private const val CLIENT_SECRET = "2fpivsu7euc1lihs7kgmdfae62os8jaf282vnhdae2t5usc705k"

@Bean
class AwsCognito : UserAccessManagerPort {
    private fun calculateSecretHash(userName: String): String {
        val macSha256Algorithm = "HmacSHA256"
        val signingKey = SecretKeySpec(
            CLIENT_SECRET.toByteArray(StandardCharsets.UTF_8),
            macSha256Algorithm
        )
        try {
            val mac = Mac.getInstance(macSha256Algorithm)
            mac.init(signingKey)
            mac.update(userName.toByteArray(StandardCharsets.UTF_8))
            val rawHmac = mac.doFinal(CLIENT_ID.toByteArray(StandardCharsets.UTF_8))
            return Base64.getEncoder().encodeToString(rawHmac)

        } catch (e: UnsupportedEncodingException) {
            println(e.message)
            throw e
        }
    }

    override fun signUp(user: User): SignUpResponseDto {
        val attributes = listOf(
            AttributeType { name = "email"; value = user.email },
            AttributeType { name = "name"; value = user.name },
            AttributeType { name = "phone_number"; value = user.phoneNumber }
        )

        val request = SignUpRequest {
            username = user.email;
            clientId = CLIENT_ID;
            password = user.password;
            userAttributes = attributes;
            secretHash = calculateSecretHash(user.email)
        }

        val response = CognitoIdentityProviderClient {
            region = "us-east-1"
        }.safeAwait { client -> client.signUp(request) }

        return SignUpResponseDto(response.userSub ?: "")
    }

    override fun verifyUser() {
        TODO("Not yet implemented")
    }

    override fun resendVerificationToken() {
        TODO("Not yet implemented")
    }

    override fun authenticate() {
        TODO("Not yet implemented")
    }
}