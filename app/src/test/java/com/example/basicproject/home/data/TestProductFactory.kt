package com.example.basicproject.home.data

import com.example.basicproject.home.data.remote.model.ProductEntity

object TestProductFactory {
    fun create(
        id: Int = 1,
        title: String = "Product 1",
        price: Double = 10.0,
        thumbnail: String = "https://example.com/product1.jpg",
        description: String = "This is a test product",
        images: List<String> = listOf("https://example.com/product1.jpg", "https://example.com/product2.jpg")
    ) = ProductEntity(id, title, price, thumbnail, description, images)
}