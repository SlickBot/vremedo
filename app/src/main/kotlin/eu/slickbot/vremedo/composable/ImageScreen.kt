package eu.slickbot.vremedo.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.slickbot.vremedo.theme.VremedoTheme

@Composable
fun ImageScreen(
  innerPadding: PaddingValues,
  imageUrls: List<String>,
  isLoading: Boolean,
  modifier: Modifier = Modifier,
  buttonLeft: ImageScreenButton? = null,
  buttonRight: ImageScreenButton? = null,
) {
  val sliderState = rememberAppSliderState("image-controls") {
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
    modifier = modifier
      .fillMaxSize()
      .background(Color.Black.copy(.9f)),
  ) {
    Animation(
      modifier = Modifier.fillMaxSize(),
      painter = animationPainter,
    )
    Controls(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      show = showControls,
      sliderState = sliderState,
      isPlaying = isPlaying,
      painter = animationPainter,
      buttonLeft = buttonLeft,
      buttonRight = buttonRight,
      onPreviousClick = ::onPreviousClick,
      onPlayClick = ::onPlayClick,
      onNextClick = ::onNextClick,
    )
    AppAnimatedVisibility(visible = isLoading) {
      Box(modifier = Modifier.fillMaxSize()) {
        AppCircularLoader(
          modifier = Modifier
            .size(100.dp)
            .align(Alignment.Center),
        )
      }
    }
    buttonLeft?.dialog()
    buttonRight?.dialog()
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
      label = "ImageAnimation",
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
          Box(modifier = Modifier.fillMaxSize()) {
            Text(
              modifier = Modifier.align(Alignment.Center),
              text = "No images",
              style = MaterialTheme.typography.headlineMedium,
            )
          }
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

data class ImageScreenButton(
  val text: String,
  val icon: ImageVector,
  val onClick: () -> Unit,
  val dialog: @Composable () -> Unit,
)

@Composable
private fun Controls(
  modifier: Modifier,
  show: Boolean,
  sliderState: SliderBarState,
  isPlaying: Boolean,
  painter: ImageAnimationPainter,
  buttonLeft: ImageScreenButton?,
  buttonRight: ImageScreenButton?,
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
        AppSlider(
          modifier = Modifier.padding(horizontal = 20.dp),
          state = sliderState,
        )
      }
      Spacer(Modifier.weight(1f))
      ClickableLinearProgressIndicator(
        modifier = Modifier
          .fillMaxWidth()
          .height(16.dp)
          .padding(horizontal = 24.dp),
        currentValue = painter.getIndex(),
        maxValue = painter.imageUrls.lastIndex,
        onValueChange = { painter.setIndex(it) },
      )
      Spacer(Modifier.height(20.dp))
      Row(
        modifier = Modifier.padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        buttonLeft?.let {
          SmallFloatingActionButton(
            modifier = Modifier.padding(horizontal = 10.dp),
            onClick = it.onClick,
          ) {
            Icon(it.icon, it.text)
          }
        }
        Spacer(Modifier.weight(1f))
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
        Spacer(Modifier.weight(1f))
        buttonRight?.let {
          SmallFloatingActionButton(
            modifier = Modifier.padding(horizontal = 10.dp),
            onClick = it.onClick,
          ) {
            Icon(it.icon, it.text)
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun ImageScreenPreview() {
  VremedoTheme {
    AppScaffold {
      ImageScreen(
        innerPadding = PaddingValues(),
        imageUrls = listOf(),
        isLoading = false,
      )
    }
  }
}

