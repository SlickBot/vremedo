package eu.slickbot.vremedo.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.slickbot.vremedo.theme.App
import eu.slickbot.vremedo.theme.VremedoTheme
import eu.slickbot.vremedo.theme.appPrimary

@Composable
fun ClickableCard(
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null,
  colors: CardColors = CardDefaults.cardColors(),
  content: @Composable ColumnScope.() -> Unit
) {
  if (onClick != null) {
    Card(
      modifier = modifier,
      onClick = onClick,
      colors = colors,
      content = content,
    )
  } else {
    Card(
      modifier = modifier,
      colors = colors,
      content = content,
    )
  }
}

@Composable
fun WeatherCard(
  imageVector: ImageVector,
  value: String,
  label: String,
  containerColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = .5f),
  modifier: Modifier = Modifier,
  valueTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
  labelTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
  contentDescription: String? = null,
  onClick: (() -> Unit)? = null,
) {
  ClickableCard(
    modifier = modifier,
    onClick = onClick,
    colors = CardDefaults.cardColors(
      containerColor = containerColor,
    ),
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Icon(
        imageVector = imageVector,
        modifier = Modifier.size(30.dp),
        contentDescription = contentDescription,
      )
      Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        Text(
          text = value,
          style = valueTextStyle,
        )
        Text(
          text = label,
          style = labelTextStyle,
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun WeatherCardPreview() {
  VremedoTheme {
    WeatherCard(
      imageVector = Icons.App.Temperature,
      value = "18°C",
      label = "Temperature",
      containerColor = MaterialTheme.colorScheme.appPrimary,
    )
  }
}
