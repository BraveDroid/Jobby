package com.bravedroid.jobby.infrastructure.data.network.findwork.service

import com.bravedroid.jobby.infrastructure.data.network.findwork.NetworkBuilderHiltModule
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
        val okHttp = networkBuilderHiltModule.providesOkHttpClient()
        val retrofit = networkBuilderHiltModule.providesRetrofit(okHttp)
        return networkBuilderHiltModule.providesFindWorkService(retrofit)
    }
}
