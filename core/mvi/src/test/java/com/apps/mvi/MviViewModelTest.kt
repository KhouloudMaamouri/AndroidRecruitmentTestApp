package com.apps.mvi

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test implementation of [MviViewModel] for testing state and intent behavior.
 */
data class TestState(val count: Int = 0, val text: String = "")

sealed interface TestIntent {
    object Increment : TestIntent
    data class SetText(val newText: String) : TestIntent
}

class TestMviViewModel : MviViewModel<TestIntent, TestState>(TestState()) {
    override fun handleIntent(intent: TestIntent) {
        when (intent) {
            TestIntent.Increment -> updateState { copy(count = count + 1) }
            is TestIntent.SetText -> updateState { copy(text = intent.newText) }
        }
    }
}

/**
 * Unit tests for [MviViewModel].
 */
class MviViewModelTest {

    @Test
    fun initialState_isReflectedInStateFlow() {
        val viewModel = TestMviViewModel()
        assertEquals(TestState(count = 0, text = ""), viewModel.state.value)
    }

    @Test
    fun onIntent_updatesStateCorrectly() {
        val viewModel = TestMviViewModel()

        viewModel.onIntent(TestIntent.Increment)
        assertEquals(1, viewModel.state.value.count)

        viewModel.onIntent(TestIntent.SetText("Hello MVI"))
        assertEquals("Hello MVI", viewModel.state.value.text)
    }
}
