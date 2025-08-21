package com.pmonteironobrega.copilotassignment.repository

import com.pmonteironobrega.copilotassignment.model.Store
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : MongoRepository<Store, String> {
    fun findByName(name: String): List<Store>
    fun findByLocation(location: String): List<Store>
    fun findByNameIn(names: List<String>): List<Store>
    fun findByNameAndLocation(name: String, location: String): Store?
    fun findByNameInAndLocationIn(names: List<String>, locations: List<String>): List<Store>
}
