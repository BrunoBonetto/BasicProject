package com.example.basicproject.home.domain.usecase

import com.example.basicproject.home.domain.repository.HomeRepository
import com.example.basicproject.home.domain.result.ListProductsResult
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): ListProductsResult {
        return repository.getListProducts()
    }
}