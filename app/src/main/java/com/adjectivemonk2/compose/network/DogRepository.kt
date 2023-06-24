package com.adjectivemonk2.compose.network

import javax.inject.Inject

interface DogRepository {
  suspend fun getBreeds(): List<String>

  suspend fun randomImageFor(breed: String): String
}

class DogRepositoryImpl @Inject constructor(private val dogPicsApi: DogPicsApi) : DogRepository {
  override suspend fun getBreeds(): List<String> {
    return dogPicsApi.listBreeds().message.flatMap { (breed, subBreeds) ->
      if (subBreeds.isEmpty()) {
        listOf(breed)
      } else {
        subBreeds.map { "$breed/$it" }
      }
    }
  }

  override suspend fun randomImageFor(breed: String): String {
    return dogPicsApi.randomImageFor(breed).message
  }
}
