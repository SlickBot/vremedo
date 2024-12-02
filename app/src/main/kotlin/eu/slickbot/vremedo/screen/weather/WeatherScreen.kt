package eu.slickbot.vremedo.screen.weather

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.layoutId
import coil.compose.rememberAsyncImagePainter
import eu.slickbot.vremedo.composable.AppDrawer
import eu.slickbot.vremedo.composable.ClickableCard
import eu.slickbot.vremedo.composable.EasterEgg
import eu.slickbot.vremedo.composable.Loader
import eu.slickbot.vremedo.composable.ToolbarIcon
import eu.slickbot.vremedo.composable.ToolbarTitle
import eu.slickbot.vremedo.composable.ViewModelScaffold
import eu.slickbot.vremedo.composable.WeatherCard
import eu.slickbot.vremedo.composable.WeatherGraph
import eu.slickbot.vremedo.composable.WeatherGraphState
import eu.slickbot.vremedo.composable.keyboardOnlyPadding
import eu.slickbot.vremedo.composable.rememberFocusRequester
import eu.slickbot.vremedo.composable.rememberWeatherGraphState
import eu.slickbot.vremedo.extension.localDateTimeNow
import eu.slickbot.vremedo.extension.toInstant
import eu.slickbot.vremedo.model.WeatherAttribute
import eu.slickbot.vremedo.model.WeatherCity
import eu.slickbot.vremedo.model.WeatherDay
import eu.slickbot.vremedo.model.WeatherHours
import eu.slickbot.vremedo.model.WeatherItem
import eu.slickbot.vremedo.theme.App
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMotionApi::class)
@Composable
fun WeatherScreen(
  vm: WeatherViewModel = koinViewModel(),
) {
  ViewModelScaffold(vm) { paddingValues ->
    val scope = rememberCoroutineScope()
    val graphState = rememberWeatherGraphState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var isSearchOpen by remember { mutableStateOf(false) }
    var isEasterEggOpen by remember { mutableStateOf(false) }

    val focusRequester = rememberFocusRequester()
    val focusManager = LocalFocusManager.current

    val filter by vm.searchInput.collectAsState()
    val filteredCities by vm.filteredCities.collectAsState(emptyList())
    val mode by vm.mode.collectAsState()
    val selectedCity by vm.selectedCity.collectAsState()
    val weatherItems by vm.weatherItems.collectAsState()
    val weatherDays by vm.weatherDays.collectAsState()
    val graphTempMin by vm.graphTempMin.collectAsState()
    val graphTempMax by vm.graphTempMax.collectAsState()
    val isLoadingCities by vm.isLoadingCities.collectAsState()
    val isLoadingWeather by vm.isLoadingWeather.collectAsState()
    val isNight by vm.isNight.collectAsState()

    val selectedItem = remember(weatherItems, graphState.currentIndex) {
      weatherItems.getOrNull(graphState.currentIndex)
    }

    LaunchedEffect(weatherItems) {
      if (weatherItems.isEmpty()) return@LaunchedEffect

      delay(1)

      val lastItemTime = weatherItems.last().dateTime.toInstant()
      val firstItemTime = weatherItems.first().dateTime.toInstant()
      val nowTime = localDateTimeNow().toInstant()

      val totalDuration = lastItemTime.epochSeconds - firstItemTime.epochSeconds
      val partDuration = nowTime.epochSeconds - firstItemTime.epochSeconds

      if (totalDuration > 0 && partDuration > 0) {
        graphState.scrollTo(partDuration / totalDuration.toFloat())
      }
    }

    fun openMenu() {
      scope.launch { drawerState.open() }
    }

    fun closeMenu() {
      scope.launch { drawerState.close() }
    }

    fun openSearch() {
      isSearchOpen = true
    }

    fun closeSearch() {
      if (isSearchOpen) {
        focusManager.clearFocus(true)
        isSearchOpen = false
      }
    }

    fun showEasterEgg() {
      isEasterEggOpen = true
    }

    fun hideEasterEgg() {
      isEasterEggOpen = false
    }

    fun onDayClick(day: WeatherDay) {
      scope.launch {
        val firstIdx = weatherItems.indexOfFirst { it.day == day }
        val lastIdx = weatherItems.indexOfLast { it.day == day } + 1
        val percentage = (lastIdx + firstIdx) / 2f / weatherItems.lastIndex.toFloat()
        graphState.animateScrollTo(percentage * 1.001f)
      }
    }

    BackHandler(enabled = isSearchOpen || drawerState.isOpen) {
      if (drawerState.isOpen) {
        scope.launch { drawerState.close() }
      } else {
        closeSearch()
      }
    }

    val searchOpenProgress by animateFloatAsState(
      targetValue = if (isSearchOpen) 1f else 0f,
      animationSpec = tween(250),
      label = "search_progress",
    )

    Box {
      AppDrawer(drawerState) {
        MotionLayout(
          modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
          start = startConstraintSet(),
          end = endConstraintSet(),
          progress = searchOpenProgress,
        ) {
          ToolbarIcon(
            modifier = Modifier.layoutId("menu"),
            imageVector = Icons.Default.Menu,
            contentDescription = "menu",
            onClick = { openMenu() },
            onLongClick = { showEasterEgg() },
          )
//          ToolbarIcon(
//            modifier = Modifier.layoutId("images"),
//            imageVector = Icons.Default.Build,
//            contentDescription = "images",
//            onClick = { println("click") },
//          )
          ToolbarIcon(
            modifier = Modifier.layoutId("close"),
            imageVector = Icons.Default.Close,
            contentDescription = "close",
            onClick = { closeSearch() },
          )
          ToolbarTitle(
            modifier = Modifier.layoutId("title"),
            value = if (!isSearchOpen) selectedCity?.name.orEmpty() else filter,
            onValueChange = { vm.setFilter(it) },
            onFocusChange = { if (it.hasFocus) openSearch() },
            focusRequester = focusRequester,
          )

          AnimatedContent(
            modifier = Modifier.layoutId("content"),
            targetState = isSearchOpen,
            label = "weather_content",
          ) { isSearch ->
            if (isSearch) SearchContent(
              filteredCities,
              closeSearch = ::closeSearch,
              onCityClick = vm::onCityClick,
            )
            else DashboardContent(
              weatherItems = weatherItems,
              selectedItem = selectedItem,
              days = weatherDays,
              graphState = graphState,
              graphMin = graphTempMin,
              graphMax = graphTempMax,
              onDayClick = ::onDayClick,
            )
          }

          Loader(
            modifier = Modifier.layoutId("loader"),
            show = isLoadingCities || isLoadingWeather,
            isDarkMode = isNight == true,
          )
        }
      }
      EasterEgg(
        show = isEasterEggOpen,
        onHide = ::hideEasterEgg,
      )
    }
  }
}

