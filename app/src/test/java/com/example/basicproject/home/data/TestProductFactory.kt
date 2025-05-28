package com.example.basicproject.home.data

import com.example.basicproject.home.data.remote.model.ProductEntity

object TestProductFactory {
    fun create(
        id: Int = 1,
        title: String = "Product 1",
        price: Double = 10.0,
        thumbnail: String = "https://example.com/product1.jpg"
    ) = ProductEntity(id, title, price, thumbnail)
}