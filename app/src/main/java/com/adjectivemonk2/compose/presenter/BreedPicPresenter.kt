package com.adjectivemonk2.compose.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.adjectivemonk2.compose.network.DogRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class BreedPicPresenter @Inject constructor(private val repository: DogRepository) {

  private val events = MutableSharedFlow<BreedPicEvent>(extraBufferCapacity = 5)

  fun take(event: BreedPicEvent) {
    check(events.tryEmit(event)) { "Could not emit event!!" }
  }

  @Composable
  fun present(selectedBreed: String): BreedPicUi {
    var loading by remember(selectedBreed) { mutableStateOf(true) }
    var imageUrl by remember(selectedBreed) { mutableStateOf<String?>(null) }
    var fetchInvalidator by remember(selectedBreed) { mutableIntStateOf(0) }
    LaunchedEffect(selectedBreed, fetchInvalidator) {
      imageUrl = repository.randomImageFor(selectedBreed)
      loading = false
    }
    LaunchedEffect(Unit) {
      events.collect {
        when (it) {
          BreedPicEvent.Randomize -> fetchInvalidator++
        }
      }
    }
    return BreedPicUi(loading, selectedBreed, imageUrl)
  }
}

@Immutable
data class BreedPicUi(val loading: Boolean, val selectedBreed: String?, val imageUrl: String?)

sealed interface BreedPicEvent {
  object Randomize : BreedPicEvent
}
