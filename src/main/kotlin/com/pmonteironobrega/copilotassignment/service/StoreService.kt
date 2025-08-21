package com.pmonteironobrega.copilotassignment.service

import com.pmonteironobrega.copilotassignment.error.StoreErrorMessage
import com.pmonteironobrega.copilotassignment.model.Product
import com.pmonteironobrega.copilotassignment.model.Store
import com.pmonteironobrega.copilotassignment.repository.StoreRepository
import com.pmonteironobrega.copilotassignment.responses.LocationResponse
import org.springframework.stereotype.Service

@Service
class StoreService(private val storeRepository: StoreRepository) {
    fun saveStore(store: Store): Store = storeRepository.save(store)

    fun getUnavailableProductsByStore(restaurantBrand: String?, location: String?): Map<String, List<Product>> {
        val brand = restaurantBrand?.takeIf { it.isNotBlank() }
        val loc = location?.takeIf { it.isNotBlank() }
        if (restaurantBrand != null && restaurantBrand.isBlank()) throw IllegalArgumentException(StoreErrorMessage.INVALID_BRAND.message)
        if (location != null && location.isBlank()) throw IllegalArgumentException(StoreErrorMessage.INVALID_LOCATION.message)

        val stores = when {
            brand == null && loc == null -> storeRepository.findAll()
            brand != null && loc != null -> {
                storeRepository.findByRestaurantBrandAndLocation(brand, loc)?.let { listOf(it) } ?: emptyList()
            }
            brand != null -> storeRepository.findByRestaurantBrand(brand)
            loc != null -> storeRepository.findByLocation(loc)
            else -> emptyList()
        }
        if (stores.isEmpty()) throw NoSuchElementException(StoreErrorMessage.NO_STORE_FOUND.message)
        val products = stores.flatMap { it.products.filter { p -> p.qty == 0 } }
        if (products.isEmpty()) throw NoSuchElementException(StoreErrorMessage.NO_STORE_FOUND.message)
        return mapOf("results" to products)
    }

    fun getUnavailableProductsByStores(requests: List<StoreQueryRequest>): Map<String, List<Product>> {
        if (requests.isEmpty()) throw IllegalArgumentException(StoreErrorMessage.EMPTY_REQUEST.message)
        val allProducts = mutableListOf<Product>()
        requests.forEach { req ->
            val products = try {
                getUnavailableProductsByStore(req.restaurantBrand, req.location)["results"] as List<Product>
            } catch (ex: NoSuchElementException) {
                emptyList<Product>()
            }
            allProducts.addAll(products)
        }
        if (allProducts.isEmpty()) throw NoSuchElementException(StoreErrorMessage.NO_STORE_FOUND.message)
        return mapOf("results" to allProducts)
    }

    fun getAllUnavailableProducts(): Map<String, List<Product>> {
        val stores = storeRepository.findAll()
        return stores.associate { "${it.restaurantBrand} - ${it.location}" to it.products.filter { p -> p.qty == 0 } }
    }

    fun getAvailableStores(): Map<String, List<Map<String, Any>>> {
        val stores = storeRepository.findAllByOrderByRestaurantBrandAsc()
        if (stores.isEmpty()) throw NoSuchElementException(StoreErrorMessage.NO_STORE_FOUND.message)
        val results = stores.map {
            mapOf(
                "restaurantBrand" to it.restaurantBrand,
                "location" to it.location,
                "products" to it.products
            )
        }
        return mapOf("results" to results)
    }

    fun getAvailableStoreNames(): Map<String, List<Map<String, String>>> {
        val stores = storeRepository.findAllByOrderByRestaurantBrandAsc()
        if (stores.isEmpty()) throw NoSuchElementException(StoreErrorMessage.NO_STORE_FOUND.message)
        val results = stores.map {
            mapOf(
                "restaurantBrand" to it.restaurantBrand,
                "location" to it.location
            )
        }
        return mapOf("results" to results)
    }

    fun getAvailableStoreBrands(): Map<String, List<StoreBrandResponse>> {
        val stores = storeRepository.findAllByOrderByRestaurantBrandAsc()
        if (stores.isEmpty()) throw NoSuchElementException(StoreErrorMessage.NO_STORE_FOUND.message)
        val results = stores.map { StoreBrandResponse(it.id, it.restaurantBrand) }.distinctBy { it.restaurantBrand }
        return mapOf("results" to results)
    }

    fun getAllStoreNames(): Map<String, List<String>> {
        val stores = storeRepository.findAll()
        if (stores.isEmpty()) throw NoSuchElementException(StoreErrorMessage.NO_STORE_FOUND.message)
        val results = stores.map { it.restaurantBrand }.distinct()
        return mapOf("results" to results)
    }

    fun getLocationsByStoreId(storeId: String): Map<String, List<LocationResponse>> {
        if (storeId.isBlank()) throw IllegalArgumentException(StoreErrorMessage.NO_STORE_FOUND.message)
        val store = storeRepository.findById(storeId).orElse(null)
        if (store == null) throw NoSuchElementException(StoreErrorMessage.NO_STORE_FOUND.message)
        val locations = listOf(LocationResponse(store.location))
        return mapOf("results" to locations)
    }
}

// DTO para requisições múltiplas
data class StoreQueryRequest(val restaurantBrand: String?, val location: String?)
// DTO para resposta de marcas de loja
data class StoreBrandResponse(
    val id: String?,
    val restaurantBrand: String
)