package com.adjectivemonk2.compose.presenter

import com.adjectivemonk2.compose.network.DogRepository
import kotlinx.coroutines.CompletableDeferred

class FakeDogRepository : DogRepository {
  var breedsCompletable = CompletableDeferred<List<String>>()

  var breedsPicCompletable = CompletableDeferred<String>()

  override suspend fun getBreeds(): List<String> {
    return breedsCompletable.await()
  }

  override suspend fun randomImageFor(breed: String): String {
    return breedsPicCompletable.await()
  }
}
