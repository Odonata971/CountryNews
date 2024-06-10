package com.florianfabre.countrynews.data.repository

import com.florianfabre.countrynews.data.dao.CountryDAO
import com.florianfabre.countrynews.data.dao.FavouriteDAO
import com.florianfabre.countrynews.data.model.Country
import com.florianfabre.countrynews.data.model.Favourite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for the `Country` and `Favourite` entities.
 *
 * @property countryDao The DAO for the `Country` entity.
 * @property favouriteDao The DAO for the `Favourite` entity.
 *
 * @method getAllCountries Retrieves all countries from the `Country` table.
 * @method insertAll Inserts a list of countries into the `Country` table.
 * @method getCountriesByName Retrieves countries from the `Country` table that match the given name.
 * @method getCountryByIso2 Retrieves a country from the `Country` table by its ISO 3166-1 alpha-2 code.
 * @method updateCountryByIso2 Updates a country in the `Country` table by its ISO 3166-1 alpha-2 code.
 * @method getAllCountriesReverse Retrieves all countries from the `Country` table in reverse order.
 * @method addFavourite Inserts a favourite into the `Favourite` table.
 * @method removeFavourite Removes a favourite from the `Favourite` table.
 * @method isFavourite Checks if a specific country is a favourite for a specific user.
 * @method getFavourites Retrieves all favourites for a specific user.
 * @method getFavouritesByName Retrieves all favourites for a specific user that match the given country name.
 */
class CountryRepository(
    private val countryDao: CountryDAO,
    private val favouriteDao: FavouriteDAO
) {
     fun getAllCountries() = countryDao.getAllCountries()
     fun insertAll(countries: List<Country>) = countryDao.insertAll(countries)
     fun getCountriesByName(name: String) = countryDao.getCountriesByName(name)
    fun getCountryByIso2(iso2: String) = countryDao.getCountryByIso2(iso2)
    fun updateCountryByIso2(iso2: String, name: String,
                            iso3: String, currency: String, flag: String,
                            capital: String, dialCode: String)
    = countryDao.updateCountryByIso2(iso2, name, iso3, currency, flag, capital, dialCode)

    fun getAllCountriesReverse() = countryDao.getAllCountriesReverse()

    // Add the following methods to the CountryRepository class
    suspend fun addFavourite(countryId: Int, userId: Int) {
        withContext(Dispatchers.IO) {
            favouriteDao.addFavourite(Favourite(countryId, userId))
        }
    }

    suspend fun removeFavourite(countryId: Int, userId: Int) {
        withContext(Dispatchers.IO) {
            favouriteDao.removeFavourite(Favourite(countryId, userId))
        }
    }

    suspend fun isFavourite(countryId: Int, userId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            favouriteDao.isFavourite(countryId, userId)
        }
    }

    suspend fun getFavourites(userId: Int): List<Country> {
        return withContext(Dispatchers.IO) {
            val favourites = favouriteDao.getFavourites(userId)
            val countryIds = favourites.map { it.countryId }
            countryDao.getAllCountries().filter { it.countryId in countryIds }
        }
    }

    suspend fun getFavouritesByName(name: String, userId: Int): List<Country> {
        return withContext(Dispatchers.IO) {
            val favourites = favouriteDao.getFavouritesByName(name, userId)
            val countryIds = favourites.map { it.countryId }
            countryDao.getCountriesByName(name).filter { it.countryId in countryIds }
        }
    }
}
