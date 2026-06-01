package eu.slickbot.vremedo.screen.satellite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.slickbot.arso.model.ArsoSatelliteLength
import eu.slickbot.arso.model.ArsoSatelliteScope
import eu.slickbot.vremedo.repository.ArsoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class SatelliteViewModel(
  private val arsoRepo: ArsoRepository,
) : ViewModel() {

  private val _state = MutableStateFlow(SatelliteState())
  val state = _state.asStateFlow()

  private var updateImageJob: Job? = null

  init {
    updateImages()
  }

  private fun updateImages() {
    updateImageJob?.cancel()
    updateImageJob = viewModelScope.launch {
      _state.update { it.copy(isLoading = true, isError = false) }
      runCatching {
        arsoRepo.getSatelliteImages(
          scope = state.value.scope,
          length = state.value.length,
        )
      }.fold(
        onSuccess = { imageUrls ->
          _state.update { it.copy(imageUrls = imageUrls) }
        },
        onFailure = {
          Timber.e(it, "Failed to load satellite images")
          _state.update { it.copy(isError = true) }
        },
      )
      _state.update { it.copy(isLoading = false) }
    }
  }

  fun setScope(scope: ArsoSatelliteScope) {
    _state.update { it.copy(scope = scope) }
    updateImages()
  }

  fun setMode(length: ArsoSatelliteLength) {
    _state.update { it.copy(length = length) }
    updateImages()
  }

}
