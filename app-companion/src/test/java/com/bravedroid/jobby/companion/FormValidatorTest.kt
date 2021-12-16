package com.bravedroid.jobby.companion

import com.bravedroid.jobby.domain.log.Logger
import com.google.common.truth.Truth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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

    @Before
    fun setUp() {
        sut = FormValidator(loggerMock, coroutineProviderMock)
    }

    private val standardTestDispatcher = StandardTestDispatcher()

    @Test
    fun validateRegisterFormTest1() = runTest(standardTestDispatcher) {
        val nameFlow = flowOf("admin")
        val emailFlow = flowOf("admin@")
        val passwordFlow = flowOf("admin12345")
        whenever(coroutineProviderMock.provideDispatcherCpu()).thenReturn(standardTestDispatcher)

        val result = sut.validateRegisterForm(
            nameSharedFlow = nameFlow,
            emailSharedFlow = emailFlow,
            passwordSharedFlow = passwordFlow,
        )
        Truth.assertThat(result.single()).isTrue()
    }

    private val unconfinedDispatcher = UnconfinedTestDispatcher()

    @Test
    fun validateRegisterFormTest2() = runTest(unconfinedDispatcher) {
        val nameFlow = MutableStateFlow("admin")
        val emailFlow = MutableStateFlow("admin@")
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
    fun testValidateLoginForm() = runTest {
//        sut.validateLoginForm(
//
//        )
    }
}
