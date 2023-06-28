package com.adjectivemonk2.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.adjectivemonk2.compose.presenter.BreedPicUi

@Composable
fun BreedPicUi(breedPicUi: BreedPicUi, modifier: Modifier, onRandomClick: () -> Unit) {
  if (breedPicUi.loading) {
    BreedsPicLoading(modifier)
  } else {
    BreedPicContent(breedPicUi, modifier, onRandomClick)
  }
}

@Composable
private fun BreedPicContent(
  breedPicUi: BreedPicUi,
  modifier: Modifier,
  onRandomClick: () -> Unit,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(24.dp),
    )
    Text(text = checkNotNull(breedPicUi.selectedBreed))
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(24.dp),
    )
    Button(onClick = onRandomClick) {
      Text(text = "Random")
    }
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(24.dp),
    )

    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      AsyncImage(
        model = breedPicUi.imageUrl,
        contentDescription = "Breed",
        modifier = Modifier.fillMaxWidth(),
      )
    }
  }
}

@Composable
private fun BreedsPicLoading(modifier: Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    CircularProgressIndicator(modifier = Modifier.size(48.dp))
  }
}
