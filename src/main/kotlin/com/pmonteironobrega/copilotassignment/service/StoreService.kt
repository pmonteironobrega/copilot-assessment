package com.pmonteironobrega.copilotassignment.service

import com.pmonteironobrega.copilotassignment.model.Product
import com.pmonteironobrega.copilotassignment.model.Store
import com.pmonteironobrega.copilotassignment.repository.StoreRepository
import org.springframework.stereotype.Service

@Service
class StoreService(private val storeRepository: StoreRepository) {
    fun saveStore(store: Store): Store = storeRepository.save(store)

    fun getUnavailableProductsByStore(restaurantBrand: String?, location: String?): List<Product> {
        if ((restaurantBrand == null || restaurantBrand.isBlank()) && (location == null || location.isBlank())) {
            return getAllUnavailableProducts().values.flatten()
        }
        if (restaurantBrand != null && restaurantBrand.isBlank()) {
            throw IllegalArgumentException("O parâmetro restaurantBrand não pode ser vazio.")
        }
        if (location != null && location.isBlank()) {
            throw IllegalArgumentException("O parâmetro location não pode ser vazio.")
        }
        val stores = when {
            restaurantBrand != null && location != null -> {
                val store = storeRepository.findByRestaurantBrandAndLocation(restaurantBrand, location)
                if (store == null) throw NoSuchElementException("Store não encontrada para os parâmetros informados.")
                listOf(store)
            }
            restaurantBrand != null -> {
                val found = storeRepository.findByRestaurantBrand(restaurantBrand)
                if (found.isEmpty()) throw NoSuchElementException("Nenhuma store encontrada para a marca informada.")
                found
            }
            location != null -> {
                val found = storeRepository.findByLocation(location)
                if (found.isEmpty()) throw NoSuchElementException("Nenhuma store encontrada para a localização informada.")
                found
            }
            else -> emptyList()
        }
        return stores.flatMap { it.products.filter { p -> p.qty == 0 } }
    }

    fun getUnavailableProductsByStores(requests: List<StoreQueryRequest>): Map<String, List<Product>> {
        if (requests.isEmpty()) throw IllegalArgumentException("A lista de stores não pode ser vazia.")
        val result = mutableMapOf<String, List<Product>>()
        requests.forEach { req ->
            val products = try {
                storeQueryFlexible(req.restaurantBrand, req.location)
            } catch (ex: NoSuchElementException) {
                // Adiciona erro específico para cada store não encontrada
                result[listOfNotNull(req.restaurantBrand, req.location).joinToString(" - ")] = listOf()
                return@forEach
            }
            val key = listOfNotNull(req.restaurantBrand, req.location).joinToString(" - ")
            result[key] = products
        }
        return result
    }

    private fun storeQueryFlexible(restaurantBrand: String?, location: String?): List<Product> {
        if (restaurantBrand != null && restaurantBrand.isBlank()) {
            throw IllegalArgumentException("O parâmetro restaurantBrand não pode ser vazio.")
        }
        if (location != null && location.isBlank()) {
            throw IllegalArgumentException("O parâmetro location não pode ser vazio.")
        }
        return when {
            restaurantBrand != null && location != null -> {
                val store = storeRepository.findByRestaurantBrandAndLocation(restaurantBrand, location)
                if (store == null) throw NoSuchElementException("Store não encontrada para os parâmetros informados.")
                store.products.filter { it.qty == 0 }
            }
            restaurantBrand != null -> {
                val stores = storeRepository.findByRestaurantBrand(restaurantBrand)
                if (stores.isEmpty()) throw NoSuchElementException("Nenhuma store encontrada para a marca informada.")
                stores.flatMap { it.products.filter { p -> p.qty == 0 } }
            }
            location != null -> {
                val stores = storeRepository.findByLocation(location)
                if (stores.isEmpty()) throw NoSuchElementException("Nenhuma store encontrada para a localização informada.")
                stores.flatMap { it.products.filter { p -> p.qty == 0 } }
            }
            else -> getAllUnavailableProducts().values.flatten()
        }
    }

    fun getAllUnavailableProducts(): Map<String, List<Product>> {
        val stores = storeRepository.findAll()
        return stores.associate { "${it.restaurantBrand} - ${it.location}" to it.products.filter { p -> p.qty == 0 } }
    }
}

// DTO para requisições múltiplas
data class StoreQueryRequest(val restaurantBrand: String?, val location: String?)
