package eu.slickbot.vremedo.screen.weather

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.layoutId
import eu.slickbot.vremedo.composable.BaseScreen
import eu.slickbot.vremedo.composable.FullSizeBox
import eu.slickbot.vremedo.composable.UndecoratedTextField
import eu.slickbot.vremedo.model.WeatherCity
import eu.slickbot.vremedo.model.WeatherDay
import eu.slickbot.vremedo.model.WeatherItem
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class, ExperimentalMotionApi::class)
@Composable
fun WeatherScreen(
    vm: WeatherViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()

    BaseScreen(vm, fitsSystemWindows = true) {
        var isSearchOpen by remember { mutableStateOf(false) }

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        val filter by searchInput.collectAsState()
        val filteredCities by filteredCities.collectAsState(emptyList())
        val selectedCity by selectedCity.collectAsState()
        val weatherDays by weatherDays.collectAsState()
        val weatherItems by weatherItems.collectAsState()

        fun openSearch() {
            focusRequester.requestFocus()
            isSearchOpen = true
        }
        fun closeSearch() {
            focusManager.clearFocus(true)
            isSearchOpen = false
        }

        fun onDayClick(day: WeatherDay) {
            scope.launch {
                val index = weatherItems.indexOfFirst { it.day == day }
                val percentage = index / weatherItems.lastIndex.toFloat()
            }
        }

        BackHandler(isSearchOpen) {
            closeSearch()
        }

        val progress by animateFloatAsState(
            targetValue = if (isSearchOpen) 1f else 0f,
            animationSpec = tween(350),
        )

        MotionLayout(
            modifier = Modifier.fillMaxSize(),
            start = startConstraintSet(),
            end = endConstraintSet(),
            progress = progress,
        ) {
            ToolbarIcon(
                modifier = Modifier.layoutId("menu"),
                imageVector = Icons.Default.Menu,
                contentDescription = "menu",
                onClick = { println("click") },
            )
            ToolbarIcon(
                modifier = Modifier.layoutId("images"),
                imageVector = Icons.Default.Build,
                contentDescription = "images",
                onClick = { println("click") },
            )
            ToolbarIcon(
                modifier = Modifier.layoutId("close"),
                imageVector = Icons.Default.Close,
                contentDescription = "close",
                onClick = { closeSearch() },
            )
            ToolbarTitle(
                modifier = Modifier.layoutId("title"),
                value = if (!isSearchOpen) selectedCity?.name.orEmpty() else filter,
                onValueChange = { setFilter(it) },
                onFocusChange = { if (it.hasFocus) openSearch() },
                focusRequester = focusRequester,
            )

            AnimatedContent(
                modifier = Modifier.layoutId("content"),
                targetState = isSearchOpen,
            ) { isSearch ->
                when (isSearch) {
                    true -> CitiesContent(
                        filteredCities,
                        closeSearch = ::closeSearch,
                        onCityClick = ::onCityClick,
                    )
                    false -> DashboardContent(
                        weatherDays,
                        onDayClick = ::onDayClick,
                    )
                }
            }
        }
    }
}

private fun startConstraintSet() = ConstraintSet {
    val topLeft = createRefFor("menu")
    val topCenter = createRefFor("title")
    val topRight = createRefFor("images")
    val topRight2 = createRefFor("close")
    val content = createRefFor("content")

    constrain(topLeft) {
        start.linkTo(parent.start)
        top.linkTo(parent.top)
    }
    constrain(topRight) {
        end.linkTo(parent.end)
        top.linkTo(parent.top)
    }
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
        end.linkTo(topRight.start)
    }
    constrain(content) {
        width = Dimension.fillToConstraints
        height = Dimension.fillToConstraints

        top.linkTo(topLeft.bottom)
        bottom.linkTo(parent.bottom)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
    }
}


private fun endConstraintSet() = ConstraintSet {
    val topLeft = createRefFor("menu")
    val topCenter = createRefFor("title")
    val topRight = createRefFor("images")
    val topRight2 = createRefFor("close")
    val content = createRefFor("content")

    constrain(topLeft) {
        end.linkTo(parent.start)
        top.linkTo(parent.top)
    }
    constrain(topRight) {
        start.linkTo(parent.end)
        top.linkTo(parent.top)
    }
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
}


@Composable
private fun ToolbarIcon(
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
private fun ToolbarTitle(
    value: String,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    UndecoratedTextField(
        modifier = Modifier
            .onFocusChanged(onFocusChange)
            .focusRequester(focusRequester)
            .then(modifier),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.headlineSmall,
    )
}

@Composable
private fun CitiesContent(
    cities: List<WeatherCity>,
    closeSearch: () -> Unit,
    onCityClick: (WeatherCity) -> Unit,
) {
    FullSizeBox(
        Modifier
//            .background(Color.Red.copy(alpha = .3f))
            .imePadding()
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
    days: List<WeatherDay>,
    onDayClick: (WeatherDay) -> Unit,
) {
    FullSizeBox {
        DaysBottomNavigation(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            selectedItem = null,
            days = days,
            onDayClick = onDayClick,
        )
    }
}

@Composable
private fun DaysBottomNavigation(
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
//                Image(
//                    rememberAsyncImagePainter(day.iconUrl), null,
//                    modifier = Modifier.size(30.dp),
//                )
                Text(
                    day.name.take(3).toUpperCase(Locale.current),
                    fontWeight = if (day == selectedItem?.day) FontWeight.Bold else FontWeight.Light,
                    fontSize = 12.sp,
                )
            }
        }
    }
}
