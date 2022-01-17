package com.bravedroid.jobby.login.login

import com.bravedroid.jobby.core.CoroutineProvider
import com.bravedroid.jobby.domain.entities.ErrorEntity
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.usecases.GetAndroidJobsUseCase
import com.bravedroid.jobby.domain.usecases.LoginUserUseCase
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.asFlow
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultSuccess
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.any
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    private lateinit var sut: LoginViewModel
    private lateinit var coroutineProviderMock: CoroutineProvider
    private lateinit var loginUserUseCaseMock: LoginUserUseCase
    private lateinit var loggerMock: Logger

    private lateinit var loginRequest: LoginUserUseCase.LoginRequest
    private lateinit var model: LoginUiModel
    private lateinit var uiSate: LoginViewModel.LoginUiState

    @Before
    fun setUp() {
        loginRequest = LoginUserUseCase.LoginRequest(
            email = "admin@gmail.com",
            password = "12345678",
        )

        model = LoginUiModel(
            email = "admin@gmail.com",
            password = "12345678",
        )

        uiSate = LoginViewModel.LoginUiState(
            email = "admin@gmail.com",
            password = "12345678",
            isValid = true,
        )

        coroutineProviderMock = mock(CoroutineProvider::class.java)
        loginUserUseCaseMock = mock(LoginUserUseCase::class.java)
        loggerMock = mock(Logger::class.java)

        sut = LoginViewModel(
            coroutineProviderMock,
            loginUserUseCaseMock,
            loggerMock,
        )
    }

    @Test
    fun getUiEventFlowTest() = runTest(UnconfinedTestDispatcher()) {

        //arrange
        `when`(coroutineProviderMock.provideViewModelScope(any<LoginViewModel>())).thenReturn(
            this
        )

        `when`(
            loginUserUseCaseMock.invoke(
                loginRequest
            )
        ).thenReturn(LoginUserUseCase.LoginResponse.LoggedIn.toResultSuccess().asFlow())

        val obj = object : (LoginViewModel.UiEvent) -> Unit {
            val allUiEvent = mutableListOf<LoginViewModel.UiEvent>()
            override fun invoke(p1: LoginViewModel.UiEvent) {
                allUiEvent.add(p1)
            }
        }

        //act

        val job = launch {
            sut.uiEventFlow.collect(obj::invoke)
        }
        sut.login(model)

        //assert
        verify(loginUserUseCaseMock).invoke(loginRequest)
        Truth.assertThat(obj.allUiEvent).contains(
            LoginViewModel.UiEvent.NavigationToUserProfile
        )
        job.cancelAndJoin()
    }

    @Test
    fun loginTest() = runTest(UnconfinedTestDispatcher()) {
        //arrange
        `when`(coroutineProviderMock.provideViewModelScope(any<LoginViewModel>())).thenReturn(
            this
        )

        `when`(
            loginUserUseCaseMock.invoke(
                loginRequest
            )
        ).thenReturn(DomainResult.Error(ErrorEntity.Unknown).asFlow())

        val obj = object : (LoginViewModel.UiEvent) -> Unit {
            val allUiEvent = mutableListOf<LoginViewModel.UiEvent>()
            override fun invoke(p1: LoginViewModel.UiEvent) {
                allUiEvent.add(p1)
            }
        }

        //act
        val job = launch {
            sut.uiEventFlow.collect(obj::invoke)
        }
        sut.login(model)

        //assert
        verify(loginUserUseCaseMock).invoke(loginRequest)
        Truth.assertThat(obj.allUiEvent).contains(
            LoginViewModel.UiEvent.ShowError(
                "Unknown Error !"
            )
        )
        job.cancelAndJoin()

    }

    @Test
    fun getLoginUiModelStateFlowTest() = runTest(UnconfinedTestDispatcher()) {
        //arrange
        `when`(coroutineProviderMock.provideViewModelScope(any<LoginViewModel>())).thenReturn(
            this
        )

        val obj = object : (LoginViewModel.LoginUiState) -> Unit {
            val allUiState = mutableListOf<LoginViewModel.LoginUiState>()
            override fun invoke(p1: LoginViewModel.LoginUiState) {
                allUiState.add(p1)
            }
        }

        //act
        val job = launch {
            sut.loginUiModelStateFlow.collect(obj::invoke)
        }
        sut.saveLoginState(uiSate)

        //assert
        Truth.assertThat(obj.allUiState).contains(uiSate)
        job.cancelAndJoin()

    }
}
