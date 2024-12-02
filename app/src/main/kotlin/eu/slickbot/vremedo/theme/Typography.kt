package eu.slickbot.vremedo.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import eu.slickbot.vremedo.R

val fontAdventPro = FontFamily(
  Font(R.font.advent_pro_extralight),
  Font(R.font.advent_pro_extralight, weight = FontWeight.ExtraLight),
  Font(R.font.advent_pro_medium, weight = FontWeight.Medium),
  Font(R.font.advent_pro_semibold, weight = FontWeight.SemiBold),
  Font(R.font.advent_pro_bold, weight = FontWeight.Bold),
  Font(R.font.advent_pro_black, weight = FontWeight.Black),
)

val Typography = Typography(
  bodyLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
  ),

  displayLarge = TextStyle(
    fontFamily = fontAdventPro,
    fontWeight = FontWeight.SemiBold,
    fontSize = 40.sp,
    lineHeight = 46.sp,
    letterSpacing = 0.5.sp
  ),
  displayMedium = TextStyle(
    fontFamily = fontAdventPro,
    fontWeight = FontWeight.SemiBold,
    fontSize = 34.sp,
    lineHeight = 38.sp,
    letterSpacing = 0.5.sp,
  ),
  displaySmall = TextStyle(
    fontFamily = fontAdventPro,
    fontWeight = FontWeight.Medium,
    fontSize = 26.sp,
    lineHeight = 30.sp,
    letterSpacing = 0.5.sp
  ),

  titleLarge = TextStyle(
    fontFamily = fontAdventPro,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.5.sp
  ),
  titleMedium = TextStyle(
    fontFamily = fontAdventPro,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
  ),
  titleSmall = TextStyle(
    fontFamily = fontAdventPro,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.5.sp
  ),

  labelSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
  ),
)