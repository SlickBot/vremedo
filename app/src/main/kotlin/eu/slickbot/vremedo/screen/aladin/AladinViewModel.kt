package eu.slickbot.vremedo.screen.aladin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.slickbot.arso.model.ArsoAladinMode
import eu.slickbot.arso.model.ArsoAladinScope
import eu.slickbot.vremedo.repository.ArsoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AladinViewModel(
  private val arsoRepo: ArsoRepository,
) : ViewModel() {

  private val _state: MutableStateFlow<AladinState> = MutableStateFlow(AladinState())
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
        arsoRepo.getAladinImages(
          scope = state.value.scope,
          mode = state.value.mode,
        )
      }.fold(
        onSuccess = { imageUrls ->
          _state.update { it.copy(imageUrls = imageUrls) }
        },
        onFailure = {
          Log.e("AladinViewModel", "Failed to load aladin images", it)
          _state.update { it.copy(isError = true) }
        },
      )
      _state.update { it.copy(isLoading = false) }
    }
  }

  fun retry() {
    updateImages()
  }

  fun setScope(scope: ArsoAladinScope) {
    _state.update { it.copy(scope = scope) }
    updateImages()
  }

  fun setMode(mode: ArsoAladinMode) {
    _state.update { it.copy(mode = mode) }
    updateImages()
  }

}
