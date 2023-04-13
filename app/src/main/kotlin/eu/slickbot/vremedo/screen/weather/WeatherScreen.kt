package eu.slickbot.vremedo.screen.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.slickbot.vremedo.composable.ViewModelEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(
    vm: WeatherViewModel = koinViewModel(),
) {
    ViewModelEffect(vm)

    val currentNumber by vm.currentNumber.collectAsState()
    val lifecycleState by vm.lifecycleState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Hello $currentNumber $lifecycleState!",
        )
    }
}
