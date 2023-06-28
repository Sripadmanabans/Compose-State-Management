package com.adjectivemonk2.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.adjectivemonk2.compose.presenter.BreedsViewModel
import com.adjectivemonk2.compose.ui.BreedPicUi
import com.adjectivemonk2.compose.ui.BreedsList
import com.adjectivemonk2.compose.ui.theme.ComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<BreedsViewModel>()

  @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ComposeTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          val breeds by viewModel.uiModel.collectAsState()
          val isExpanded = when (calculateWindowSizeClass(this).widthSizeClass) {
            WindowWidthSizeClass.Expanded -> true
            else -> false
          }
          val breedPicUi = breeds.breedPicUi
          if (isExpanded) {
            Row {
              BreedsList(
                breedsUi = breeds.breedsUi,
                modifier = Modifier.weight(1F),
                onBreedClick = viewModel::onBreedSelection,
              )
              if (breedPicUi != null) {
                BreedPicUi(
                  breedPicUi = breedPicUi,
                  modifier = Modifier.weight(2F),
                  viewModel::onRandomClick,
                )
              } else {
                Box(
                  modifier = Modifier
                    .weight(2F)
                    .fillMaxSize(),
                  contentAlignment = Alignment.Center,
                ) {
                  Text(text = "Select Breed")
                }
              }
            }
          } else {
            if (breedPicUi != null) {
              BreedPicUi(
                breedPicUi = breedPicUi,
                modifier = Modifier.fillMaxSize(),
                viewModel::onRandomClick,
              )
              BackHandler(onBack = viewModel::onBack)
            } else {
              BreedsList(
                breedsUi = breeds.breedsUi,
                modifier = Modifier.fillMaxSize(),
                viewModel::onBreedSelection,
              )
            }
          }
        }
      }
    }
  }
}
