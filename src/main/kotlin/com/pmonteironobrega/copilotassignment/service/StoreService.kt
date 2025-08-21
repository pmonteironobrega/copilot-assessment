package com.pmonteironobrega.copilotassignment.service

import com.pmonteironobrega.copilotassignment.model.Product
import com.pmonteironobrega.copilotassignment.model.Store
import com.pmonteironobrega.copilotassignment.repository.StoreRepository
import org.springframework.stereotype.Service

@Service
class StoreService(private val storeRepository: StoreRepository) {
    fun saveStore(store: Store): Store = storeRepository.save(store)

    fun getUnavailableProductsByStore(restaurantBrand: String?, location: String?): List<Product> {
        return when {
            restaurantBrand != null && location != null -> {
                val store = storeRepository.findByRestaurantBrandAndLocation(restaurantBrand, location)
                store?.products?.filter { it.qty == 0 } ?: emptyList()
            }
            restaurantBrand != null -> {
                val stores = storeRepository.findByRestaurantBrand(restaurantBrand)
                stores.flatMap { it.products.filter { p -> p.qty == 0 } }
            }
            location != null -> {
                val stores = storeRepository.findByLocation(location)
                stores.flatMap { it.products.filter { p -> p.qty == 0 } }
            }
            else -> getAllUnavailableProducts().values.flatten()
        }
    }

    fun getUnavailableProductsByStores(requests: List<StoreQueryRequest>): Map<String, List<Product>> {
        val result = mutableMapOf<String, List<Product>>()
        requests.forEach { req ->
            val products = storeQueryFlexible(req.restaurantBrand, req.location)
            val key = listOfNotNull(req.restaurantBrand, req.location).joinToString(" - ")
            result[key] = products
        }
        return result
    }

    private fun storeQueryFlexible(restaurantBrand: String?, location: String?): List<Product> {
        return when {
            restaurantBrand != null && location != null -> {
                val store = storeRepository.findByRestaurantBrandAndLocation(restaurantBrand, location)
                store?.products?.filter { it.qty == 0 } ?: emptyList()
            }
            restaurantBrand != null -> {
                val stores = storeRepository.findByRestaurantBrand(restaurantBrand)
                stores.flatMap { it.products.filter { p -> p.qty == 0 } }
            }
            location != null -> {
                val stores = storeRepository.findByLocation(location)
                stores.flatMap { it.products.filter { p -> p.qty == 0 } }
            }
            else -> getAllUnavailableProducts().values.flatten()
        }
    }

    fun getAllUnavailableProducts(): Map<String, List<Product>> {
        val stores = storeRepository.findAll()
        return stores.associate { "${it.name} - ${it.location}" to it.products.filter { p -> p.qty == 0 } }
    }
}

// DTO para requisições múltiplas
data class StoreQueryRequest(val restaurantBrand: String?, val location: String?)
