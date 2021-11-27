package com.bravedroid.jobby.domain.entities

data class Job(
    val id: Int,
    val role: String,
    val keywords: List<String>,
    val isRemote: Boolean,
    val companyName: String,
    val datePosted: String,
    val employmentType: String,
    val companyNumEmployees: String,
    val logo: String,
    val description: String,
)
