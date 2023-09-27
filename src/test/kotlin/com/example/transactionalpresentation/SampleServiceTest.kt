package com.example.transactionalpresentation

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException

@SpringBootTest
class SampleServiceTest {

    @Autowired
    lateinit var productRepository: ProductRepository
    @Autowired
    lateinit var sampleService: SampleService

    /**
     * define a same barcode for 2 different barcode
     */
    val barcode3 = "b3"

    @BeforeEach
    fun clearDb(){
        productRepository.deleteAll()
    }

    @Test
    fun `test unique constraint`() {
        val barcode1 = "b1"
        val product = productRepository.save(Product(name = "p1", price = 10, barcode = barcode1))
        Assertions.assertEquals("p1", product.name)
        Assertions.assertThrows(DataIntegrityViolationException::class.java, {
            productRepository.save(
                Product(
                    name = "p1",
                    price = 22,
                    barcode = barcode1
                )
            )
        }, "database throw exception because of duplicate barcode which has unique constraint")
    }


    @Test
    fun `test transactional when atomic operation is required`() {
        sampleService.saveProductsAtomic(
            listOf(
                Product(name = "p1", price = 10, barcode = "b1"),
                Product(name = "p2", price = 20, barcode = "b2")
            )
        )
        Assertions.assertEquals(2, productRepository.findAll().size)

        runCatching {
            sampleService.saveProductsAtomic(
                listOf(
                    Product(name = "p1", price = 30, barcode = barcode3),
                    Product(name = "p4", price = 40, barcode = barcode3)
                )
            )
        }.onFailure {
            Assertions.assertEquals(DataIntegrityViolationException::class.java,it::class.java)
        }
        Assertions.assertEquals(2, productRepository.findAll().size)
    }

    @Test
    fun `test transactional when non-atomic operation is required`() {
        sampleService.saveProductsAtomic(
            listOf(
                Product(name = "p1", price = 10, barcode = "b1"),
                Product(name = "p2", price = 20, barcode = "b2")
            )
        )
        Assertions.assertEquals(2, productRepository.findAll().size)

        runCatching {
            sampleService.saveProductsNonAtomic(
                listOf(
                    Product(name = "p1", price = 30, barcode = barcode3),
                    Product(name = "p4", price = 40, barcode = barcode3)
                )
            )
        }.onFailure {
            Assertions.assertEquals(DataIntegrityViolationException::class.java,it::class.java)
        }
        Assertions.assertEquals(3, productRepository.findAll().size)
    }
}
