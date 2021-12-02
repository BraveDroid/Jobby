package com.bravedroid.jobby.auth

import com.bravedroid.jobby.auth.dto.login.LoginRequestDto
import com.bravedroid.jobby.auth.dto.login.LoginResponseDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenRequestDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenResponseDto
import com.bravedroid.jobby.auth.dto.register.RegisterRequestDto
import com.bravedroid.jobby.auth.dto.register.RegisterResponseDto
import com.bravedroid.jobby.auth.service.AuthService
import com.bravedroid.jobby.auth.service.AuthServiceFake
import com.bravedroid.jobby.domain.entities.ErrorEntity
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.getErrorOrNull
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.isSucceeded
import com.google.common.truth.Truth
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.HttpException

@ExperimentalSerializationApi
@ExperimentalCoroutinesApi
class AuthDataSourceTest : TestCase() {

    private lateinit var sut: AuthDataSource

    @Test
    fun testRegisterUser() = runTest {
        val authServiceFake = AuthServiceFake()
        sut = AuthDataSource(authServiceFake, ErrorHandlerImpl())

        val resultFlow = sut.registerUser(
            RegisterRequestDto(
                email = "UserB@gmail.com",
                name = "UserB",
                password = "UserB2020!",
            )
        )

        val result = resultFlow.single()
        Truth.assertThat(result.isSucceeded).isTrue()

        result as DomainResult.Success
        val response = result.data
        Truth.assertThat(response).isEqualTo(
            RegisterResponseDto(
                name = "UserB"
            )
        )
    }

    @Test
    fun `testRegisterUser error case `() = runTest {
        val authServiceMock = mock(AuthService::class.java)
        `when`(authServiceMock.registerUser(
            RegisterRequestDto(
                email = "UserB@gmail.com",
                name = "UserB",
                password = "User",
            )
        )).thenThrow(RuntimeException("error"))

        sut = AuthDataSource(authServiceMock, ErrorHandlerImpl())

        val resultFlow = sut.registerUser(RegisterRequestDto(
            email = "UserB@gmail.com",
            name = "UserB",
            password = "User",
        ))

        val result = resultFlow.single()

        Truth.assertThat(result.isSucceeded).isFalse()
        Truth.assertThat(result is DomainResult.Error).isTrue()

        Truth.assertThat(result.getErrorOrNull()).isEqualTo(ErrorEntity.Unknown)

    }

    @Test
    fun testLoginUser() = runTest {
        val authServiceFake = AuthServiceFake()
        sut = AuthDataSource(authServiceFake, ErrorHandlerImpl())

        val resultFlow = sut.loginUser(
            LoginRequestDto(
                email = "UserB@gmail.com",
                password = "UserB2020!",
            )
        )

        val result = resultFlow.single()
        Truth.assertThat(result.isSucceeded).isTrue()

        result as DomainResult.Success
        val response = result.data
        Truth.assertThat(response).isEqualTo(
            LoginResponseDto(
                accessToken = "access token",
                refreshToken = "refresh token",
                tokenType = "Bearer",
            )
        )
    }

    @Test
    fun `testLoginUser error case `() = runTest {
        val authServiceMock = mock(AuthService::class.java)
        `when`(authServiceMock.loginUser(
            LoginRequestDto(
                email = "UserB@gmail.com",
                password = "User",
            )
        )).thenThrow(RuntimeException("error"))
        sut = AuthDataSource(authServiceMock, ErrorHandlerImpl())

        val resultFlow = sut.loginUser(
            LoginRequestDto(
                email = "UserB@gmail.com",
                password = "User",
            )
        )
        val result = resultFlow.single()
        Truth.assertThat(result.isSucceeded).isFalse()
        Truth.assertThat(result.getErrorOrNull()).isEqualTo(ErrorEntity.Unknown)
    }

    @Test
    fun testRefreshToken() = runTest {
        val authServiceFake = AuthServiceFake()
        sut = AuthDataSource(authServiceFake, ErrorHandlerImpl())

        val resultFlow = sut.refreshToken(
            RefreshTokenRequestDto(
                "new access token",
            )
        )

        val result = resultFlow.single()
        Truth.assertThat(result.isSucceeded).isTrue()

        result as DomainResult.Success
        val response = result.data
        Truth.assertThat(response).isEqualTo(
            RefreshTokenResponseDto(
                accessToken = "new access token",
                tokenType = "Bearer",
            )
        )
    }

    @Test
    fun `test check refreshToken error case`() = runTest {

        val httpExceptionMock = mock(HttpException::class.java)
        `when`(httpExceptionMock.code()).thenReturn(404)

        val authServiceMock = mock(AuthService::class.java)
        `when`(authServiceMock.refreshToken(
            RefreshTokenRequestDto(
                refreshToken = "refresh Token",
            )
        )).thenThrow(httpExceptionMock)

        sut = AuthDataSource(authServiceMock, ErrorHandlerImpl())

        val resultFlow = sut.refreshToken(
            RefreshTokenRequestDto(
                refreshToken = "refresh Token",
            ))
        val result = resultFlow.single()

        Truth.assertThat(result.isSucceeded).isFalse()
        Truth.assertThat(result.getErrorOrNull()).isEqualTo(ErrorEntity.NotFound)
    }
}
