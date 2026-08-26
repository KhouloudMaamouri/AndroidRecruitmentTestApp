package com.apps.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Abstract base class for ViewModels implementing Model-View-Intent (MVI) architecture.
 *
 * @param Intent Sealed interface or type representing user actions/intents.
 * @param State Data class representing immutable UI state.
 * @param initialState Initial UI state object.
 */
abstract class MviViewModel<Intent, State>(
    initialState: State
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    /** Read-only [StateFlow] exposing current UI state to observers. */
    val state: StateFlow<State> = _state.asStateFlow()

    /**
     * Entry point for dispatching user intents to the ViewModel.
     *
     * @param intent The [Intent] action to process.
     */
    fun onIntent(intent: Intent) {
        handleIntent(intent)
    }

    /**
     * Abstract method implemented by subclasses to process incoming intents.
     *
     * @param intent The [Intent] action to handle.
     */
    protected abstract fun handleIntent(intent: Intent)

    /**
     * Updates current UI state using a reducer transformation lambda.
     *
     * @param reducer State transformation lambda taking current state and returning new state.
     */
    protected fun updateState(
        reducer: State.() -> State
    ) {
        _state.update { currentState ->
            currentState.reducer()
        }
    }
}