package com.products.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.products.app.R
import com.products.app.data.model.Product
import com.products.app.data.model.Rating
import com.products.app.ui.theme.AccentTan
import com.products.app.ui.theme.ProductAppTheme
import com.products.app.ui.theme.SurfaceDark
import java.util.Locale

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(ProductCardConstants.CornerRadius)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = ProductCardConstants.CardElevation,
                shape = shape,
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
            )
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ProductCardConstants.InnerPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.image)
                    .crossfade(300)
                    .build(),
                contentDescription = product.title,
                contentScale = ContentScale.Fit,
                placeholder = ColorPainter(Color(0xFF1A1A1A)),
                error = ColorPainter(Color(0xFF2A2A2A)),
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1A1A1A)),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = ProductCardConstants.TitleMaxLines,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = product.category.uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentTan,
                )
                Text(
                    text = stringResource(
                        R.string.price_format,
                        String.format(Locale.US, "%.2f", product.price),
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

private object ProductCardConstants {
    val CornerRadius = 16.dp
    val CardElevation = 2.dp
    val InnerPadding = 12.dp
    const val TitleMaxLines = 2
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
private fun ProductCardPreview() {
    ProductAppTheme {
        ProductCard(
            product = Product(
                id = 1,
                title = "Sample product with a long title that truncates",
                price = 29.99,
                description = "",
                category = "electronics",
                image = "",
                rating = Rating(rate = 4.5, count = 120),
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
