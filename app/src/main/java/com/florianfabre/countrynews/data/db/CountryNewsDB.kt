package com.florianfabre.countrynews.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.florianfabre.countrynews.data.dao.CountryDAO
import com.florianfabre.countrynews.data.dao.FavouriteDAO
import com.florianfabre.countrynews.data.dao.UserDAO
import com.florianfabre.countrynews.data.model.Country
import com.florianfabre.countrynews.data.model.Favourite
import com.florianfabre.countrynews.data.model.User


/*
Database class based on the Singleton pattern.
*/
@Database(
    entities = [
        Country::class,
        User::class,
        Favourite::class
    ],
    version = 1,
    exportSchema = false
)

/**
 * Database class based on the Singleton pattern.
 *
 * @property countryDAO The DAO for the `Country` entity.
 * @property userDAO The DAO for the `User` entity.
 * @property favouriteDAO The DAO for the `Favourite` entity.
 */
abstract class CountryNewsDB : RoomDatabase() {

    abstract fun countryDAO() : CountryDAO
    abstract fun userDAO() : UserDAO
    abstract fun favouriteDAO() : FavouriteDAO

    companion object {
        @Volatile
        private var Instance: CountryNewsDB? = null

        /**
         * Gets the singleton instance of the `CountryNewsDB` database.
         *
         * @param context The context to build the database instance.
         * @return The singleton instance of the `CountryNewsDB` database.
         */
        fun getInstance(context: Context): CountryNewsDB {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CountryNewsDB::class.java, "country_news_db")
                /*
                Setting this option in your app's database builder means that Room
                permanently deletes all data from the tables in your database when it
               atempts to perform a migration with no defined migration path.*/
                    .fallbackToDestructiveMigration()
                    .build().also { Instance = it }
            }
        }
    }
}