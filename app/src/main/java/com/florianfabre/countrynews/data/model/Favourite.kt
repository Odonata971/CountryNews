package com.florianfabre.countrynews.data.model

import androidx.room.Entity

/**
 * Represents a favourite entity in the CountryNews application.
 *
 * @property countryId The unique ID of the country.
 * @property userId The unique ID of the user.
 */
@Entity(primaryKeys = ["countryId", "userId"])
data class Favourite(
    var countryId: Int,
    var userId: Int
)