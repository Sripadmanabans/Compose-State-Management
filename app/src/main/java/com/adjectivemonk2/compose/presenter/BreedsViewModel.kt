package com.adjectivemonk2.compose.presenter

import androidx.compose.runtime.Composable
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
) : ViewModel() {

  private val scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

  val uiModel = scope.launchMolecule(RecompositionClock.ContextClock) { present() }

  fun onBreedSelection(breed: Breed) {
    breedsPresenter.take(BreedsEvent.OnSelect(breed))
  }

  @Composable
  private fun present(): Breeds {
    return breedsPresenter.present()
  }
}
