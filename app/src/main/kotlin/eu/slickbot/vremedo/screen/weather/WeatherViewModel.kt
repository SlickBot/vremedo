package eu.slickbot.vremedo.screen.weather

import androidx.lifecycle.viewModelScope
import eu.slickbot.provreme.model.ProCity
import eu.slickbot.vremedo.repository.WeatherRepository
import eu.slickbot.vremedo.utils.ComponentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
) : ComponentViewModel() {

    private val _weatherText = MutableStateFlow("")
    val weatherText = _weatherText.asStateFlow()

    private val _cities = MutableStateFlow(emptyList<ProCity>())
    val cities = _cities.asStateFlow()

    private val _filter = MutableStateFlow("")
    val filter = _filter.asStateFlow()

    val filteredCities = combine(_cities, _filter) { cities, filter ->
        when {
            filter.isBlank() -> cities
            else -> withContext(Dispatchers.Default) {
                cities
                    .filter { it.name.contains(filter, ignoreCase = true) }
                    .sortedBy { it.name.indexOf(filter, ignoreCase = true) }
            }
        }
    }

    private val _selectedCity = MutableStateFlow<ProCity?>(null)
    val selectedCity = _selectedCity.asStateFlow()

    private var numberJob: Job? = null
    private var weatherJob: Job? = null
    private var citiesJob: Job? = null

    override fun onComposableCreate() {
        updateWeatherInfo()
        updateCities()
    }

    private fun updateWeatherInfo() {
        weatherJob?.cancel()
        weatherJob = viewModelScope.launch {
            try {
                val data = weatherRepository.getSunInfo("sl", "Novo mesto")
                _weatherText.update {
                    data.joinToString("\n\n") { "${it.sunrise}\n${it.sunset}" }
                }
            } catch (e: Throwable) {
                // TODO: handle
                e.printStackTrace()
            }
        }
    }

    private fun updateCities() {
        citiesJob?.cancel()
        citiesJob = viewModelScope.launch {
            try {
                val cities = weatherRepository.getCities()
                _cities.update { cities }
                _selectedCity.update {
                    cities.find { it.name == "Novo mesto" }
                        ?: cities.firstOrNull()
                }
            } catch (e: Throwable) {
                // TODO: handle
                e.printStackTrace()
            }
        }
    }

    override fun onComposableDispose() {
        numberJob?.cancel()
        weatherJob?.cancel()
        citiesJob?.cancel()
    }

    fun setFilter(filter: String) {
        _filter.update { filter }
    }

    fun onCityClick(city: ProCity) {
        _selectedCity.update { city }
//        _filter.update { city.name }
    }

    fun onSearchOpened() {

    }
    fun onSearchClosed() {

    }

}
