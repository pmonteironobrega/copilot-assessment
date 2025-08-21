package com.pmonteironobrega.copilotassignment.repository

import com.pmonteironobrega.copilotassignment.model.Store
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : MongoRepository<Store, String> {
    fun findByRestaurantBrand(restaurantBrand: String): List<Store>
    fun findByLocation(location: String): List<Store>
    fun findByRestaurantBrandIn(restaurantBrands: List<String>): List<Store>
    fun findByRestaurantBrandAndLocation(restaurantBrand: String, location: String): Store?
    fun findByRestaurantBrandInAndLocationIn(restaurantBrands: List<String>, locations: List<String>): List<Store>
    fun findAllByOrderByRestaurantBrandAsc(): List<Store>
    @Query("SELECT DISTINCT restaurantBrand FROM Store")
    fun findDistinctRestaurantBrand(): List<String>

    override fun findById(storeId: String): java.util.Optional<Store>
}
