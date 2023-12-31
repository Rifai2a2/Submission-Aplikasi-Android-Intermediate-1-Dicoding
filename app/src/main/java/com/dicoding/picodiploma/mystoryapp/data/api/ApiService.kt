package com.dicoding.picodiploma.mystoryapp.data.api

import com.dicoding.picodiploma.mystoryapp.response.LoginResponse
import com.dicoding.picodiploma.mystoryapp.response.RegisterResponse
import com.dicoding.picodiploma.mystoryapp.response.StoryResponse
import com.dicoding.picodiploma.mystoryapp.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService{
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStory(): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): UploadResponse
}

