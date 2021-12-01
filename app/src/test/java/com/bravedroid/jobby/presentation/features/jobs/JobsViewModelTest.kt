package com.bravedroid.jobby.presentation.features.jobs

import androidx.lifecycle.SavedStateHandle
import com.bravedroid.jobby.di.CoroutineProvider
import com.bravedroid.jobby.domain.entities.Job
import com.bravedroid.jobby.domain.usecases.GetAndroidJobsUseCase
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.asFlow
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultSuccess
import com.bravedroid.jobby.presentation.util.PageState
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.any
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class JobsViewModelTest {

    private lateinit var sut: JobsViewModel
    private lateinit var coroutineProviderMock: CoroutineProvider
    private lateinit var savedStateHandleMock: SavedStateHandle
    private lateinit var getAndroidJobsUseCaseMock: GetAndroidJobsUseCase

    private val nonRemoteJob = Job(
        id= 1,
        role = "Java, Spring, AWS Software Engineer",
        keywords = listOf("kafka", "restful", "aws", "lambda", "spring"),
        isRemote = false,
        companyName = "JPMorgan Chase & Co.",
        datePosted = "2021-11-04T04:00:00Z",
        employmentType = "full time",
        companyNumEmployees = "",
        logo = "https://findwork-dev-images.s3.amazonaws.com/jpmorgan-chase-co-job-98824",
        description = " The Chief Technology Office for JPMorgan Chase oversees enabling components inclusive of the top quality engineering and architecture tools and practices required to make us a leading technology company. We will drive firmwide efforts in such critical areas as Cloud Enablement &amp; Adoption, Technology Portfolio Management and Rationalization, Platform and API Strategy, Development Tooling and Practices, Big Data and Machine Learning services, Data Architecture, Reference Architecture, and Technology Data Management.<p>We are seeking a Core Java Software Engineer to help drive this effort - key responsibilities would include:<br/>     * Lead software design and implementation of major components and systems.<br/>     * Develop high-available and high-scalable system using Caching, Elastic Search, Kafka etc.<br/>     * Build a team and cultivate innovation by driving cross-team collaboration and project execution.<br/>     * Manage project deliverables and deadlines with your technical expertise.<br/>     * Coach and mentor junior engineers on design techniques, engineering process, coding standards, and testing practices.<p>Key qualifications include:<br/>     * BS/BA degree or equivalent experience<br/>     * Strong understanding of Architecture, Design Patterns, and Object Oriented Analysis and Design.<br/>     * Strong problem solving and consensus building skills.<br/>     * Following SDLC best practices such as design review, code review, CICD, unit tests and end-to-end testing, etc.<br/>     * Expert with Java using Generics, Lambda, Mockito, and Java Concurrency.<br/>     * Proficient with Core Spring framework, Dependency Injection, Spring Restful Service.<br/>     * Experiences with technologies such as Elastic Search, Kafka, AWS.<br/>     * Familiar with building high-scalable and high-available system.<br/>     * Ability to solve complex business problems and present recommendations to senior management effectively.<br/>     * Good verbal and written communication skills including the ability to present design solutions to stakeholders.<p>When you work at JPMorgan Chase &amp; Co., you're not just working at a global financial institution. You're an integral part of one of the world's biggest tech companies. In 15 technology centers worldwide, our team of 50,000 technologists design, build and deploy everything from enterprise technology initiatives to big data and mobile solutions, as well as innovations in electronic payments, cybersecurity, machine learning, and cloud development. Our $11B annual investment in technology enables us to hire people to create innovative solutions that are transforming the financial services industry.<p>At JPMorgan Chase &amp; Co. we value the unique skills of every employee, and we're building a technology organization that thrives on diversity. We encourage professional growth and career development, and offer competitive benefits and compensation. If you're looking to build your career as part of a global technology team tackling big challenges that impact the lives of people and companies all around the world, we want to meet you.<p>JPMorgan Chase &amp; Co., one of the oldest financial institutions, offers innovative financial solutions to millions of consumers, small businesses and many of the world's most prominent corporate, institutional and government clients under the J.P. Morgan and Chase brands. Our history spans over 200 years and today we are a leader in investment banking, consumer and small business banking, commercial banking, financial transaction processing and asset management.<p>We recognize that our people are our strength and the diverse talents they bring to our global workforce are directly linked to our success. We are an equal opportunity employer and place a high value on diversity and inclusion at our company. We do not discriminate on the basis of any protected attribute, including race, religion, color, national origin, gender, sexual orientation, gender identity, gender expression, age, marital or veteran status, pregnancy or disability, or any other basis protected under applicable law. In accordance with applicable law, we make reasonable accommodations for applicants' and employees' religious practices and beliefs, as well as any mental health or physical disability needs.<p>Equal Opportunity Employer/Disability/Veterans</p> <br><br/></br></p></p></p></p></p></p>",
    )

    private val remoteJob = Job(
        id= 2,
        role = "Java, Spring, AWS Software Engineer",
        keywords = listOf("kafka", "restful", "aws", "lambda", "spring"),
        isRemote = true,
        companyName = "JPMorgan Chase & Co.",
        datePosted = "2021-11-04T04:00:00Z",
        employmentType = "full time",
        companyNumEmployees = "",
        logo = "https://findwork-dev-images.s3.amazonaws.com/jpmorgan-chase-co-job-98824",
        description = " The Chief Technology Office for JPMorgan Chase oversees enabling components inclusive of the top quality engineering and architecture tools and practices required to make us a leading technology company. We will drive firmwide efforts in such critical areas as Cloud Enablement &amp; Adoption, Technology Portfolio Management and Rationalization, Platform and API Strategy, Development Tooling and Practices, Big Data and Machine Learning services, Data Architecture, Reference Architecture, and Technology Data Management.<p>We are seeking a Core Java Software Engineer to help drive this effort - key responsibilities would include:<br/>     * Lead software design and implementation of major components and systems.<br/>     * Develop high-available and high-scalable system using Caching, Elastic Search, Kafka etc.<br/>     * Build a team and cultivate innovation by driving cross-team collaboration and project execution.<br/>     * Manage project deliverables and deadlines with your technical expertise.<br/>     * Coach and mentor junior engineers on design techniques, engineering process, coding standards, and testing practices.<p>Key qualifications include:<br/>     * BS/BA degree or equivalent experience<br/>     * Strong understanding of Architecture, Design Patterns, and Object Oriented Analysis and Design.<br/>     * Strong problem solving and consensus building skills.<br/>     * Following SDLC best practices such as design review, code review, CICD, unit tests and end-to-end testing, etc.<br/>     * Expert with Java using Generics, Lambda, Mockito, and Java Concurrency.<br/>     * Proficient with Core Spring framework, Dependency Injection, Spring Restful Service.<br/>     * Experiences with technologies such as Elastic Search, Kafka, AWS.<br/>     * Familiar with building high-scalable and high-available system.<br/>     * Ability to solve complex business problems and present recommendations to senior management effectively.<br/>     * Good verbal and written communication skills including the ability to present design solutions to stakeholders.<p>When you work at JPMorgan Chase &amp; Co., you're not just working at a global financial institution. You're an integral part of one of the world's biggest tech companies. In 15 technology centers worldwide, our team of 50,000 technologists design, build and deploy everything from enterprise technology initiatives to big data and mobile solutions, as well as innovations in electronic payments, cybersecurity, machine learning, and cloud development. Our $11B annual investment in technology enables us to hire people to create innovative solutions that are transforming the financial services industry.<p>At JPMorgan Chase &amp; Co. we value the unique skills of every employee, and we're building a technology organization that thrives on diversity. We encourage professional growth and career development, and offer competitive benefits and compensation. If you're looking to build your career as part of a global technology team tackling big challenges that impact the lives of people and companies all around the world, we want to meet you.<p>JPMorgan Chase &amp; Co., one of the oldest financial institutions, offers innovative financial solutions to millions of consumers, small businesses and many of the world's most prominent corporate, institutional and government clients under the J.P. Morgan and Chase brands. Our history spans over 200 years and today we are a leader in investment banking, consumer and small business banking, commercial banking, financial transaction processing and asset management.<p>We recognize that our people are our strength and the diverse talents they bring to our global workforce are directly linked to our success. We are an equal opportunity employer and place a high value on diversity and inclusion at our company. We do not discriminate on the basis of any protected attribute, including race, religion, color, national origin, gender, sexual orientation, gender identity, gender expression, age, marital or veteran status, pregnancy or disability, or any other basis protected under applicable law. In accordance with applicable law, we make reasonable accommodations for applicants' and employees' religious practices and beliefs, as well as any mental health or physical disability needs.<p>Equal Opportunity Employer/Disability/Veterans</p> <br><br/></br></p></p></p></p></p></p>",
    )

    @Test
    fun getPageStateFlowTest() = runTest (UnconfinedTestDispatcher()){
        //arrange
        coroutineProviderMock = mock(CoroutineProvider::class.java)
        savedStateHandleMock = mock(SavedStateHandle::class.java)
        getAndroidJobsUseCaseMock = mock(GetAndroidJobsUseCase::class.java)
        val getAndroidJobsResponse: GetAndroidJobsUseCase.GetAndroidJobsResponse =
            GetAndroidJobsUseCase.GetAndroidJobsResponse(
                listOf(remoteJob),
                listOf(nonRemoteJob),
            )
        `when`(coroutineProviderMock.provideViewModelScope(any<JobsViewModel>())).thenReturn(
            this
        )

        `when`(getAndroidJobsUseCaseMock.invoke()).thenReturn(
            getAndroidJobsResponse.toResultSuccess().asFlow()
        )

        val obj = object : (PageState<GetAndroidJobsUseCase.GetAndroidJobsResponse>) -> Unit {
            val allPageStates =
                mutableListOf<PageState<GetAndroidJobsUseCase.GetAndroidJobsResponse>>()

            override fun invoke(p1: PageState<GetAndroidJobsUseCase.GetAndroidJobsResponse>) {
                allPageStates.add(p1)
            }
        }

        //act
        sut = JobsViewModel(
            coroutineProviderMock,
            savedStateHandleMock,
            getAndroidJobsUseCaseMock,
        )
        val job = launch {
            sut.pageStateFlow.collect(obj::invoke)
        }

        //assert
        verify(getAndroidJobsUseCaseMock).invoke()
       Truth.assertThat(obj.allPageStates).contains(
           PageState.Content(getAndroidJobsResponse),
       )
        job.cancelAndJoin()
    }

    @Test
    fun getUiEventFlow() = runTest (UnconfinedTestDispatcher()) {

        //arrange
        coroutineProviderMock = mock(CoroutineProvider::class.java)
        savedStateHandleMock = mock(SavedStateHandle::class.java)
        getAndroidJobsUseCaseMock = mock(GetAndroidJobsUseCase::class.java)
        val getAndroidJobsResponse: GetAndroidJobsUseCase.GetAndroidJobsResponse =
            GetAndroidJobsUseCase.GetAndroidJobsResponse(
                listOf(remoteJob),
                listOf(nonRemoteJob),
            )
        `when`(coroutineProviderMock.provideViewModelScope(any<JobsViewModel>())).thenReturn(
            this
        )

        `when`(getAndroidJobsUseCaseMock.invoke()).thenReturn(
            getAndroidJobsResponse.toResultSuccess().asFlow()
        )

        val obj = object : (JobsViewModel.UiEvent) -> Unit {
            val uiEvent =
                mutableListOf<JobsViewModel.UiEvent>()

            override fun invoke(p1: JobsViewModel.UiEvent) {
                uiEvent.add(p1)
            }
        }
        sut = JobsViewModel(
            coroutineProviderMock,
            savedStateHandleMock,
            getAndroidJobsUseCaseMock,
        )
        val job = launch {
            sut.uiEventFlow.collect(obj::invoke)
        }

        //act
       sut.loadJobs()

        //assert
        Truth.assertThat(obj.uiEvent).contains(
            JobsViewModel.UiEvent.ContentLoaded,
        )
        job.cancelAndJoin()
    }
}
