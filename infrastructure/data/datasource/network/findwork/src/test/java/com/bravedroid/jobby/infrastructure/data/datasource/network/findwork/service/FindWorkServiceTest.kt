package com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.service

import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class FindWorkServiceTest {

   private lateinit var sut: FindWorkService

    @Test
    fun `check availability service`() = runBlocking {
        sut = createFindWorkServiceByRetrofit()
        val actualResult = sut.getJobs()
        Truth.assertThat(actualResult.results).isNotEmpty()
    }

    private fun createFindWorkServiceByRetrofit(): FindWorkService {
        val networkBuilderHiltModule = NetworkBuilderHiltModule()
        return networkBuilderHiltModule.providesFindWorkService()
    }
}