private fun startConstraintSet() = ConstraintSet {
  val topLeft = createRefFor("menu")
  val topCenter = createRefFor("title")
//  val topRight = createRefFor("images")
  val topRight2 = createRefFor("close")
  val content = createRefFor("content")
  val loader = createRefFor("loader")

  constrain(topLeft) {
    start.linkTo(parent.start)
    top.linkTo(parent.top)
  }
//  constrain(topRight) {
//    end.linkTo(parent.end)
//    top.linkTo(parent.top)
//  }
  constrain(topRight2) {
    start.linkTo(parent.end)
    top.linkTo(parent.top)
  }
  constrain(topCenter) {
    width = Dimension.fillToConstraints
    height = Dimension.fillToConstraints

    top.linkTo(parent.top)
    bottom.linkTo(topLeft.bottom)
    start.linkTo(topLeft.end)
    end.linkTo(parent.end)
  }
  constrain(content) {
    width = Dimension.fillToConstraints
    height = Dimension.fillToConstraints

    top.linkTo(topLeft.bottom)
    bottom.linkTo(parent.bottom)
    start.linkTo(parent.start)
    end.linkTo(parent.end)
  }
  constrain(loader) {
    width = Dimension.fillToConstraints
    height = Dimension.fillToConstraints

    top.linkTo(parent.top)
    bottom.linkTo(parent.bottom)
    start.linkTo(parent.start)
    end.linkTo(parent.end)
  }
}

