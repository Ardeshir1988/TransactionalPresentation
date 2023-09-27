package com.example.transactionalpresentation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
@Table(indexes = [Index(columnList = "barcode", unique = true)])
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val name: String,
    val price: Long,
    val barcode: String
)

@Entity
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    @OneToMany
    val items: List<OrderItem>
)


@Entity
data class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val quantity: Long,
    val price: Long,
    val productId: Long
)

@Repository
interface ProductRepository : JpaRepository<Product, Long>

@Repository
interface OrderRepository : JpaRepository<Order, Long>