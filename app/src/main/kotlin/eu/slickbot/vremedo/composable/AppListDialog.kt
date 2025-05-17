package eu.slickbot.vremedo.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun <T> AppListDialog(
  items: List<T>,
  onItemClick: (T) -> Unit,
  onDismissRequest: () -> Unit,
  isVisible: Boolean,
  modifier: Modifier = Modifier,
  itemText: (T) -> String = { it.toString() },
  itemSelected: (T) -> Boolean = { false },
) {
  ListDialog(
    items = items,
    onDismissRequest = onDismissRequest,
    isVisible = isVisible,
    modifier = modifier,
  ) { item ->
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemClick(item) }
        .padding(12.dp),
      text = itemText(item),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      textAlign = TextAlign.Center,
      fontWeight = if (itemSelected(item)) FontWeight.Black else FontWeight.Normal,
      color = if (itemSelected(item)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
    )
  }
}

@Composable
fun <T> ListDialog(
  items: List<T>,
  onDismissRequest: () -> Unit,
  isVisible: Boolean,
  modifier: Modifier = Modifier,
  content: @Composable LazyItemScope.(T) -> Unit
) {
  if (isVisible) {
    Dialog(
      onDismissRequest = { onDismissRequest() }
    ) {
      Card {
        LazyColumn(
          Modifier
            .padding(vertical = 10.dp)
            .then(modifier)
        ) {
          items(items) { item ->
            content(item)
          }
        }
      }
    }
  }
}
