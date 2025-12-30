package org.plantsmap.model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

class AppRepository(private val userDataStore: UserDataStore) {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    val user: Flow<User?> = userDataStore.user

    suspend fun getPlants(): Result<List<Plant>> {
        return try {
            val response = client.get("https://michelesalvador.it/piante/api/plants")
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body())
            } else {
                throw Exception(response.body<GenericResponse>().message)
            }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun login(credentials: Credentials): Result<User> {
        return try {
            val response = client.post("https://michelesalvador.it/piante/api/session") {
                contentType(ContentType.Application.Json)
                setBody(credentials)
            }
            if (response.status == HttpStatusCode.OK) {
                val user = response.body<User>()
                userDataStore.saveUser(user)
                Result.success(user) // Returned user is not used by AppViewModel
            } else {
                val errorBody = response.bodyAsText()
                val errorMessage = Json.decodeFromString<GenericResponse>(errorBody).message
                throw Exception(errorMessage)
            }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun logout(token: String): Result<String> {
        return try {
            val response = client.delete("https://michelesalvador.it/piante/api/session") {
                header("Authorization", "Bearer $token")
            }
            val message = response.body<GenericResponse>().message
            if (response.status == HttpStatusCode.OK) {
                userDataStore.clearUser()
                Result.success(message)
            } else {
                throw Exception(message)
            }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}