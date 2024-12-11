package eu.slickbot.vremedo.screen.aladin

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.slickbot.vremedo.composable.AppAnimatedVisibility
import eu.slickbot.vremedo.composable.AppCircularLoader
import eu.slickbot.vremedo.composable.AppScaffold
import eu.slickbot.vremedo.composable.ImageAnimationPainter
import eu.slickbot.vremedo.composable.SliderBar
import eu.slickbot.vremedo.composable.SliderBarState
import eu.slickbot.vremedo.composable.ViewModelScaffold
import eu.slickbot.vremedo.composable.ZoomableImage
import eu.slickbot.vremedo.composable.rememberImageAnimationPainter
import eu.slickbot.vremedo.composable.rememberSliderBarState
import eu.slickbot.vremedo.theme.VremedoTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AladinScreen(vm: AladinViewModel = koinViewModel()) {
  val state by vm.state.collectAsStateWithLifecycle()

  AppScaffold { paddingValues ->
    Content(
      imageUrls = state.imageUrls,
      isLoading = state.isLoading,
      paddingValues = paddingValues,
    )
  }
}

@Composable
private fun Content(
  imageUrls: List<String>,
  isLoading: Boolean,
  paddingValues: PaddingValues,
) {
  val sliderState = rememberSliderBarState("image-controls") {
    minValue = 1f
    maxValue = 20f
    value = 5f
  }

  var isPlaying by rememberSaveable { mutableStateOf(true) }
  val showControls by remember(imageUrls) { mutableStateOf(imageUrls.size > 1) }

  val animationPainter = rememberImageAnimationPainter(
    imageUrls = imageUrls,
    isPlaying = isPlaying,
    delay = (1000 / sliderState.value).toLong()
  )

  var showScopesDialog by rememberSaveable { mutableStateOf(false) }
  var showModesDialog by rememberSaveable { mutableStateOf(false) }

  fun onPlayClick() {
    isPlaying = !isPlaying
  }

  fun onNextClick() {
    animationPainter.showNext()
  }

  fun onPreviousClick() {
    animationPainter.showPrevious()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black.copy(.9f)),
  ) {
    Animation(
      modifier = Modifier.fillMaxSize(),
      painter = animationPainter,
    )
    Controls(
      modifier = Modifier
        .fillMaxWidth()
        .padding(paddingValues)
        .align(Alignment.BottomCenter),
      show = showControls,
      sliderState = sliderState,
      isPlaying = isPlaying,
      onPreviousClick = ::onPreviousClick,
      onPlayClick = ::onPlayClick,
      onNextClick = ::onNextClick,
    )
    Loader(
      modifier = Modifier.fillMaxSize(),
      visible = isLoading,
    )
  }
}

@Composable
private fun Animation(
  modifier: Modifier,
  painter: ImageAnimationPainter,
) {
  Box(modifier = modifier) {
    AnimatedContent(
      targetState = painter.state,
      label = "AladinAnimation",
    ) { state ->
      when (state) {
        is ImageAnimationPainter.State.Loading -> {
          Box(modifier = Modifier.fillMaxSize()) {
            AppCircularLoader(
              modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center),
              progress = { state.percentage },
            )
          }
        }
        is ImageAnimationPainter.State.Empty -> {
//          Box(modifier = Modifier.fillMaxSize()) {
//            Text(
//              modifier = Modifier.align(Alignment.Center),
//              text = "No images",
//              style = MaterialTheme.typography.headlineMedium,
//            )
//          }
        }
        is ImageAnimationPainter.State.Error -> {
          Box(modifier = Modifier.fillMaxSize()) {
            Text(
              modifier = Modifier.align(Alignment.Center),
              text = "Failed to fetch images",
              style = MaterialTheme.typography.headlineMedium,
            )
          }
        }
        is ImageAnimationPainter.State.Ready -> {
          ZoomableImage(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = "Image",
            minScale = 1f,
            maxScale = 10f,
            disableRotation = true,
          )
        }
      }
    }
  }
}

@Composable
private fun Controls(
  modifier: Modifier,
  show: Boolean,
  sliderState: SliderBarState,
  isPlaying: Boolean,
  onPreviousClick: () -> Unit,
  onPlayClick: () -> Unit,
  onNextClick: () -> Unit,
) {
  AppAnimatedVisibility(
    modifier = modifier,
    visible = show,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      AppAnimatedVisibility(isPlaying) {
        SliderBar(
          modifier = Modifier.padding(horizontal = 20.dp),
          state = sliderState,
        )
      }

      Row(
        modifier = Modifier.padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        AppAnimatedVisibility(!isPlaying) {
          SmallFloatingActionButton(
            modifier = Modifier.padding(horizontal = 10.dp),
            onClick = onPreviousClick,
          ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Previous")
          }
        }
        FloatingActionButton(
          modifier = Modifier.padding(horizontal = 10.dp),
          onClick = onPlayClick,
        ) {
          AnimatedContent(
            targetState = isPlaying,
            label = "Play/Stop",
          ) { isPlaying ->
            if (isPlaying) {
              Icon(Icons.Default.Pause, "Pause")
            } else {
              Icon(Icons.Default.PlayArrow, "Play")
            }
          }
        }
        AppAnimatedVisibility(!isPlaying) {
          SmallFloatingActionButton(
            modifier = Modifier.padding(horizontal = 10.dp),
            onClick = onNextClick,
          ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next")
          }
        }
      }

//      ClickableLinearProgressIndicator(
//        currentValue = animationPainter.getIndex(),
//        maxValue = imageUrls.lastIndex,
//        onValueChange = { animationPainter.setIndex(it) },
//        modifier = Modifier
//          .fillMaxWidth()
//          .height(10.dp)
//      )
    }
  }
}

@Composable
private fun Loader(
  modifier: Modifier,
  visible: Boolean,
) {
  AppAnimatedVisibility(
    modifier = modifier,
    visible = visible,
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      AppCircularLoader(
        modifier = Modifier
          .size(100.dp)
          .align(Alignment.Center),
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun AladinScreenPreview() {
  VremedoTheme {
    AppScaffold {
      Content(
        imageUrls = listOf(),
        isLoading = false,
        paddingValues = PaddingValues(),
      )
    }
  }
}
