package eu.slickbot.vremedo.screen.cameras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.slickbot.arso.model.ArsoCameraLength
import eu.slickbot.arso.model.ArsoCameraOrientation
import eu.slickbot.vremedo.repository.ArsoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CamerasViewModel(
  private val arsoRepo: ArsoRepository,
) : ViewModel() {

  private val _state: MutableStateFlow<CamerasState> = MutableStateFlow(CamerasState())
  val state = _state.asStateFlow()

  private var updateImageJob: Job? = null
  private var updateDataJob: Job? = null

  init {
    updateData()
//    updateImages()
  }

  private fun updateData() {
    updateDataJob?.cancel()
    updateDataJob = viewModelScope.launch {
      _state.update { it.copy(isLoading = true) }
      runCatching {
        arsoRepo.getCameraData()
      }.fold(
        onSuccess = { imageData ->
          _state.update { it.copy(imageData = imageData, selectedImageData = imageData.firstOrNull()) }
          updateImages()
        },
        onFailure = {
          // TODO: handle failure
        },
      )
      _state.update { it.copy(isLoading = false) }
    }
  }

  private fun updateImages() {
    updateImageJob?.cancel()
    updateImageJob = viewModelScope.launch {
      _state.update { it.copy(isLoading = true) }
      runCatching {
        arsoRepo.getCamerasImages(
          data = state.value.selectedImageData!!,
          orientation = state.value.orientation,
          length = state.value.length,
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

  fun setOrientation(orientation: ArsoCameraOrientation) {
    _state.update { it.copy(orientation = orientation) }
    updateImages()
  }

  fun setLength(length: ArsoCameraLength) {
    _state.update { it.copy(length = length) }
    updateImages()
  }

}
