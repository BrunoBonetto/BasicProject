package com.example.basicproject.home.data.remote.model

data class ProductEntity (
    val id: Int,
    val title: String,
    val price: Double,
    val thumbnail: String,
    val description: String,
    val images: List<String> = emptyList()
)



