package com.asaad27.ui.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

open class ViewModel(protected val viewModelScope: CoroutineScope) {

    /**
     * Cancels the CoroutineScope when the ViewModel is cleared.
     */
    open fun clear() {
        viewModelScope.cancel()
    }
}