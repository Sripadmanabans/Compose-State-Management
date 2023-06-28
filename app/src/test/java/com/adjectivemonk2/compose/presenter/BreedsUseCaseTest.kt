package com.adjectivemonk2.compose.presenter

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BreedsUseCaseTest {

  @Test
  fun `Loading data success`() = runTest {
    val breeds = listOf("Pug", "Border Collie", "German Shepard", "Golden Retriever")
    val repository = FakeDogRepository()
    val useCase = BreedsUseCase(repository)
    moleculeFlow(RecompositionClock.Immediate) { useCase.models() }.test {
      val item1 = awaitItem()
      assertThat(item1).isEqualTo(BreedsUi(emptyList(), null))
      repository.breedsCompletable.complete(breeds)
      val item2 = awaitItem()
      assertThat(item2).isEqualTo(BreedsUi(breeds.map { Breed(it, false) }, null))
      expectNoEvents()
    }
  }

  @Test
  fun `Loading data fail`() = runTest {
    val repository = FakeDogRepository()
    val useCase = BreedsUseCase(repository)
    moleculeFlow(RecompositionClock.Immediate) { useCase.models() }.test {
      val item1 = awaitItem()
      assertThat(item1).isEqualTo(BreedsUi(emptyList(), null))
      repository.breedsCompletable.completeExceptionally(RuntimeException("Something is wrong"))
      expectNoEvents()
    }
  }

  @Test
  fun `Loading data success select border collie`() = runTest {
    val breeds = listOf("Pug", "Border Collie", "German Shepard", "Golden Retriever")
    val repository = FakeDogRepository()
    repository.breedsCompletable.complete(breeds)
    val useCase = BreedsUseCase(repository)
    moleculeFlow(RecompositionClock.Immediate) { useCase.models() }.test {
      skipItems(2)
      useCase.take(BreedsEvent.OnSelect(Breed("Border Collie", false)))
      val item2 = awaitItem()
      val breeds2 = breeds.map { Breed(it, it == "Border Collie") }
      assertThat(item2).isEqualTo(BreedsUi(breeds2, "Border Collie"))
      expectNoEvents()
    }
  }

  @Test
  fun `Loading data success select border collie after another selection`() = runTest {
    val breeds = listOf("Pug", "Border Collie", "German Shepard", "Golden Retriever")
    val repository = FakeDogRepository()
    repository.breedsCompletable.complete(breeds)
    val useCase = BreedsUseCase(repository)
    moleculeFlow(RecompositionClock.Immediate) { useCase.models() }.test {
      skipItems(2)
      useCase.take(BreedsEvent.OnSelect(Breed("German Shepard", false)))
      val item2 = awaitItem()
      val breeds2 = breeds.map { Breed(it, it == "German Shepard") }
      assertThat(item2).isEqualTo(BreedsUi(breeds2, "German Shepard"))
      useCase.take(BreedsEvent.OnSelect(Breed("Border Collie", false)))
      val item3 = awaitItem()
      val breeds3 = breeds.map { Breed(it, it == "Border Collie") }
      assertThat(item3).isEqualTo(BreedsUi(breeds3, "Border Collie"))
      expectNoEvents()
    }
  }
}
