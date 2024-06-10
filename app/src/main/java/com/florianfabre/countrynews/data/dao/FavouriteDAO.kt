package com.florianfabre.countrynews.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.florianfabre.countrynews.data.model.Favourite

/**
 * DAO for the `Favourite` entity.
 *
 * @method addFavourite Inserts a favourite into the `Favourite` table.
 * @method removeFavourite Removes a favourite from the `Favourite` table.
 * @method isFavourite Checks if a specific country is a favourite for a specific user.
 * @method getFavourites Retrieves all favourites for a specific user.
 * @method getFavouritesByName Retrieves all favourites for a specific user that match the given country name.
 */
@Dao
interface FavouriteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavourite(favourite: Favourite)

    @Delete
    suspend fun removeFavourite(favourite: Favourite)

    @Query("SELECT EXISTS(SELECT * FROM Favourite WHERE countryId = :countryId AND userId = :userId)")
    suspend fun isFavourite(countryId: Int, userId: Int): Boolean

    @Query("SELECT * FROM Favourite WHERE userId = :userId")
    suspend fun getFavourites(userId: Int): List<Favourite>

    @Query("""
    SELECT Favourite.*
    FROM Favourite
    INNER JOIN Country ON Favourite.countryId = Country.countryId 
    WHERE Country.name LIKE :name || '%' AND Favourite.userId = :userId
""")
    suspend fun getFavouritesByName(name: String, userId: Int): List<Favourite>
}