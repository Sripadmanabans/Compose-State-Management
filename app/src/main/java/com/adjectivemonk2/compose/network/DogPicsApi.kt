package com.adjectivemonk2.compose.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

interface DogPicsApi {
  @GET("breeds/list/all")
  suspend fun listBreeds(): ListResponse

  @GET("breed/{breed}/images/random")
  suspend fun randomImageFor(@Path("breed", encoded = true) breed: String): ImageResponse
}

@JsonClass(generateAdapter = true)
data class ListResponse(@Json(name = "message") val message: Map<String, List<String>>)

@JsonClass(generateAdapter = true)
data class ImageResponse(@Json(name = "message") val message: String)
