package com.bravedroid.jobby.auth

import com.bravedroid.jobby.auth.dto.login.LoginRequestDto
import com.bravedroid.jobby.auth.dto.login.LoginResponseDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenRequestDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenResponseDto
import com.bravedroid.jobby.auth.dto.register.RegisterRequestDto
import com.bravedroid.jobby.auth.dto.register.RegisterResponseDto
import com.bravedroid.jobby.auth.service.AuthServiceFake
import com.bravedroid.jobby.domain.utils.Result
import com.bravedroid.jobby.domain.utils.Result.Companion.isSucceeded
import com.google.common.truth.Truth
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Test

@ExperimentalSerializationApi
@ExperimentalCoroutinesApi
class AuthDataSourceTest : TestCase() {

    private lateinit var sut: AuthDataSource

    @Test
    fun testRegisterUser() = runTest {
        val authServiceFake = AuthServiceFake()
        sut = AuthDataSource(authServiceFake)

        val resultFlow = sut.registerUser(
            RegisterRequestDto(
                email = "UserB@gmail.com",
                name = "UserB",
                password = "UserB2020!",
            )
        )

        val result = resultFlow.single()
        Truth.assertThat(result.isSucceeded).isTrue()

        result as Result.Success
        val response = result.data
        Truth.assertThat(response).isEqualTo(
            RegisterResponseDto(
                name = "UserB"
            )
        )
    }

    @Test
    fun testLoginUser() = runTest {
        val authServiceFake = AuthServiceFake()
        sut = AuthDataSource(authServiceFake)

        val resultFlow = sut.loginUser(
            LoginRequestDto(
                email = "UserB@gmail.com",
                password = "UserB2020!",
            )
        )

        val result = resultFlow.single()
        Truth.assertThat(result.isSucceeded).isTrue()

        result as Result.Success
        val response = result.data
        Truth.assertThat(response).isEqualTo(
            LoginResponseDto(
                accessToken = "access token",
                refreshToken = "refresh token",
                tokenType = "Bearer"
            )
        )
    }

    @Test
    fun testRefreshToken() = runTest {
        val authServiceFake = AuthServiceFake()
        sut = AuthDataSource(authServiceFake)

        val resultFlow = sut.refreshToken(
            RefreshTokenRequestDto(
                "new access token",
            )
        )

        val result = resultFlow.single()
        Truth.assertThat(result.isSucceeded).isTrue()

        result as Result.Success
        val response = result.data
        Truth.assertThat(response).isEqualTo(
            RefreshTokenResponseDto(
                accessToken = "new access token",
                tokenType = "Bearer"
            )
        )
    }
}
