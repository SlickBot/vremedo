package eu.slickbot.vremedo.screen.cameras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.slickbot.arso.model.ArsoCameraData
import eu.slickbot.arso.model.ArsoCameraLength
import eu.slickbot.arso.model.ArsoCameraOrientation
import eu.slickbot.vremedo.repository.ArsoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class CamerasViewModel(
  private val arsoRepo: ArsoRepository,
) : ViewModel() {

  private val _state = MutableStateFlow(CamerasState())
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
      _state.update { it.copy(isLoading = true, isError = false) }
      runCatching {
        arsoRepo.getCameraData()
      }.fold(
        onSuccess = { imageData ->
          _state.update {
            val selectedImageData = imageData.randomOrNull()
            val orientation = selectedImageData?.orientations?.randomOrNull()
            it.copy(
              cameraData = imageData,
              selectedCameraData = selectedImageData,
              orientation = orientation,
            )
          }
          updateImages()
        },
        onFailure = {
          Timber.e(it, "Failed to load camera data")
          _state.update { it.copy(isError = true) }
        },
      )
      _state.update { it.copy(isLoading = false) }
    }
  }

  private fun updateImages() {
    val selectedImageData = state.value.selectedCameraData ?: return
    val orientation = state.value.orientation ?: return

    updateImageJob?.cancel()
    updateImageJob = viewModelScope.launch {
      _state.update { it.copy(isLoading = true, isError = false) }
      runCatching {
        arsoRepo.getCamerasImages(
          data = selectedImageData,
          orientation = orientation,
          length = state.value.length,
        )
      }.fold(
        onSuccess = { imageUrls ->
          _state.update { it.copy(imageUrls = imageUrls) }
        },
        onFailure = {
          Timber.e(it, "Failed to load camera images")
          _state.update { it.copy(isError = true) }
        },
      )
      _state.update { it.copy(isLoading = false) }
    }
  }

  fun setOrientation(orientation: ArsoCameraOrientation) {
    _state.update { it.copy(orientation = orientation) }
    updateImages()
  }

  fun setCameraData(data: ArsoCameraData) {
    _state.update { it.copy(selectedCameraData = data, orientation = data.orientations.randomOrNull()) }
    updateImages()
  }

  fun setLength(length: ArsoCameraLength) {
    _state.update { it.copy(length = length) }
    updateImages()
  }

}
