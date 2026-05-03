package com.products.app.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.products.app.data.model.Product
import com.products.app.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    sealed class ProductListState {
        data object Loading : ProductListState()
        data class Success(val products: List<Product>) : ProductListState()
        data class Error(val message: String) : ProductListState()
    }

    private val _state = MutableStateFlow<ProductListState>(ProductListState.Loading)
    val state: StateFlow<ProductListState> = _state.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val searchQuery = MutableStateFlow("")

    val filteredProducts: StateFlow<List<Product>> = combine(_state, searchQuery) { state, query ->
        val products = (state as? ProductListState.Success)?.products ?: emptyList()
        if (query.isBlank()) {
            products
        } else {
            products.filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _state.value = ProductListState.Loading
            try {
                repository.getProducts().collect { result ->
                    result.fold(
                        onSuccess = { products ->
                            _state.value = ProductListState.Success(products)
                        },
                        onFailure = { throwable ->
                            Log.e(TAG, "loadProducts failed", throwable)
                            _state.value = ProductListState.Error(throwable.toUserMessage())
                        },
                    )
                }
            } catch (throwable: Throwable) {
                Log.e(TAG, "loadProducts crashed", throwable)
                _state.value = ProductListState.Error(throwable.toUserMessage())
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                repository.getProducts().collect { result ->
                    result.fold(
                        onSuccess = { products ->
                            _state.value = ProductListState.Success(products)
                        },
                        onFailure = { throwable ->
                            Log.e(TAG, "refresh failed", throwable)
                            _state.value = ProductListState.Error(throwable.toUserMessage())
                        },
                    )
                }
            } catch (throwable: Throwable) {
                Log.e(TAG, "refresh crashed", throwable)
                _state.value = ProductListState.Error(throwable.toUserMessage())
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun updateSearch(query: String) {
        searchQuery.value = query
    }

    private fun Throwable.toUserMessage(): String {
        return if (this is IOException) {
            "Couldn't load products. Check your connection."
        } else {
            "Something went wrong. Please try again."
        }
    }

    private companion object {
        const val TAG = "ProductApp"
    }
}
