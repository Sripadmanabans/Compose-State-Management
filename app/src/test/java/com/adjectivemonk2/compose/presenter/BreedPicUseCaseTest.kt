package com.adjectivemonk2.compose.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BreedPicUseCaseTest {

  @Test
  fun `Load random image based on selection`() = runTest {
    val repository = FakeDogRepository()
    val selectedBreed by mutableStateOf("Border Collie")
    val useCase = BreedPicUseCase(repository)
    moleculeFlow(RecompositionClock.Immediate) { useCase.models(selectedBreed) }.test {
      val item1 = awaitItem()
      assertThat(item1).isEqualTo(BreedPicUi(true, selectedBreed, null))
      repository.breedsPicCompletable.complete("image1")
      val item2 = awaitItem()
      assertThat(item2).isEqualTo(BreedPicUi(false, selectedBreed, "image1"))
      expectNoEvents()
    }
  }

  @Test
  fun `Load random image based on selection, random image loads again`() = runTest {
    val repository = FakeDogRepository()
    val selectedBreed by mutableStateOf("Border Collie")
    val useCase = BreedPicUseCase(repository)
    moleculeFlow(RecompositionClock.Immediate) { useCase.models(selectedBreed) }.test {
      val item1 = awaitItem()
      assertThat(item1).isEqualTo(BreedPicUi(true, selectedBreed, null))
      repository.breedsPicCompletable.complete("image1")
      val item2 = awaitItem()
      assertThat(item2).isEqualTo(BreedPicUi(false, selectedBreed, "image1"))
      repository.breedsPicCompletable = CompletableDeferred()
      useCase.take(BreedPicEvent.Randomize)
      val item3 = awaitItem()
      assertThat(item3).isEqualTo(BreedPicUi(true, selectedBreed, "image1"))
      repository.breedsPicCompletable.complete("image2")
      val item4 = awaitItem()
      assertThat(item4).isEqualTo(BreedPicUi(false, selectedBreed, "image2"))
      expectNoEvents()
    }
  }

  @Test
  fun `Load random image based on selection, update selection`() = runTest {
    val repository = FakeDogRepository()
    var selectedBreed by mutableStateOf("Border Collie")
    val useCase = BreedPicUseCase(repository)
    moleculeFlow(RecompositionClock.Immediate) { useCase.models(selectedBreed) }.test {
      val item1 = awaitItem()
      assertThat(item1).isEqualTo(BreedPicUi(true, selectedBreed, null))
      repository.breedsPicCompletable.complete("image1")
      val item2 = awaitItem()
      assertThat(item2).isEqualTo(BreedPicUi(false, selectedBreed, "image1"))
      repository.breedsPicCompletable = CompletableDeferred()
      selectedBreed = "Golden Retriever"
      val item3 = awaitItem()
      assertThat(item3).isEqualTo(BreedPicUi(true, selectedBreed, null))
      repository.breedsPicCompletable.complete("imageGR1")
      val item4 = awaitItem()
      assertThat(item4).isEqualTo(BreedPicUi(false, selectedBreed, "imageGR1"))
      expectNoEvents()
    }
  }
}
