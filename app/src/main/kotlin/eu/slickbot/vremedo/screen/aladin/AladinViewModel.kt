package eu.slickbot.vremedo.screen.aladin

import androidx.lifecycle.viewModelScope
import eu.slickbot.vremedo.repository.ArsoRepository
import eu.slickbot.vremedo.utils.ComponentViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AladinViewModel(
  private val arsoRepo: ArsoRepository,
) : ComponentViewModel() {

  private val _state: MutableStateFlow<AladinState> = MutableStateFlow(AladinState())
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
        arsoRepo.getAladinImages(
          scope = state.value.scope,
          mode = state.value.mode,
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

}
