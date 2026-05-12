package io.github.visiongem.cryptopulse.feature.markets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.visiongem.cryptopulse.data.repository.MarketsRepository
import io.github.visiongem.cryptopulse.domain.LoadResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MarketsViewModel(
    private val repository: MarketsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MarketsUiState())
    val state: StateFlow<MarketsUiState> = _state.asStateFlow()

    init {
        load(isRefresh = false)
    }

    fun onAction(action: MarketsAction) {
        when (action) {
            MarketsAction.Refresh -> load(isRefresh = true)
            MarketsAction.Retry -> load(isRefresh = false)
        }
    }

    private fun load(isRefresh: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = !isRefresh && it.coins.isEmpty(),
                    isRefreshing = isRefresh,
                    error = null,
                )
            }
            when (val result = repository.getTopMarkets()) {
                is LoadResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            coins = result.data,
                            error = null,
                        )
                    }
                }
                is LoadResult.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.error,
                        )
                    }
                }
            }
        }
    }

    companion object {
        fun factory(repository: MarketsRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { MarketsViewModel(repository) }
            }
    }
}
