package eu.slickbot.vremedo.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorMessage(
  modifier: Modifier = Modifier,
  message: String = "Something went wrong",
  color: Color = MaterialTheme.colorScheme.onBackground,
  onRetry: (() -> Unit)? = null,
) {
  Box(modifier = modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .align(Alignment.Center)
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = message,
        style = MaterialTheme.typography.headlineMedium,
        color = color,
        textAlign = TextAlign.Center,
      )
      if (onRetry != null) {
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
          )
          Spacer(Modifier.width(8.dp))
          Text("Retry")
        }
      }
    }
  }
}
