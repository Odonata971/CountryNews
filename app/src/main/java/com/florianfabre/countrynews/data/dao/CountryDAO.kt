package com.florianfabre.countrynews.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.florianfabre.countrynews.data.model.Country

/**
 * DAO for the `Country` entity.
 *
 * @method insertAll Inserts a list of countries into the `Country` table.
 * @method getAllCountries Retrieves all countries from the `Country` table.
 * @method getAllCountriesReverse Retrieves all countries from the `Country` table in reverse order.
 * @method getCountriesByName Retrieves countries from the `Country` table that match the given name.
 * @method getCountryByIso2 Retrieves a country from the `Country` table by its ISO 3166-1 alpha-2 code.
 * @method updateCountryByIso2 Updates a country in the `Country` table by its ISO 3166-1 alpha-2 code.
 */
@Dao
interface CountryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(countries: List<Country>)
    @Query("SELECT * FROM Country")
    fun getAllCountries(): List<Country>

    //get all countries in reverse order
    @Query("SELECT * FROM Country ORDER BY name DESC")
    fun getAllCountriesReverse(): List<Country>

    @Query("SELECT  * FROM Country WHERE name LIKE :name || '%'")
    fun getCountriesByName(name: String): List<Country>

    @Query("SELECT * FROM Country WHERE iso2 = :iso2")
    fun getCountryByIso2(iso2: String): Country

    @Query("UPDATE Country SET name = :name, iso3 = :iso3, currency = :currency, flag = :flag, capital = :capital, dialCode = :dialCode WHERE iso2 = :iso2")
    fun updateCountryByIso2(iso2: String, name: String, iso3: String, currency: String, flag: String, capital: String, dialCode: String) : Int
}
