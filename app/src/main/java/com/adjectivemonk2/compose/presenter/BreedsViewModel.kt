package com.adjectivemonk2.compose.presenter

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class BreedsViewModel @Inject constructor(
  private val breedsUseCase: BreedsUseCase,
  private val breedPicUseCase: BreedPicUseCase,
) : ViewModel() {

  private val scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

  val uiModel = scope.launchMolecule(RecompositionClock.ContextClock) { models() }

  fun onBreedSelection(breed: Breed) {
    breedsUseCase.take(BreedsEvent.OnSelect(breed))
  }

  fun onBack() {
    breedsUseCase.take(BreedsEvent.ClearSelection)
  }

  fun onRandomClick() {
    breedPicUseCase.take(BreedPicEvent.Randomize)
  }

  @Composable
  private fun models(): BreedsWithPicsUi {
    val breedsUi = breedsUseCase.models()
    val breedPicUi = breedsUi.selectedBreed?.let {
      breedPicUseCase.models(it)
    }
    Log.d("Presenting", "${breedsUi.breeds.size}, ${breedsUi.selectedBreed}, $breedPicUi")
    return BreedsWithPicsUi(breedsUi, breedPicUi)
  }
}

@Immutable
data class BreedsWithPicsUi(val breedsUi: BreedsUi, val breedPicUi: BreedPicUi?)
