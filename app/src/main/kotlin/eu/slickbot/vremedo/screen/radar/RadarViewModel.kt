package eu.slickbot.vremedo.screen.radar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.slickbot.arso.model.ArsoRadarLength
import eu.slickbot.arso.model.ArsoRadarScope
import eu.slickbot.vremedo.repository.ArsoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RadarViewModel(
  private val arsoRepo: ArsoRepository,
) : ViewModel() {

  private val _state: MutableStateFlow<RadarState> = MutableStateFlow(RadarState())
  val state = _state.asStateFlow()

  private var updateImageJob: Job? = null

  init {
    updateImages()
  }

  private fun updateImages() {
    updateImageJob?.cancel()
    updateImageJob = viewModelScope.launch {
      _state.update { it.copy(isLoading = true) }
      runCatching {
        arsoRepo.getRadarImages(
          length = state.value.length,
          scope = state.value.scope,
        )
      }.fold(
        onSuccess = { imageUrls ->
          _state.update { it.copy(imageUrls = imageUrls) }
        },
        onFailure = {
          // TODO: handle failure
        },
      )
      _state.update { it.copy(isLoading = false) }
    }
  }

  fun setScope(scope: ArsoRadarScope) {
    _state.update { it.copy(scope = scope) }
    updateImages()
  }

  fun setLength(length: ArsoRadarLength) {
    _state.update { it.copy(length = length) }
    updateImages()
  }

}
