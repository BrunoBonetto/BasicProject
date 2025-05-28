package com.example.basicproject.product.ui.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.basicproject.R
import com.example.basicproject.home.data.remote.model.ProductEntity


@Composable
fun ProductDetailScreenPreviewContent(
    productDetailUiState: ProductDetailUiState
) {
    Box(modifier = Modifier.fillMaxSize()) {

        if(productDetailUiState.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }else if(productDetailUiState.errorMessage != null){
            Text(
                text = stringResource(R.string.error_loading_product),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                AsyncImage(
                    model = productDetailUiState.product?.thumbnail,
                    contentDescription = productDetailUiState.product?.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = productDetailUiState.product?.title.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "R$ %.2f".format(productDetailUiState.product?.price),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Descrição do produto indisponível no momento.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    val mockProduct = ProductEntity(
        id = 1,
        title = "Smartphone Android 13",
        price = 1999.99,
        thumbnail = "https://dummyjson.com/image/i/products/1/thumbnail.jpg"
    )

    val mockProductUiState = ProductDetailUiState(
        isLoading = false,
        errorMessage = null,
        product = mockProduct
    )

    ProductDetailScreenPreviewContent(productDetailUiState = mockProductUiState)
}