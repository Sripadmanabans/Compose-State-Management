package com.adjectivemonk2.compose.network

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun okhttp(): OkHttpClient {
    return OkHttpClient.Builder().build()
  }

  @Provides
  @Singleton
  fun moshi(): Moshi {
    return Moshi.Builder().build()
  }

  @Provides
  @Singleton
  fun moshiConverterFactory(moshi: Moshi): Converter.Factory {
    return MoshiConverterFactory.create(moshi)
  }

  @Provides
  @Singleton
  fun retrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://dog.ceo/api/")
      .client(okHttpClient)
      .addConverterFactory(converterFactory)
      .build()
  }

  @Provides
  @Singleton
  fun dogPicsApi(retrofit: Retrofit): DogPicsApi {
    return retrofit.create()
  }
}
