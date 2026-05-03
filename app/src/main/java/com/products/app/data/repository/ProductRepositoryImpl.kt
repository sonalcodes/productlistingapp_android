package com.products.app.data.repository

import com.products.app.data.model.Product
import com.products.app.data.remote.ApiService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : ProductRepository {

    override fun getProducts(): Flow<Result<List<Product>>> = flow {
        try {
            emit(Result.success(apiService.getProducts()))
        } catch (e: CancellationException) {
            throw e
        } catch (throwable: Throwable) {
            emit(Result.failure(throwable))
        }
    }

    override fun getProduct(id: Int): Flow<Result<Product>> = flow {
        try {
            emit(Result.success(apiService.getProduct(id)))
        } catch (e: CancellationException) {
            throw e
        } catch (throwable: Throwable) {
            emit(Result.failure(throwable))
        }
    }
}
