package eu.slickbot.vremedo.composable

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.slickbot.vremedo.theme.VremedoTheme

@Composable
fun AppCircularLoader(
  modifier: Modifier = Modifier,
  progress: (() -> Float)? = null,
) {
  if (progress != null) {
    CircularProgressIndicator(
      modifier = modifier,
      progress = progress,
    )
  } else {
    CircularProgressIndicator(
      modifier = modifier,
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun AppCircularLoaderPreview() {
  VremedoTheme {
    AppCircularLoader(
      progress = { .7f }
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun AppCircularLoaderIndefinitePreview() {
  VremedoTheme {
    AppCircularLoader()
  }
}
