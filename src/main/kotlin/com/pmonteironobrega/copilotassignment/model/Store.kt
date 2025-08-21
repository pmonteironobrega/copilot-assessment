package com.pmonteironobrega.copilotassignment.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "stores")
data class Store(
    @Id
    val id: String? = null,
    val name: String,
    val location: String,
    val products: List<Product>
)

data class Product(
    val name: String,
    val qty: Int
)
