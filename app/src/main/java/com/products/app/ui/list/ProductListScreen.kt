package com.products.app.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.products.app.R
import com.products.app.data.model.Product
import com.products.app.data.model.Rating
import com.products.app.ui.components.ErrorView
import com.products.app.ui.components.LoadingView
import com.products.app.ui.components.ProductCard
import com.products.app.ui.theme.AccentTan
import com.products.app.ui.theme.ProductAppTheme
import com.products.app.ui.theme.SurfaceDark
import com.products.app.ui.theme.TextSecondaryMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel = hiltViewModel(),
) {
    val pullRefreshState = rememberPullToRefreshState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = stringResource(R.string.shop_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            color = MaterialTheme.colorScheme.onBackground,
        )
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::updateSearch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.search_hint),
                    color = TextSecondaryMuted,
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(ProductListScreenConstants.SearchFieldCorner),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentTan.copy(alpha = 0.45f),
                unfocusedBorderColor = TextSecondaryMuted.copy(alpha = 0.35f),
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = AccentTan,
            ),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
        ) {
            when (val currentState = state) {
                is ProductListViewModel.ProductListState.Loading -> {
                    LoadingView()
                }

                is ProductListViewModel.ProductListState.Error -> {
                    ErrorView(
                        message = currentState.message,
                        onRetry = viewModel::loadProducts,
                    )
                }

                is ProductListViewModel.ProductListState.Success -> {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = viewModel::refresh,
                        state = pullRefreshState,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        if (filteredProducts.isEmpty() && searchQuery.isNotBlank()) {
                            EmptySearchView(query = searchQuery)
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                items(
                                    items = filteredProducts,
                                    key = { it.id },
                                ) { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = { onProductClick(product.id) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private object ProductListScreenConstants {
    val SearchFieldCorner = 28.dp
}

@Composable
private fun EmptySearchView(query: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "🔍", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No results for \"$query\"",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
private fun ProductListScreenPreview() {
    ProductAppTheme {
        ProductListScreenContentPreview(
            state = ProductListViewModel.ProductListState.Success(
                listOf(
                    Product(
                        id = 1,
                        title = "Fjallraven Backpack",
                        price = 109.95,
                        description = "",
                        category = "men's clothing",
                        image = "",
                        rating = Rating(3.9, 120),
                    ),
                ),
            ),
            searchQuery = "",
            filteredProducts = listOf(
                Product(
                    id = 1,
                    title = "Fjallraven Backpack",
                    price = 109.95,
                    description = "",
                    category = "men's clothing",
                    image = "",
                    rating = Rating(3.9, 120),
                ),
            ),
            isRefreshing = false,
            onProductClick = {},
            onRetry = {},
            onRefresh = {},
            onSearchChange = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductListScreenContentPreview(
    state: ProductListViewModel.ProductListState,
    searchQuery: String,
    filteredProducts: List<Product>,
    isRefreshing: Boolean,
    onProductClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onSearchChange: (String) -> Unit,
) {
    val pullRefreshState = rememberPullToRefreshState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = stringResource(R.string.shop_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            color = MaterialTheme.colorScheme.onBackground,
        )
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.search_hint),
                    color = TextSecondaryMuted,
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(ProductListScreenConstants.SearchFieldCorner),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentTan.copy(alpha = 0.45f),
                unfocusedBorderColor = TextSecondaryMuted.copy(alpha = 0.35f),
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = AccentTan,
            ),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
        ) {
            when (state) {
                is ProductListViewModel.ProductListState.Loading -> LoadingView()
                is ProductListViewModel.ProductListState.Error -> {
                    ErrorView(message = state.message, onRetry = onRetry)
                }

                is ProductListViewModel.ProductListState.Success -> {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh,
                        state = pullRefreshState,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        if (filteredProducts.isEmpty() && searchQuery.isNotBlank()) {
                            EmptySearchView(query = searchQuery)
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                items(
                                    items = filteredProducts,
                                    key = { it.id },
                                ) { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = { onProductClick(product.id) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