private fun endConstraintSet() = ConstraintSet {
  val topLeft = createRefFor("menu")
  val topCenter = createRefFor("title")
//  val topRight = createRefFor("images")
  val topRight2 = createRefFor("close")
  val content = createRefFor("content")
  val loader = createRefFor("loader")

  constrain(topLeft) {
    end.linkTo(parent.start)
    top.linkTo(parent.top)
  }
//  constrain(topRight) {
//    start.linkTo(parent.end)
//    top.linkTo(parent.top)
//  }
  constrain(topRight2) {
    end.linkTo(parent.end)
    top.linkTo(parent.top)
  }
  constrain(topCenter) {
    width = Dimension.fillToConstraints
    height = Dimension.fillToConstraints

    top.linkTo(parent.top)
    bottom.linkTo(topLeft.bottom)
    start.linkTo(parent.start)
    end.linkTo(topRight2.start)
  }
  constrain(content) {
    width = Dimension.fillToConstraints
    height = Dimension.fillToConstraints

    top.linkTo(topLeft.bottom)
    bottom.linkTo(parent.bottom)
    start.linkTo(parent.start)
    end.linkTo(parent.end)
  }
  constrain(loader) {
    width = Dimension.fillToConstraints
    height = Dimension.fillToConstraints

    top.linkTo(parent.top)
    bottom.linkTo(parent.bottom)
    start.linkTo(parent.start)
    end.linkTo(parent.end)
  }
}

@Composable
private fun SearchContent(
  cities: List<WeatherCity>,
  closeSearch: () -> Unit,
  onCityClick: (WeatherCity) -> Unit,
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .keyboardOnlyPadding()
  ) {
    LazyColumn {
      items(cities) { city ->
        CityItem(
          city = city,
          onClick = {
            closeSearch()
            onCityClick(city)
          },
        )
      }
    }
  }
}

@Composable
private fun CityItem(city: WeatherCity, onClick: () -> Unit) {
  Box(
    Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(20.dp, 12.dp)
  ) {
    Text(city.name)
  }
}

@Composable
private fun DashboardContent(
  weatherItems: List<WeatherItem>,
  selectedItem: WeatherItem? = null,
  days: List<WeatherDay>,
  graphState: WeatherGraphState,
  graphMin: Float,
  graphMax: Float,
  onDayClick: (WeatherDay) -> Unit,
) {
  Column(
    Modifier
      .fillMaxSize()
      .padding(top = 10.dp)
  ) {
    WeatherDayHeader(
      modifier = Modifier.fillMaxWidth(),
      item = selectedItem,
    )
//    WeatherHoursHeader(
//      modifier = Modifier.fillMaxWidth(),
//      item = selectedItem,
//    )
    WeatherContent(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      item = selectedItem,
    )
    WeatherGraph(
      modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(bottom = 20.dp, start = 0.dp, end = 0.dp),
      items = weatherItems,
      state = graphState,
      valueMin = graphMin,
      valueMax = graphMax,
      weatherImageSize = DpSize(22.dp, 22.dp),
      windImageSize = DpSize(16.dp, 16.dp),
      paddingValues = PaddingValues(
        start = LocalConfiguration.current.screenWidthDp.dp / 2,
        end = LocalConfiguration.current.screenWidthDp.dp / 2,
      ),
      lineOffset = LocalConfiguration.current.screenWidthDp.dp / 2,
      itemSpacing = 14.dp,
    )
    WeatherDaysNavbar(
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
      selectedItem = selectedItem,
      days = days,
      onDayClick = onDayClick,
    )
  }
}

