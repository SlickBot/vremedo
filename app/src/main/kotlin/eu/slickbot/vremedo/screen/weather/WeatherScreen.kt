package eu.slickbot.vremedo.screen.weather

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.layoutId
import eu.slickbot.provreme.model.ProCity
import eu.slickbot.vremedo.composable.BaseScreen
import eu.slickbot.vremedo.composable.FullSizeBox
import eu.slickbot.vremedo.composable.UndecoratedTextField
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WeatherScreen(
    vm: WeatherViewModel = koinViewModel(),
) {
    BaseScreen(vm, fitsSystemWindows = true) {
        var isSearchOpen by remember { mutableStateOf(false) }

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        val filter by filter.collectAsState()
        val filteredCities by filteredCities.collectAsState(emptyList())
        val selectedCity by selectedCity.collectAsState()

        fun openSearch() {
            focusRequester.requestFocus()
            isSearchOpen = true
            onSearchOpened()
        }
        fun closeSearch() {
            focusManager.clearFocus(true)
            isSearchOpen = false
            onSearchClosed()
        }

        BackHandler(isSearchOpen) {
            closeSearch()
        }

        DashboardLayout(
            isSearchOpen = isSearchOpen,
            topLeft = {
                ToolbarIcon(
                    Icons.Default.Menu, "menu",
                    onClick = { println("click") }
                )
            },
            topRight = {
                ToolbarIcon(
                    Icons.Default.Build, "images",
                    onClick =  { println("click") }
                )
            },
            topRight2 = {
                ToolbarIcon(
                    Icons.Default.Close, "close",
                    onClick = { closeSearch() }
                )
            },
            topCenter = {
                ToolbarTitle(
                    value = if (!isSearchOpen) selectedCity?.name.orEmpty() else filter,
                    onValueChange = { setFilter(it) },
                    onFocusChange = { if (it.hasFocus) openSearch() },
                    focusRequester = focusRequester,
                )
            },
            content = {
                Box {
                    AnimatedContent(targetState = isSearchOpen) { isOpen ->
                        when (isOpen) {
                            true -> FullSizeBox(
                                Modifier
//                                    .background(Color.Red.copy(alpha = .3f))
                                    .imePadding()
                            ) {
                                LazyColumn {
                                    items(filteredCities) { city ->
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
                            false -> FullSizeBox(
                                Modifier
//                                    .background(Color.Green.copy(alpha = .3f))
                            )
                        }
                    }
                }
            },
        )
    }
}

@Composable
@OptIn(ExperimentalMotionApi::class)
fun DashboardLayout(
    isSearchOpen: Boolean,
    topLeft: @Composable BoxScope.() -> Unit,
    topRight: @Composable BoxScope.() -> Unit,
    topRight2: @Composable BoxScope.() -> Unit,
    topCenter: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
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
        Box(Modifier.layoutId("topLeft"), content = topLeft)
        Box(Modifier.layoutId("topCenter"), content = topCenter)
        Box(Modifier.layoutId("topRight"), content = topRight)
        Box(Modifier.layoutId("topRight2"), content = topRight2)
        Box(Modifier.layoutId("content"), content = content)
    }
}

private fun startConstraintSet() = ConstraintSet {
    val topLeft = createRefFor("topLeft")
    val topCenter = createRefFor("topCenter")
    val topRight = createRefFor("topRight")
    val topRight2 = createRefFor("topRight2")
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
    val topLeft = createRefFor("topLeft")
    val topCenter = createRefFor("topCenter")
    val topRight = createRefFor("topRight")
    val topRight2 = createRefFor("topRight2")
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
}


@Composable
private fun ToolbarIcon(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(20.dp)
            .size(30.dp)
    )
}

@Composable
private fun ToolbarTitle(
    value: String,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    focusRequester: FocusRequester,
) {
    UndecoratedTextField(
        modifier = Modifier
            .onFocusChanged(onFocusChange)
            .focusRequester(focusRequester),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.headlineSmall,
    )
}

@Composable
private fun CityItem(city: ProCity, onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(20.dp, 12.dp)
    ) {
        Text(city.name)
    }
}
