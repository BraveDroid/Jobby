package com.bravedroid.jobby.infrastructure.data.network.findwork.dto


import com.bravedroid.jobby.domain.entities.Job
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultDto(
    @SerialName("company_name")
    val companyName: String = "",
    @SerialName("company_num_employees")
    val companyNumEmployees: String = "",
    @SerialName("date_posted")
    val datePosted: String = "",
    @SerialName("employment_type")
    val employmentType: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("keywords")
    val keywords: List<String> = listOf(),
    @SerialName("location")
    val location: String = "",
    @SerialName("logo")
    val logo: String = "",
    @SerialName("remote")
    val remote: Boolean = false,
    @SerialName("role")
    val role: String = "",
    @SerialName("source")
    val source: String = "",
    @SerialName("text")
    val text: String = "",
    @SerialName("url")
    val url: String = "",
) {
    companion object {
        fun ResultDto.toJob() = Job(
            role = role,
            keywords = keywords,
            isRemote = remote,
            companyName = companyName,
            datePosted = datePosted,
            employmentType = employmentType,
            companyNumEmployees = companyNumEmployees,
            logo = logo,
            description = text,
        )
    }
}
