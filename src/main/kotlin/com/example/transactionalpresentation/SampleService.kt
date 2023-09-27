package com.example.transactionalpresentation

import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class SampleService(
    val productRepository: ProductRepository
) {

    @Transactional(value = Transactional.TxType.REQUIRED)
    fun saveProductsAtomic(products: List<Product>): List<Product> {
        return products.map { productRepository.save(it) }
    }

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    fun saveProductsNonAtomic(products: List<Product>): List<Product> {
        return products.map { productRepository.save(it) }
    }

}