package com.florianfabre.countrynews.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Represents a country entity in the CountryNews application.
 *
 * @property countryId The unique ID of the country.
 * @property name The name of the country.
 * @property iso2 The ISO 3166-1 alpha-2 code of the country.
 * @property iso3 The ISO 3166-1 alpha-3 code of the country.
 * @property currency The currency of the country.
 * @property flag The flag of the country.
 * @property capital The capital of the country.
 * @property dialCode The dialing code of the country.
 */
@Entity(indices = [Index(value = ["iso2"], unique = true)])
data class Country(
    @PrimaryKey
    var countryId: Int? = null,
    val name: String,
    val iso2: String,
    val iso3: String,
    val currency: String?,
    val flag: String?,
    val capital: String?,
    val dialCode: String?
)