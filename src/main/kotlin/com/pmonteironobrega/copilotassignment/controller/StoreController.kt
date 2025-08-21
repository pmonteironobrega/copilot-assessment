package com.pmonteironobrega.copilotassignment.controller

import com.pmonteironobrega.copilotassignment.error.StoreErrorMessage
import com.pmonteironobrega.copilotassignment.model.Store
import com.pmonteironobrega.copilotassignment.service.StoreQueryRequest
import com.pmonteironobrega.copilotassignment.service.StoreService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stores")
class StoreController(private val storeService: StoreService) {

    @PostMapping
    fun saveStore(@RequestBody store: Store): ResponseEntity<Store> =
        ResponseEntity.ok(storeService.saveStore(store))

    @GetMapping("/unavailable-products")
    fun getUnavailableProductsByStore(
        @RequestParam(required = false) restaurantBrand: String?,
        @RequestParam(required = false) location: String?
    ): ResponseEntity<Any> {
        val parsedBrand = restaurantBrand?.replace("+", " ")
        val parsedLocation = location?.replace("+", " ")
        val result = if (parsedBrand == null && parsedLocation == null) {
            storeService.getAllUnavailableProducts()
        } else {
            storeService.getUnavailableProductsByStore(parsedBrand, parsedLocation)
        }
        return ResponseEntity.ok(result)
    }

    @PostMapping("/unavailable-products/multi")
    fun getUnavailableProductsByStores(@RequestBody requests: List<StoreQueryRequest>): ResponseEntity<Any> {
        val parsedRequests = requests.map {
            StoreQueryRequest(
                it.restaurantBrand?.replace("+", " "),
                it.location?.replace("+", " ")
            )
        }
        val result = storeService.getUnavailableProductsByStores(parsedRequests)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/available-stores")
    fun getAvailableStoreBrands(): ResponseEntity<Any> {
        val result = storeService.getAvailableStoreBrands()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/available-stores/names")
    fun getAvailableStoreNames(): ResponseEntity<Any> {
        val result = storeService.getAvailableStoreNames()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/available-stores/location/{storeId}")
    fun getLocationsByStoreId(@PathVariable storeId: String): ResponseEntity<Any> {
        val result = storeService.getLocationsByStoreId(storeId)
        return ResponseEntity.ok(result)
    }
}

@RestControllerAdvice
class StoreExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(400).body(mapOf("error" to (ex.message ?: StoreErrorMessage.INVALID_BRAND.message)))

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElement(ex: NoSuchElementException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(404).body(mapOf("error" to (ex.message ?: StoreErrorMessage.NO_STORE_FOUND.message)))

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(500).body(mapOf("error" to (ex.message ?: StoreErrorMessage.INTERNAL_ERROR.message)))
}
