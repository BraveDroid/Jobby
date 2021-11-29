package com.bravedroid.jobby.auth.service

import com.bravedroid.jobby.auth.dto.login.LoginRequestDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenRequestDto
import com.bravedroid.jobby.auth.dto.register.RegisterRequestDto
import com.bravedroid.jobby.auth.dto.register.RegisterResponseDto
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.random.Random

@ExperimentalCoroutinesApi
class AuthServiceTest {
    private lateinit var sut: AuthService

    @Test
    fun `check availability service`() = runBlocking {
        sut = AuthServiceFactory.create()
        val name = createRandomString()
        val email = "$name@gmail.com"
        val password = "$name&2020"

        val registrationResult = sut.registerUser(
            RegisterRequestDto(
                email = email,
                name = name,
                password = password,
            )
        )
        Truth.assertThat(registrationResult).isEqualTo(RegisterResponseDto(
            name = name
        ))

        val loginResult = sut.loginUser(LoginRequestDto(
            email, password
        ))
        Truth.assertThat(loginResult.tokenType).isEqualTo("Bearer")

        val refreshTokenResult = sut.refreshToken(RefreshTokenRequestDto(
            loginResult.refreshToken
        ))
        Truth.assertThat(refreshTokenResult.tokenType).isEqualTo("Bearer")
    }

    private fun createRandomString(): String {
        val stringLength = 6
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..stringLength)
            .map { _ -> Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}
