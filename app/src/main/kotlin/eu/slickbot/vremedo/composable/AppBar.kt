package eu.slickbot.vremedo.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import eu.slickbot.vremedo.extension.runIf

@Composable
fun AppBar(
  title: String,
  onMenuClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth()
  ) {
    ToolbarIcon(
      imageVector = Icons.Default.Menu,
      contentDescription = "menu",
      onClick = onMenuClick,
    )
    ToolbarTitle(
      value = title,
    )
  }
}

@Composable
fun ToolbarIcon(
  imageVector: ImageVector,
  contentDescription: String,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  Icon(
    imageVector = imageVector,
    contentDescription = contentDescription,
    modifier = Modifier
      .clickable(onClick = onClick)
      .padding(20.dp)
      .size(30.dp)
      .then(modifier)
  )
}

@Composable
fun ToolbarTitle(
  value: String,
  readOnly: Boolean = true,
  focusRequester: FocusRequester? = null,
  onValueChange: (String) -> Unit = {},
  onFocusChange: (FocusState) -> Unit = {},
  modifier: Modifier = Modifier,
) {
  UndecoratedTextField(
    modifier = Modifier
      .onFocusChanged(onFocusChange)
      .runIf(focusRequester != null) {
        focusRequester(focusRequester!!)
      }
      .then(modifier),
    value = value,
    onValueChange = onValueChange,
    singleLine = true,
    textStyle = MaterialTheme.typography.displayMedium,
    readOnly = readOnly,
  )
}
