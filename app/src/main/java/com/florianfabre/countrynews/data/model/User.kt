package com.florianfabre.countrynews.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user entity in the CountryNews application.
 *
 * @property userId The unique ID of the user.
 * @property username The username of the user.
 * @property password The password of the user.
 */
@Entity(tableName = "User")
data class User (
    @PrimaryKey
    var userId: Int? = null,
    val username: String,
    val password: String
)