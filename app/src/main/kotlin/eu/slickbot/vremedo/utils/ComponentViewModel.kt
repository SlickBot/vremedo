package eu.slickbot.vremedo.utils

import androidx.lifecycle.ViewModel

abstract class ComponentViewModel : ViewModel() {

    abstract fun onScreenCreate()
    abstract fun onScreenDispose()

}
