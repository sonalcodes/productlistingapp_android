package com.products.app.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.products.app.R
import com.products.app.data.model.Product
import com.products.app.data.model.Rating
import com.products.app.ui.components.ErrorView
import com.products.app.ui.components.LoadingView
import com.products.app.ui.theme.AccentTan
import com.products.app.ui.theme.OnAccentDark
import com.products.app.ui.theme.ProductAppTheme
import com.products.app.ui.theme.TextSecondaryMuted
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val favoritesMessage = stringResource(R.string.favorites_snackbar)

    val topBarTitle = when (val state = uiState) {
        is ProductDetailViewModel.ProductDetailState.Success ->
            state.product.category.toCapitalizedCategory()

        else -> ""
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = topBarTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_desc_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
        ) {
            when (val state = uiState) {
                is ProductDetailViewModel.ProductDetailState.Loading -> {
                    LoadingView()
                }

                is ProductDetailViewModel.ProductDetailState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = viewModel::loadProduct,
                    )
                }

                is ProductDetailViewModel.ProductDetailState.Success -> {
                    ProductDetailContent(
                        product = state.product,
                        onAddToFavorites = {
                            scope.launch {
                                snackbarHostState.showSnackbar(message = favoritesMessage)
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDetailContent(
    product: Product,
    onAddToFavorites: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        AsyncImage(
            model = product.image,
            contentDescription = stringResource(R.string.content_desc_product_image),
            modifier = Modifier
                .fillMaxWidth()
                .height(ProductDetailConstants.ImageHeight)
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = ProductDetailConstants.ImageBottomCorner,
                        bottomEnd = ProductDetailConstants.ImageBottomCorner,
                    ),
                ),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ProductDetailConstants.ContentPadding),
        ) {
            Text(
                text = stringResource(
                    R.string.price_format,
                    String.format(Locale.US, "%.2f", product.price),
                ),
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 32.sp),
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = TextSecondaryMuted.copy(alpha = 0.35f))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.about_section).uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.labelSmall,
                color = AccentTan,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = AccentTan,
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = String.format(Locale.US, "%.1f", product.rating.rate),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.reviews_format, product.rating.count),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryMuted,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAddToFavorites,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentTan,
                    contentColor = OnAccentDark,
                ),
            ) {
                Text(
                    text = stringResource(R.string.add_to_favorites),
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

private fun String.toCapitalizedCategory(): String {
    return split(" ").joinToString(" ") { fragment ->
        fragment.replaceFirstChar { char ->
            if (char.isLowerCase()) {
                char.titlecase(Locale.getDefault())
            } else {
                char.toString()
            }
        }
    }
}

private object ProductDetailConstants {
    val ImageHeight = 300.dp
    val ImageBottomCorner = 24.dp
    val ContentPadding = 16.dp
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
private fun ProductDetailContentPreview() {
    ProductAppTheme {
        ProductDetailContent(
            product = Product(
                id = 1,
                title = "Premium backpack for daily adventures",
                price = 109.95,
                description = "Great product with durable materials and thoughtful design.",
                category = "men's clothing",
                image = "",
                rating = Rating(rate = 4.2, count = 87),
            ),
            onAddToFavorites = {},
        )
    }
}
