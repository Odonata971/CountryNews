package com.florianfabre.countrynews.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Represents a favourite entity in the CountryNews application.
 *
 * @property countryId The unique ID of the country.
 * @property userId The unique ID of the user.
 */
//@Entity(primaryKeys = ["countryId", "userId"])
@Entity(tableName = "Favourite",
    foreignKeys = [ForeignKey(
        entity = Country::class,
        childColumns = ["countryId"],
        parentColumns = ["countryId"]
    ),
    ForeignKey(
        entity = User::class,
        childColumns = ["userId"],
        parentColumns = ["userId"]
    )], primaryKeys = ["countryId", "userId"])
data class Favourite(
    @ColumnInfo(name = "countryId")
    var countryId: Int,
    @ColumnInfo(name = "userId")
    var userId: Int
)