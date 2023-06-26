package com.adjectivemonk2.compose.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.adjectivemonk2.compose.network.DogRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class BreedsUseCase @Inject constructor(private val repository: DogRepository) {
  private val events = MutableSharedFlow<BreedsEvent>(extraBufferCapacity = 5)

  fun take(event: BreedsEvent) {
    check(events.tryEmit(event)) { "Could not emit event!!" }
  }

  @Composable
  fun models(): BreedsUi {
    val breeds = remember { mutableStateListOf<Breed>() }
    var selectedBreed by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
      try {
        val elements = repository.getBreeds().map { Breed(it, false) }
        breeds.addAll(elements)
      } catch (_: Throwable) {
      }
    }
    LaunchedEffect(Unit) {
      events.collect { event ->
        when (event) {
          is BreedsEvent.OnSelect -> {
            val currentSelectedBreed = event.breed
            var previousSelection = -1
            var currentSelection = -1
            for (i in breeds.indices) {
              val breed = breeds[i]
              if (previousSelection == -1 && breed.isSelected) {
                previousSelection = i
              }
              if (currentSelection == -1 && breed.name == currentSelectedBreed.name) {
                currentSelection = i
              }
            }
            check(currentSelection != -1) { "We should always have a selection value!!" }
            if (previousSelection != -1 && previousSelection != currentSelection) {
              breeds[previousSelection] = breeds[previousSelection].copy(isSelected = false)
            }
            if (previousSelection != currentSelection) {
              breeds[currentSelection] = breeds[currentSelection].copy(isSelected = true)
            }
            selectedBreed = currentSelectedBreed.name
          }

          BreedsEvent.ClearSelection -> {
            selectedBreed = null
            val index = breeds.indexOfFirst { it.isSelected }
            breeds[index] = breeds[index].copy(isSelected = false)
          }
        }
      }
    }
    return BreedsUi(breeds.toList(), selectedBreed)
  }
}

sealed interface BreedsEvent {
  class OnSelect(val breed: Breed) : BreedsEvent

  object ClearSelection : BreedsEvent
}

@Immutable
data class BreedsUi(
  val breeds: List<Breed>,
  val selectedBreed: String?,
)

@Immutable
data class Breed(val name: String, val isSelected: Boolean)
