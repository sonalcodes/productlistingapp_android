package com.products.app.ui.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.products.app.R
import com.products.app.data.model.Product
import com.products.app.data.repository.ProductRepository
import com.products.app.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val repository: ProductRepository,
) : ViewModel() {

    sealed class ProductDetailState {
        data object Loading : ProductDetailState()
        data class Success(val product: Product) : ProductDetailState()
        data class Error(val message: String) : ProductDetailState()
    }

    private val _uiState = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val uiState: StateFlow<ProductDetailState> = _uiState.asStateFlow()

    private val productId: Int = savedStateHandle.get<Int>(Screen.ARG_PRODUCT_ID) ?: -1

    init {
        if (productId > 0) {
            loadProduct()
        } else {
            _uiState.value = ProductDetailState.Error(context.getString(R.string.error_generic_product_detail))
            Log.e(TAG, "Invalid product id from navigation arguments")
        }
    }

    fun loadProduct() {
        if (productId <= 0) return
        viewModelScope.launch {
            try {
                _uiState.value = ProductDetailState.Loading
                repository.getProduct(productId)
                    .catch { throwable ->
                        Log.e(TAG, "getProduct flow failed", throwable)
                        _uiState.value = ProductDetailState.Error(mapProductError(throwable))
                    }
                    .collect { result ->
                        result.fold(
                            onSuccess = { product ->
                                _uiState.value = ProductDetailState.Success(product)
                            },
                            onFailure = { throwable ->
                                Log.e(TAG, "getProduct failed", throwable)
                                _uiState.value = ProductDetailState.Error(mapProductError(throwable))
                            },
                        )
                    }
            } catch (throwable: Throwable) {
                Log.e(TAG, "loadProduct failed", throwable)
                _uiState.value = ProductDetailState.Error(mapProductError(throwable))
            }
        }
    }

    private fun mapProductError(throwable: Throwable?): String {
        return when (throwable) {
            is IOException,
            is HttpException,
            -> context.getString(R.string.error_network_product_detail)
            else -> context.getString(R.string.error_generic_product_detail)
        }
    }

    private companion object {
        const val TAG = "ProductApp"
    }
}
