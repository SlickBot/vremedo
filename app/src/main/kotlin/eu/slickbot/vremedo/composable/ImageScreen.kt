package eu.slickbot.vremedo.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.slickbot.vremedo.theme.VremedoTheme
import kotlinx.coroutines.launch

private val SPEED_PRESETS = listOf(1, 2, 4, 8, 15)
private const val DEFAULT_SPEED_INDEX = 2 // 4x

@Composable
fun ImageScreen(
  innerPadding: PaddingValues,
  title: String,
  imageUrls: List<String>,
  isLoading: Boolean,
  modifier: Modifier = Modifier,
  buttonLeft: ImageScreenButton? = null,
  buttonRight: ImageScreenButton? = null,
  extraContent: @Composable () -> Unit = {},
) {
  val scope = rememberCoroutineScope()
  val drawerState = rememberDrawerState(DrawerValue.Closed)

  var speedIndex by rememberSaveable { mutableIntStateOf(DEFAULT_SPEED_INDEX) }
  val speed = SPEED_PRESETS[speedIndex]

  var isPlaying by rememberSaveable { mutableStateOf(true) }
  val showControls = imageUrls.size > 1

  val animationPainter = rememberImageAnimationPainter(
    imageUrls = imageUrls,
    isPlaying = isPlaying,
    delay = (1000 / speed).toLong(),
  )

  fun openMenu() {
    scope.launch { drawerState.open() }
  }

  fun onPlayClick() {
    isPlaying = !isPlaying
  }

  fun onNextClick() {
    animationPainter.showNext()
  }

  fun onPreviousClick() {
    animationPainter.showPrevious()
  }

  fun onSpeedClick() {
    speedIndex = (speedIndex + 1) % SPEED_PRESETS.size
  }

  AppDrawer(drawerState, onImageClick = {}) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(Color.Black.copy(.9f)),
    ) {
      Animation(
        modifier = Modifier.fillMaxSize(),
        painter = animationPainter,
      )
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp)
          .align(Alignment.TopCenter)
          .background(
            Brush.verticalGradient(
              listOf(Color.Black.copy(.45f), Color.Transparent),
            ),
          ),
      )
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.TopStart)
          .padding(innerPadding),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        ToolbarIcon(
          imageVector = Icons.Default.Menu,
          contentDescription = "menu",
          onClick = ::openMenu,
        )
        ToolbarTitle(
          modifier = Modifier.weight(1f),
          value = title,
          readOnly = true,
        )
        buttonLeft?.let {
          ToolbarIcon(
            imageVector = it.icon,
            contentDescription = it.text,
            onClick = it.onClick,
          )
        }
        buttonRight?.let {
          ToolbarIcon(
            imageVector = it.icon,
            contentDescription = it.text,
            onClick = it.onClick,
          )
        }
      }
      Controls(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        show = showControls,
        speedLabel = "${speed}×",
        isPlaying = isPlaying,
        painter = animationPainter,
        extraContent = extraContent,
        onSpeedClick = ::onSpeedClick,
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
  speedLabel: String,
  isPlaying: Boolean,
  painter: ImageAnimationPainter,
  extraContent: @Composable () -> Unit,
  onSpeedClick: () -> Unit,
  onPreviousClick: () -> Unit,
  onPlayClick: () -> Unit,
  onNextClick: () -> Unit,
) {
  AppAnimatedVisibility(
    modifier = modifier,
    visible = show,
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      Spacer(Modifier.weight(1f))
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              listOf(Color.Transparent, Color.Black.copy(.62f)),
            ),
          )
          .padding(horizontal = 16.dp)
          .padding(top = 40.dp, bottom = 16.dp),
      ) {
        extraContent()
        Row(verticalAlignment = Alignment.CenterVertically) {
          ControlChip(text = speedLabel, onClick = onSpeedClick)
          Spacer(Modifier.width(12.dp))
          ClickableLinearProgressIndicator(
            modifier = Modifier
              .weight(1f)
              .height(16.dp),
            currentValue = painter.getIndex(),
            maxValue = painter.imageUrls.lastIndex,
            onValueChange = { painter.setIndex(it) },
          )
          Spacer(Modifier.width(12.dp))
          Text(
            text = "${painter.getIndex() + 1} / ${painter.imageUrls.size}",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
          )
        }
        Spacer(Modifier.height(16.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          AppAnimatedVisibility(!isPlaying) {
            SmallFloatingActionButton(
              modifier = Modifier.padding(horizontal = 12.dp),
              onClick = onPreviousClick,
            ) {
              Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Previous")
            }
          }
          FloatingActionButton(
            modifier = Modifier.padding(horizontal = 12.dp),
            onClick = onPlayClick,
          ) {
            AnimatedContent(
              targetState = isPlaying,
              label = "Play/Stop",
            ) { playing ->
              if (playing) {
                Icon(Icons.Default.Pause, "Pause")
              } else {
                Icon(Icons.Default.PlayArrow, "Play")
              }
            }
          }
          AppAnimatedVisibility(!isPlaying) {
            SmallFloatingActionButton(
              modifier = Modifier.padding(horizontal = 12.dp),
              onClick = onNextClick,
            ) {
              Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next")
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ControlChip(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
) {
  Row(
    modifier = modifier
      .clip(RoundedCornerShape(22.dp))
      .background(Color.White.copy(alpha = .15f))
      .clickable(onClick = onClick)
      .padding(horizontal = 14.dp, vertical = 9.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(7.dp),
  ) {
    icon?.let {
      Icon(
        imageVector = it,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier.size(18.dp),
      )
    }
    Text(
      text = text,
      style = MaterialTheme.typography.labelLarge,
      color = Color.White,
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ImageScreenPreview() {
  VremedoTheme {
    AppScaffold {
      ImageScreen(
        innerPadding = PaddingValues(),
        title = "Aladin",
        imageUrls = listOf("a", "b", "c"),
        isLoading = false,
      )
    }
  }
}
