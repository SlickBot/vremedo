package eu.slickbot.vremedo.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.slickbot.vremedo.extension.runIf
import eu.slickbot.vremedo.theme.VremedoTheme
import eu.slickbot.vremedo.theme.toolbarTitle

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
      readOnly = true,
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToolbarIcon(
  imageVector: ImageVector,
  contentDescription: String,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  onLongClick: (() -> Unit)? = null,
) {
  Icon(
    imageVector = imageVector,
    contentDescription = contentDescription,
    modifier = Modifier
      .combinedClickable(
        onClick = onClick,
        onLongClick = onLongClick,
      )
      .padding(20.dp)
      .size(30.dp)
      .then(modifier)
  )
}

@Composable
fun ToolbarTitle(
  value: String,
  readOnly: Boolean = false,
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
    textStyle = MaterialTheme.typography.toolbarTitle,
    readOnly = readOnly,
  )
}

@Preview(showBackground = true)
@Composable
private fun AppBarPreview() {
  VremedoTheme {
    AppBar(
      title = "Title",
      onMenuClick = {},
    )
  }
}
