package com.products.app.navigation

object Screen {
    const val LIST = "list"
    const val DETAIL = "detail/{productId}"
    const val ARG_PRODUCT_ID = "productId"

    fun detailRoute(productId: Int): String = "detail/$productId"
}
