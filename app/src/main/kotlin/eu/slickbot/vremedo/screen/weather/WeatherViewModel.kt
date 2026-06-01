package eu.slickbot.vremedo.screen.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.slickbot.vremedo.DEFAULT_LOCATION
import eu.slickbot.vremedo.model.WeatherCity
import eu.slickbot.vremedo.model.WeatherDay
import eu.slickbot.vremedo.model.WeatherHours
import eu.slickbot.vremedo.model.WeatherItem
import eu.slickbot.vremedo.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class WeatherViewModel(
  private val weatherRepository: WeatherRepository,
) : ViewModel() {

  enum class Mode {
    DAY, HOURS,
  }

  private var _mode = MutableStateFlow(Mode.HOURS)
  val mode = _mode.asStateFlow()

  private val _searchInput = MutableStateFlow("")
  val searchInput = _searchInput.asStateFlow()

  private val _cities = MutableStateFlow(emptyList<WeatherCity>())
  val cities = _cities.asStateFlow()

  private val _selectedCity = MutableStateFlow<WeatherCity?>(null)
  val selectedCity = _selectedCity.asStateFlow()

  private val _weatherItems = MutableStateFlow(emptyList<WeatherItem>())
  val weatherItems = _weatherItems.asStateFlow()

  private var _weatherDays = MutableStateFlow(emptyList<WeatherDay>())
  val weatherDays = _weatherDays.asStateFlow()

  private var _weatherHours = MutableStateFlow(emptyList<WeatherHours>())
  val weatherHours = _weatherHours.asStateFlow()


  private var _graphTempMin = MutableStateFlow(0f)
  val graphTempMin = _graphTempMin.asStateFlow()

  private var _graphTempMax = MutableStateFlow(0f)
  val graphTempMax = _graphTempMax.asStateFlow()


  private val _isLoadingCities = MutableStateFlow(false)
  val isLoadingCities = _isLoadingCities.asStateFlow()

  private val _isLoadingWeather = MutableStateFlow(false)
  val isLoadingWeather = _isLoadingWeather.asStateFlow()

  private val _isError = MutableStateFlow(false)
  val isError = _isError.asStateFlow()

  val isNight = weatherRepository.isNightFlow(DEFAULT_LOCATION)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), null)

  val filteredCities = combine(_cities, _searchInput) { cities, input ->
    when {
      input.isBlank() -> cities
      else -> withContext(Dispatchers.Default) {
        cities
          .filter { it.name.contains(input, ignoreCase = true) }
          .sortedBy { it.name.indexOf(input, ignoreCase = true) }
      }
    }
  }


  private var citiesJob: Job? = null
  private var weatherItemsJob: Job? = null

  init {
    updateCities()
  }

  fun setFilter(filter: String) {
    _searchInput.update { filter }
  }

  fun onModeChangeClick() {
    _mode.value = when (_mode.value) {
      Mode.DAY -> Mode.HOURS
      Mode.HOURS -> Mode.DAY
    }
  }

  fun onCityClick(city: WeatherCity) {
    _selectedCity.update { city }
    _searchInput.update { "" }
    updateWeatherItems()
  }

  fun retry() {
    if (_cities.value.isEmpty()) {
      updateCities()
    } else {
      updateWeatherItems()
    }
  }

  private fun updateCities() {
    citiesJob?.cancel()
    citiesJob = viewModelScope.launch {
      _isLoadingCities.update { true }
      _isError.update { false }
      try {
        val cities = weatherRepository.getCities()
        _cities.update { cities }
        _selectedCity.update { cities.find { it.name == DEFAULT_LOCATION } ?: cities.firstOrNull() }
        updateWeatherItems()
      } catch (e: Throwable) {
        Timber.e(e, "Failed to load cities")
        _isError.update { true }
      }
      _isLoadingCities.update { false }
    }
  }

  private fun updateWeatherItems() {
    weatherItemsJob?.cancel()
    weatherItemsJob = viewModelScope.launch {
      _isLoadingWeather.update { true }
      _isError.update { false }
      try {
        val cityId = _selectedCity.value?.id!!
        updateWeatherItemsState(emptyList())
        val weatherItems = weatherRepository.getWeatherItems(cityId)
        updateWeatherItemsState(weatherItems)
      } catch (e: Throwable) {
        Timber.e(e, "Failed to load weather")
        _isError.update { true }
      }
      _isLoadingWeather.update { false }
    }
  }

  private suspend fun updateWeatherItemsState(weatherItems: List<WeatherItem>) {
    _weatherItems.update { weatherItems }
    _weatherDays.update {
      withContext(Dispatchers.Default) {
        weatherItems.map { it.day }.distinct()
      }
    }
    _weatherHours.update {
      withContext(Dispatchers.Default) {
        weatherItems.map { it.hours }.distinct()
      }
    }
    _graphTempMin.update {
      withContext(Dispatchers.Default) {
        weatherItems.minOfOrNull {
          it.hours.temperature ?: Float.MAX_VALUE
        }?.let { it - 10 } ?: -20f
      }
    }
    _graphTempMax.update {
      withContext(Dispatchers.Default) {
        weatherItems.maxOfOrNull {
          it.hours.temperature ?: Float.MIN_VALUE
        }?.let { it + 10 } ?: 40f
      }
    }
  }

}
