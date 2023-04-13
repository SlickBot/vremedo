package eu.slickbot.vremedo.utils

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppLifecycle : LifecycleEventObserver {

    private val _state = MutableStateFlow(Lifecycle.State.INITIALIZED)
    val state = _state.asStateFlow()

    fun bind(activity: ComponentActivity) {
        activity.lifecycle.addObserver(this)
        changeState(activity.lifecycle.currentState)
    }
    fun unbind(activity: ComponentActivity) {
        activity.lifecycle.removeObserver(this)
        changeState(activity.lifecycle.currentState)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        changeState(event.targetState)
    }

    private fun changeState(state: Lifecycle.State) {
        if (_state.value != state) {
            _state.value = state
        }
    }

}
