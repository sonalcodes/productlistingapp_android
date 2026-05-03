package com.products.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.products.app.ui.detail.ProductDetailScreen
import com.products.app.ui.list.ProductListScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.LIST,
        modifier = modifier,
    ) {
        composable(Screen.LIST) {
            ProductListScreen(
                onProductClick = { productId ->
                    navController.navigate(Screen.detailRoute(productId))
                },
            )
        }
        composable(
            route = Screen.DETAIL,
            arguments = listOf(
                navArgument(Screen.ARG_PRODUCT_ID) { type = NavType.IntType },
            ),
        ) {
            ProductDetailScreen(
                onBack = { navController.navigateUp() },
            )
        }
    }
}
