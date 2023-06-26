package com.adjectivemonk2.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.adjectivemonk2.compose.presenter.BreedsViewModel
import com.adjectivemonk2.compose.ui.BreedPicUi
import com.adjectivemonk2.compose.ui.BreedsList
import com.adjectivemonk2.compose.ui.theme.ComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<BreedsViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ComposeTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          val breeds by viewModel.uiModel.collectAsState()
          val breedPicUi = breeds.breedPicUi
          if (breedPicUi != null) {
            BreedPicUi(breedPicUi = breedPicUi, viewModel::onRandomClick)
            BackHandler(onBack = viewModel::onBack)
          } else {
            BreedsList(breedsUi = breeds.breedsUi, viewModel::onBreedSelection)
          }
        }
      }
    }
  }
}
