package eu.slickbot.vremedo.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.slickbot.vremedo.theme.VremedoTheme

@Composable
fun DrawerScaffold(
  modifier: Modifier = Modifier,
  paddingValues: PaddingValues = PaddingValues(),
  header: (@Composable ColumnScope.() -> Unit)? = null,
  footer: (@Composable ColumnScope.() -> Unit)? = null,
  content: @Composable ColumnScope.() -> Unit,
) {
  ModalDrawerSheet(
    modifier = modifier.padding(paddingValues),
  ) {
    header?.invoke(this)
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState()),
      content = content,
    )
    footer?.invoke(this)
  }
}

@Composable
fun DrawerSectionLabel(
  text: String,
  modifier: Modifier = Modifier,
) {
  Text(
    modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
    text = text,
    style = MaterialTheme.typography.labelSmall,
    color = MaterialTheme.colorScheme.primary,
    letterSpacing = 1.sp,
  )
}

@Composable
fun DrawerScreenItem(
  icon: ImageVector,
  label: String,
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val contentColor = if (selected) {
    MaterialTheme.colorScheme.primary
  } else {
    MaterialTheme.colorScheme.onSurface
  }
  Row(
    modifier = modifier
      .fillMaxWidth()
      .then(
        if (selected) {
          Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
        } else {
          Modifier
        }
      )
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 14.dp),
    horizontalArrangement = Arrangement.spacedBy(20.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = contentColor,
    )
    Text(
      text = label,
      style = MaterialTheme.typography.bodyLarge,
      color = contentColor,
    )
  }
}

@Composable
fun DrawerSourceItem(
  name: String,
  description: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = name,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
      )
      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
    Icon(
      imageVector = Icons.AutoMirrored.Filled.OpenInNew,
      contentDescription = "Open $name website",
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.size(18.dp),
    )
  }
}

@Preview(showBackground = true, heightDp = 420)
@Composable
private fun DrawerScaffoldPreview() {
  VremedoTheme {
    DrawerScaffold(
      header = {
        Icon(
          imageVector = Icons.Outlined.WbSunny,
          contentDescription = null,
          modifier = Modifier
            .padding(start = 16.dp, top = 24.dp)
            .size(48.dp),
          tint = MaterialTheme.colorScheme.primary,
        )
        Text(
          modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp),
          text = "Vremedo",
          style = MaterialTheme.typography.headlineLarge,
        )
      },
      footer = {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
      },
    ) {
      DrawerSectionLabel(text = "FORECAST")
      DrawerScreenItem(
        icon = Icons.Outlined.WbSunny,
        label = "Weather",
        selected = true,
        onClick = {},
      )
      DrawerScreenItem(
        icon = Icons.Outlined.Radar,
        label = "Radar",
        selected = false,
        onClick = {},
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun DrawerScreenItemPreview() {
  VremedoTheme {
    Column {
      DrawerScreenItem(
        icon = Icons.Outlined.WbSunny,
        label = "Weather",
        selected = true,
        onClick = {},
      )
      DrawerScreenItem(
        icon = Icons.Outlined.Radar,
        label = "Radar",
        selected = false,
        onClick = {},
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun DrawerSourceItemPreview() {
  VremedoTheme {
    DrawerSourceItem(
      name = "ARSO",
      description = "Slovenian Environmental Agency",
      onClick = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun DrawerSectionLabelPreview() {
  VremedoTheme {
    DrawerSectionLabel(text = "FORECAST")
  }
}
