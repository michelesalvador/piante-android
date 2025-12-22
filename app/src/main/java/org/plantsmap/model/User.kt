package org.plantsmap.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String? = null,
    val token: String? = null
)