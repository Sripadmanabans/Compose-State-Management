package com.adjectivemonk2.compose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adjectivemonk2.compose.R
import com.adjectivemonk2.compose.presenter.Breed
import com.adjectivemonk2.compose.presenter.BreedsUi
import com.adjectivemonk2.compose.ui.theme.ComposeTheme

@Composable
fun BreedsList(breedsUi: BreedsUi, modifier: Modifier, onBreedClick: (breed: Breed) -> Unit) {
  LazyColumn(modifier = modifier) {
    items(items = breedsUi.breeds, key = { breed -> breed.name }) { breed ->
      BreedItem(breed = breed) { onBreedClick(breed) }
    }
  }
}

@Composable
fun BreedItem(breed: Breed, onBreedClick: () -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onBreedClick)
      .padding(horizontal = 16.dp, vertical = 12.dp),
  ) {
    Text(text = breed.name, modifier = Modifier.weight(1F))
    if (breed.isSelected) {
      Icon(
        painter = painterResource(id = R.drawable.baseline_check_24),
        contentDescription = "Selected",
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun BreedItemSelectedPreview() {
  ComposeTheme {
    BreedItem(Breed("Pug", true)) {}
  }
}

@Preview(showBackground = true)
@Composable
private fun BreedItemUnselectedPreview() {
  ComposeTheme {
    BreedItem(Breed("Pug", false)) {}
  }
}

@Preview
@Composable
private fun BreedsListPreview() {
  val breedsUi = remember {
    BreedsUi(
      listOf(
        Breed("Pug", false),
        Breed("Golden retriever", false),
        Breed("Border collie", true),
        Breed("German Shepard", false),
      ),
      "Border collie",
    )
  }
  Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    BreedsList(breedsUi = breedsUi, modifier = Modifier.fillMaxSize()) {}
  }
}