@Composable
fun WeatherContent(
  item: WeatherItem?,
  modifier: Modifier = Modifier,
) {
  if (item == null) return

//  Card(
//    modifier = Modifier
//      .padding(top = 20.dp, start = 10.dp, end = 10.dp)
//      .then(modifier),
//    colors = CardDefaults.cardColors(
//      containerColor = NavigationRailDefaults.ContainerColor.copy(alpha = .3f)
//    )
//  ) {
//    LazyVerticalGrid(
//      columns = GridCells.Adaptive(minSize = 150.dp),
//      contentPadding = PaddingValues(10.dp)
//    ) {
//      items(item.hours.dataList) { item ->
//        Column(
//          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
//        ) {
//          Text(
//            item.title,
//            fontSize = 12.sp,
//            lineHeight = 13.sp,
//          )
//          Text(
//            item.text,
//            fontSize = 15.sp,
//            lineHeight = 16.sp,
//          )
//        }
//      }
//    }
//  }
//  return

  val weatherAttributes = remember {
    listOf(
      WeatherAttribute(Icons.App.Temperature, item.hours.temperatureText, "Temperature"),
      WeatherAttribute(Icons.App.Humidity, item.hours.humidityText, "Humidity"),
      WeatherAttribute(Icons.App.Wind, item.hours.windSpeedText, "Wind speed"),
      WeatherAttribute(Icons.App.Direction, item.hours.windDirectionText, "Wind direction"),
      WeatherAttribute(Icons.App.Gauge, item.hours.pressureText, "Pressure"),
      WeatherAttribute(Icons.App.Rain, item.hours.rainText, "Rain"),
      WeatherAttribute(Icons.App.Snow, item.hours.snowText, "Snow"),
      WeatherAttribute(Icons.App.Visibility, item.hours.visibilityText, "Visibility")
    )
  }

  LazyVerticalGrid(
    modifier = modifier.padding(horizontal = 16.dp).padding(top = 20.dp),
    columns = GridCells.Fixed(2),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    hoursCard(item.hours)
    weatherAttributes.forEach { attribute ->
      attribute.value?.let {
        weatherCard(attribute)
      }
    }
  }
}

private fun LazyGridScope.hoursCard(
  hours: WeatherHours,
) {
  item(span = { GridItemSpan(2) }) {
    ClickableCard(
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = .5f),
      ),
    ) {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        text = hours.name,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.displayMedium,
      )
    }
  }
}

private fun LazyGridScope.weatherCard(
  attribute: WeatherAttribute,
) {
  item {
    WeatherCard(
      imageVector = attribute.imageVector,
      value = attribute.value ?: "-",
      label = attribute.label,
    )
  }
}

@Composable
private fun WeatherDayHeader(
  modifier: Modifier,
  item: WeatherItem?,
) {
  if (item == null)
    return

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Column {
      Text(
        item.day.name,
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier
          .padding(horizontal = 20.dp),
      )
    }

    Spacer(modifier = Modifier.weight(1f))

    val temperatureText = remember(item) {
      val minTemp = item.day.minTemperatureText
      val maxTemp = item.day.maxTemperatureText
      "$minTemp / $maxTemp"
    }
    Text(
      temperatureText,
      style = MaterialTheme.typography.displayMedium,
      modifier = Modifier
        .padding(horizontal = 20.dp),
    )

    val weatherIcon = remember(item) {
      item.day.iconUrl
    }
    Image(
      painter = rememberAsyncImagePainter(weatherIcon),
      contentDescription = "Weather icon",
      modifier = Modifier
        .size(60.dp)
        .scale(1.3f)
        .padding(end = 20.dp)
    )
  }
}

@Composable
private fun WeatherHoursHeader(
  modifier: Modifier,
  item: WeatherItem?,
) {
  if (item == null)
    return

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      item.hours.name,
      style = MaterialTheme.typography.displaySmall,
      modifier = Modifier
        .padding(horizontal = 20.dp),
    )
  }
}

@Composable
private fun WeatherDaysNavbar(
  modifier: Modifier,
  selectedItem: WeatherItem?,
  days: List<WeatherDay>,
  onDayClick: (WeatherDay) -> Unit,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.SpaceEvenly,
  ) {
    for (day in days) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .clickable { onDayClick(day) }
          .weight(1f)
      ) {
        Image(
          rememberAsyncImagePainter(day.iconUrl), null,
          modifier = Modifier.size(30.dp),
        )
        Text(
          day.name.take(3).toUpperCase(Locale.current),
          fontWeight = if (day == selectedItem?.day) FontWeight.Bold else FontWeight.Light,
          fontSize = 12.sp,
        )
      }
    }
  }
}
