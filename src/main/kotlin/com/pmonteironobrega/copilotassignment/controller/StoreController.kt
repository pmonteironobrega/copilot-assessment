package com.pmonteironobrega.copilotassignment.controller

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
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) location: String?
    ): ResponseEntity<Any> {
        val parsedName = name?.replace("+", " ")
        val parsedLocation = location?.replace("+", " ")
        // Se ambos forem nulos, retorna todos os produtos não disponíveis de todas as lojas
        return if (parsedName == null && parsedLocation == null) {
            ResponseEntity.ok(storeService.getAllUnavailableProducts())
        } else {
            ResponseEntity.ok(storeService.getUnavailableProductsByStore(parsedName, parsedLocation))
        }
    }

    @PostMapping("/unavailable-products/multi")
    fun getUnavailableProductsByStores(@RequestBody requests: List<StoreQueryRequest>): ResponseEntity<Any> {
        val parsedRequests = requests.map {
            StoreQueryRequest(
                it.name?.replace("+", " "),
                it.location?.replace("+", " ")
            )
        }
        return ResponseEntity.ok(storeService.getUnavailableProductsByStores(parsedRequests))
    }
}
