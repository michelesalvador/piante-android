package org.plantsmap.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class Plant(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val number: Int?,
    val diameters: JsonArray?,
    val height: Float?,
    val note: String?,
    val species: Species,
    val user: User,
    val date: String,
    val images: JsonArray
)

@Serializable
data class Species(
    val id: Int,
    val scientificName: String,
    val commonName: String,
    val warning: String?
)

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String
)