package com.example.transactionalpresentation

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class PropagationSampleService(
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveProductsTransactionIsMandatoryWithNewTransaction(products: List<Product>): List<Product> {
        return saveProductsTransactionIsMandatory(products)
    }

    @Transactional(propagation = Propagation.MANDATORY)
    fun saveProductsTransactionIsMandatory(products: List<Product>): List<Product> {
        return products.map { productRepository.save(it) }
    }

}