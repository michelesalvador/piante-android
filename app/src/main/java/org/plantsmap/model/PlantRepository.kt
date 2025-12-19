package org.plantsmap.model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

class PlantRepository {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getPlants(): List<Plant> {
        return try {
            client.get("https://michelesalvador.it/piante/api/plants").body()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}