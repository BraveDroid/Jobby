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
        sut = FindWorkServiceFactory.create()
        val actualResult = sut.getJobs()
        Truth.assertThat(actualResult.results).isNotEmpty()
    }
}
