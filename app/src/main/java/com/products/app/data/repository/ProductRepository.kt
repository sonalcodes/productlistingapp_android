package com.products.app.data.repository

import com.products.app.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<Result<List<Product>>>
    fun getProduct(id: Int): Flow<Result<Product>>
}
