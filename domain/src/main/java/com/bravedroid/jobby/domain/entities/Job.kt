package com.bravedroid.jobby.domain.entities

data class Job(
    val role: String,
    val keywords: List<String>,
    val isRemote: Boolean,
    val companyName: String,
    val datePosted: String,
    val employmentType: String,
    val id: Int = 0,
    val companyNumEmployees: String,
    val logo: String,
    val description: String,
)
