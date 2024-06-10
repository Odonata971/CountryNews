package com.florianfabre.countrynews.data.container

import android.content.Context
import com.florianfabre.countrynews.data.db.CountryNewsDB
import com.florianfabre.countrynews.data.repository.CountryRepository
import com.florianfabre.countrynews.data.repository.UserRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val countryRepository: CountryRepository
    val userRepository: UserRepository
}

/**
 * [AppContainer] implementation that provides instance of [countryRepository] and [UserRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [countryRepository]
     */
    override val countryRepository: CountryRepository by lazy {
        CountryRepository(
            CountryNewsDB.getInstance(context).countryDAO(),
            CountryNewsDB.getInstance(context).favouriteDAO()
        )
    }

    /**
     * Implementation for  [UserRepository]
     */
    override val userRepository: UserRepository by lazy {
        UserRepository(CountryNewsDB.getInstance(context).userDAO())
    }
}
