package com.example.transactionalpresentation

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class SampleService(
    val productRepository: ProductRepository
) {

    @Transactional(propagation = Propagation.REQUIRED)
    fun saveProductsAtomic(products: List<Product>): List<Product> {
        return products.map { productRepository.save(it) }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun saveProductsNonAtomic(products: List<Product>): List<Product> {
        return products.map { productRepository.save(it) }
    }

}