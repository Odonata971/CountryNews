package com.florianfabre.countrynews

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.florianfabre.countrynews.data.dao.CountryDAO
import com.florianfabre.countrynews.data.db.CountryNewsDB
import com.florianfabre.countrynews.data.model.Country
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Test

import org.junit.Assert.*
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}



@RunWith(AndroidJUnit4::class)
class CountryDaoTest {

    private lateinit var countryDao: CountryDAO
    private lateinit var db: CountryNewsDB


    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, CountryNewsDB::class.java)
            .allowMainThreadQueries()
            .build()
        countryDao = db.countryDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertCountries_insertsCountriesIntoDB() = runBlocking {
        val country1 = Country(0, "France", "FR", "FRA", "EUR", "French", "UTC+01:00", "33")
        val country2 = Country(1, "Germany", "DE", "DEU", "EUR", "German", "UTC+01:00", "49")
        countryDao.insertAll(listOf(country1, country2))
        val allCountries = countryDao.getAllCountries()
        assertEquals(allCountries[0], country1)
        assertEquals(allCountries[1], country2)
    }

    // Add more tests here...
}