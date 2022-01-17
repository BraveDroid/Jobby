package com.bravedroid.jobby.login.register

import com.bravedroid.jobby.core.CoroutineProvider
import com.bravedroid.jobby.domain.entities.ErrorEntity
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.usecases.LoginUserUseCase
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.asFlow
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultSuccess
import com.bravedroid.jobby.login.login.LoginViewModel
import com.bravedroid.jobby.login.util.FormValidator
import com.bravedroid.jobby.login.util.RegisterValidation
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.any
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    private lateinit var sut: RegisterViewModel
    private lateinit var coroutineProviderMock: CoroutineProvider
    private lateinit var registerUserUseCaseMock: RegisterUserUseCase
    private lateinit var loggerMock: Logger
    private lateinit var formValidatorMock: FormValidator

    private lateinit var registerRequest: RegisterUserUseCase.RegistrationRequest
    private lateinit var model: RegisterUiModel
    private lateinit var uiSate: RegisterViewModel.RegisterUiState


    @Before
    fun setUp() {
        registerRequest = RegisterUserUseCase.RegistrationRequest(
            "admin@gmail.com",
            "admin",
            "12345678",
        )

        model = RegisterUiModel(
            "admin@gmail.com",
            "admin",
            "12345678",
        )

        uiSate = RegisterViewModel.RegisterUiState(
            "admin@gmail.com",
            "admin",
            "12345678",
            true,
        )

        coroutineProviderMock = mock(CoroutineProvider::class.java)
        registerUserUseCaseMock = mock(RegisterUserUseCase::class.java)
        loggerMock = mock(Logger::class.java)
        formValidatorMock = mock(FormValidator::class.java)

        sut = RegisterViewModel(
            coroutineProviderMock,
            registerUserUseCaseMock,
            loggerMock,
            formValidatorMock,
        )
    }

    @Test
    fun getUiEventFlowTest() = runTest(UnconfinedTestDispatcher()) {
        //arrange
        Mockito.`when`(coroutineProviderMock.provideViewModelScope(any<RegisterViewModel>()))
            .thenReturn(
                this
            )

        Mockito.`when`(
            registerUserUseCaseMock.invoke(
                registerRequest
            )
        ).thenReturn(RegisterUserUseCase.RegistrationResponse.Registered.toResultSuccess().asFlow())

        val obj = object : (RegisterViewModel.UiEvent) -> Unit {
            val allUiEvent = mutableListOf<RegisterViewModel.UiEvent>()
            override fun invoke(p1: RegisterViewModel.UiEvent) {
                allUiEvent.add(p1)
            }
        }

        //act
        val job = launch {
            sut.uiEventFlow.collect(obj::invoke)
        }

        sut.register(model)

        //assert
        verify(registerUserUseCaseMock).invoke(
            registerRequest
        )
        Truth.assertThat(obj.allUiEvent).contains(
            RegisterViewModel.UiEvent.NavigationToLoginScreen
        )
        job.cancelAndJoin()

    }

    @Test
    fun registerTest() = runTest(UnconfinedTestDispatcher()) {
        //arrange
        Mockito.`when`(coroutineProviderMock.provideViewModelScope(any<RegisterViewModel>()))
            .thenReturn(
                this
            )

        Mockito.`when`(
            registerUserUseCaseMock.invoke(
                registerRequest
            )
        ).thenReturn(DomainResult.Error(ErrorEntity.Unknown).asFlow())

        val obj = object : (RegisterViewModel.UiEvent) -> Unit {
            val allUiEvent = mutableListOf<RegisterViewModel.UiEvent>()
            override fun invoke(p1: RegisterViewModel.UiEvent) {
                allUiEvent.add(p1)
            }
        }

        //act
        val job = launch {
            sut.uiEventFlow.collect(obj::invoke)
        }

        sut.register(model)

        //assert
        verify(registerUserUseCaseMock).invoke(
            registerRequest
        )
        Truth.assertThat(obj.allUiEvent).contains(
            RegisterViewModel.UiEvent.ShowError(
                "Unknown Error !"
            )
        )
        job.cancelAndJoin()

    }

    @Test
    fun getRegisterUiModelStateFlowTest() = runTest(UnconfinedTestDispatcher()) {
        //arrange
        Mockito.`when`(coroutineProviderMock.provideViewModelScope(any<RegisterViewModel>()))
            .thenReturn(
                this
            )

        val obj = object : (RegisterViewModel.RegisterUiState) -> Unit {
            val allUiState = mutableListOf<RegisterViewModel.RegisterUiState>()
            override fun invoke(p1: RegisterViewModel.RegisterUiState) {
                allUiState.add(p1)
            }
        }

        //act
        val job = launch {
            sut.registerUiModelStateFlow.collect(obj::invoke)
        }
        sut.saveRegisterState(uiSate)

        //assert
        Truth.assertThat(obj.allUiState).contains(uiSate)
        job.cancelAndJoin()
    }

    @Test
    fun validateRegisterFormTest() = runTest(UnconfinedTestDispatcher()) {
        //arrange
        Mockito.`when`(coroutineProviderMock.provideViewModelScope(any<RegisterViewModel>()))
            .thenReturn(
                this
            )

        val name = MutableStateFlow("admin")
        val email = MutableStateFlow("admin@gmail.com")
        val password = MutableStateFlow("12345678")

          sut.validateRegisterForm(
            name,
            email,
            password,
        )

        verify(formValidatorMock).validateRegisterForm(
            name,
            email,
            password,
        )
    }

}
