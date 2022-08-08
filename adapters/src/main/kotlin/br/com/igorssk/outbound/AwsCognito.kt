package br.com.igorssk.outbound

import aws.sdk.kotlin.services.cognitoidentityprovider.CognitoIdentityProviderClient
import aws.sdk.kotlin.services.cognitoidentityprovider.model.*
import aws.smithy.kotlin.runtime.client.SdkLogMode
import br.com.igorssk.domain.dtos.DestinatioType
import br.com.igorssk.domain.dtos.OtpTokenResponseDto
import br.com.igorssk.domain.dtos.SignUpResponseDto
import br.com.igorssk.domain.entities.User
import br.com.igorssk.domain.entities.UserToken
import br.com.igorssk.ports.outbound.UserAccessManagerPort
import br.com.igorssk.extensions.safeAwait
import io.micronaut.context.annotation.Bean
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val POOL_ID = "us-east-1_xH5L2YRXT"
private const val CLIENT_ID = "5dr4jrkncgbdn7191ctjdip0gj"
private const val CLIENT_SECRET = "2fpivsu7euc1lihs7kgmdfae62os8jaf282vnhdae2t5usc705k"

@Bean
class AwsCognito : UserAccessManagerPort {
    private val cognitoClient = CognitoIdentityProviderClient {
        region = "us-east-1"
        sdkLogMode = SdkLogMode.LogResponseWithBody
    }

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
            username = user.email
            clientId = CLIENT_ID
            password = user.password
            userAttributes = attributes
            secretHash = calculateSecretHash(user.email)
        }

        val response = cognitoClient.safeAwait { client -> client.signUp(request) }

        return SignUpResponseDto(response.userSub ?: "")
    }

    override fun confirmSignUp(username: String, otpToken: String) {
        val input = ConfirmSignUpRequest {
            this.username = username
            clientId = CLIENT_ID
            confirmationCode = otpToken
            secretHash = calculateSecretHash(username)
        }

        cognitoClient.safeAwait { client -> client.confirmSignUp(input) }
    }

    override fun sendOtpToken(username: String): OtpTokenResponseDto {
        val input = ResendConfirmationCodeRequest {
            this.username = username
            clientId = CLIENT_ID
            secretHash = calculateSecretHash(username)
        }

        val response = cognitoClient.safeAwait { client -> client.resendConfirmationCode(input) }

        return response.codeDeliveryDetails?.let { OtpTokenResponseDto(it.destination.toString(), DestinatioType.valueOf(it.deliveryMedium?.value ?: "")) } ?: throw Exception()
    }

    override fun authenticate(username: String, password: String): UserToken {
        val input = AdminInitiateAuthRequest {
            authFlow = AuthFlowType.AdminUserPasswordAuth
            authParameters =
                mapOf("USERNAME" to username, "PASSWORD" to password, "SECRET_HASH" to calculateSecretHash(username))
            clientId = CLIENT_ID
            userPoolId = POOL_ID
        }

        val response = cognitoClient.safeAwait { client -> client.adminInitiateAuth(input) }

        return response.authenticationResult?.let {
            if (it.accessToken.isNullOrEmpty() || it.tokenType.isNullOrEmpty() || it.refreshToken.isNullOrEmpty())
                throw Exception()

            UserToken(it.accessToken!!, it.expiresIn, it.tokenType!!, it.refreshToken!!)
        } ?: throw Exception()
    }
}