package com.bravedroid.jobby.companion

import com.bravedroid.jobby.domain.log.Logger
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class FormValidatorTest {

    private lateinit var sut: FormValidator
    private val loggerMock = mock<Logger>()
    private val coroutineProviderMock = mock<CoroutineProvider>()
    private val standardTestDispatcher = StandardTestDispatcher()
    private val unconfinedDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        sut = FormValidator(loggerMock, coroutineProviderMock)
    }

    @Test
    fun validateRegisterFormTest1() = runTest(standardTestDispatcher) {
        val nameFlow = flowOf("admin")
        val emailFlow = flowOf("admin@gmial.com")
        val passwordFlow = flowOf("admin12345")
        whenever(coroutineProviderMock.provideDispatcherCpu()).thenReturn(standardTestDispatcher)

        val result = sut.validateRegisterForm(
            nameSharedFlow = nameFlow,
            emailSharedFlow = emailFlow,
            passwordSharedFlow = passwordFlow,
        )
        Truth.assertThat(result.single()).isTrue()
    }

    @Test
    fun validateRegisterFormTest2() = runTest(unconfinedDispatcher) {
        val nameFlow = MutableStateFlow("admin")
        val emailFlow = MutableStateFlow("admin@gmial.com")
        val passwordFlow = MutableStateFlow("admin12345")
        whenever(coroutineProviderMock.provideDispatcherCpu()).thenReturn(unconfinedDispatcher)

        val result = sut.validateRegisterForm(
            nameSharedFlow = nameFlow,
            emailSharedFlow = emailFlow,
            passwordSharedFlow = passwordFlow,
        )

        val values = mutableListOf<Boolean>()
        val job = launch {
            result.collect {
                values.add(it)
            }
        }
        nameFlow.value = ""
        job.cancel()

        Truth.assertThat(values).containsExactly(true, false)
    }

    @Test
    fun validateLoginFormTest1() = runTest(standardTestDispatcher) {
        val emailFlow = flowOf("admin@gmial.com")
        val passwordFlow = flowOf("admin12345")
        whenever(coroutineProviderMock.provideDispatcherCpu()).thenReturn(standardTestDispatcher)
        val result = sut.validateLoginForm(
            emailSharedFlow = emailFlow,
            passwordSharedFlow = passwordFlow,
        )
        Truth.assertThat(result.single()).isTrue()
    }

    @Test
    fun validateLoginFormTest2() = runTest(unconfinedDispatcher) {
        val emailFlow = MutableStateFlow("admin@gmial.com")
        val passwordFlow = MutableStateFlow("admin12345")
        whenever(coroutineProviderMock.provideDispatcherCpu()).thenReturn(unconfinedDispatcher)

        val result = sut.validateLoginForm(
            emailSharedFlow = emailFlow,
            passwordSharedFlow = passwordFlow,
        )

        val values = mutableListOf<Boolean>()
        val job = launch {
            result.collect {
                values.add(it)
            }
        }
        emailFlow.value = "admin@"
        job.cancel()

        Truth.assertThat(values).containsExactly(true, false)
    }

    @Test
    fun isEmailTest() {
        val list = listOf(
            "john.doe@mail.com",
            "john.doe@mailcom",
            "john.doemail.com",
            "abc+3@example.com",
        )
        val results = mutableListOf<Boolean>()
        list.forEach {
            results.add(sut.isEmail(it))
        }
        Truth.assertThat(results).isEqualTo(listOf(true, false, false, true))
    }
}
