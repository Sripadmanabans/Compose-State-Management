package com.adjectivemonk2.compose.presenter

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class BreedsViewModel @Inject constructor(
  private val breedsPresenter: BreedsPresenter,
  private val breedPicPresenter: BreedPicPresenter,
) : ViewModel() {

  private val scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

  val uiModel = scope.launchMolecule(RecompositionClock.ContextClock) { present() }

  fun onBreedSelection(breed: Breed) {
    breedsPresenter.take(BreedsEvent.OnSelect(breed))
  }

  fun onBack() {
    breedsPresenter.take(BreedsEvent.ClearSelection)
  }

  fun onRandomClick() {
    breedPicPresenter.take(BreedPicEvent.Randomize)
  }

  @Composable
  private fun present(): BreedsWithPicsUi {
    val breedsUi = breedsPresenter.present()
    val breedPicUi = breedsUi.selectedBreed?.let {
      breedPicPresenter.present(it)
    }
    Log.d("Presenting", "${breedsUi.breeds.size}, ${breedsUi.selectedBreed}, $breedPicUi")
    return BreedsWithPicsUi(breedsUi, breedPicUi)
  }
}

@Immutable
data class BreedsWithPicsUi(val breedsUi: BreedsUi, val breedPicUi: BreedPicUi?)
