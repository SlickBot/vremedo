package eu.slickbot.vremedo.screen.weather

import androidx.lifecycle.viewModelScope
import eu.slickbot.vremedo.utils.ComponentViewModel
import eu.slickbot.vremedo.utils.AppLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(
    lifecycle: AppLifecycle,
) : ComponentViewModel() {

    val lifecycleState = lifecycle.state

    private val _currentNumber = MutableStateFlow(0)
    val currentNumber = _currentNumber.asStateFlow()

    private var numberJob: Job? = null

    override fun onScreenCreate() {
        numberJob?.cancel()
        numberJob = viewModelScope.launch {
            while (true) {
                _currentNumber.update { it + 1 }
                delay(1000)
            }
        }
    }

    override fun onScreenDispose() {
        numberJob?.cancel()
    }

}
