package com.bravedroid.jobby.infrastructure.data.repository

import com.bravedroid.jobby.infrastructure.data.network.findwork.datasource.NetworkDataSource
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

class JobsRepositoryImplTest {

    private lateinit var sut: JobsRepositoryImpl

    @Test
    fun getAndroidJobs() {
        val networkDataSourceMock: NetworkDataSource = mock(NetworkDataSource::class.java)
        sut = JobsRepositoryImpl(networkDataSourceMock)
        sut.getAndroidJobs()
        verify(networkDataSourceMock).fetchJobs()
    }
}
