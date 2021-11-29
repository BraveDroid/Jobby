package com.bravedroid.jobby.auth.service

import com.bravedroid.jobby.auth.dto.user.UserResponseDto
import retrofit2.http.GET
import retrofit2.http.Headers

class TokenProvider{
    var ACCESS_TOKEN: String = ""

}
interface UserService {
    @GET("user")
//    @Headers("Authorization: Bearer ${TokenProvider().ACCESS_TOKEN}")
    suspend fun loginUser(): UserResponseDto
}
