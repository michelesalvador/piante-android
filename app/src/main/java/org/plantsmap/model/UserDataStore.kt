package org.plantsmap.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

private val Context.userDataStore: DataStore<User?> by dataStore("user.json", UserSerializer)

object UserSerializer : Serializer<User?> {
    override val defaultValue: User? = null

    override suspend fun readFrom(input: InputStream): User? {
        return try {
            Json.decodeFromString(User.serializer(), input.readBytes().decodeToString())
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun writeTo(t: User?, output: OutputStream) {
        if (t != null) {
            output.write(Json.encodeToString(User.serializer(), t).encodeToByteArray())
        }
    }
}

class UserDataStore(context: Context) {

    private val dataStore = context.userDataStore
    val user: Flow<User?> = dataStore.data

    suspend fun saveUser(user: User) {
        dataStore.updateData { user }
    }

    suspend fun clearUser() {
        dataStore.updateData { null }
    }
}