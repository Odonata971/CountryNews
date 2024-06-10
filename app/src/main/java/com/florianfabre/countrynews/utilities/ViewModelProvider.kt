package com.florianfabre.countrynews.utilities

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.florianfabre.countrynews.app.CountryNewsApplication
import com.florianfabre.countrynews.ui.countriesRelated.CountriesListViewModel
import com.florianfabre.countrynews.ui.countriesRelated.CountryDetailViewModel
import com.florianfabre.countrynews.ui.countriesRelated.FavouriteCountriesViewModel
import com.florianfabre.countrynews.ui.userRelated.CreateUserViewModel
import com.florianfabre.countrynews.ui.userRelated.LoginViewModel
import com.florianfabre.countrynews.ui.userRelated.ProfileViewModel


/**
 * Provides instances of various ViewModel classes.
 */
object ViewModelProvider {


    /**
     * A factory for creating instances of ViewModel classes.
     */
    val Factory = viewModelFactory {
        initializer  {
            LoginViewModel(
                repository = CountryNewsApplication().container.userRepository
            )
        }
        initializer  {
            CountriesListViewModel(
                repository = CountryNewsApplication().container.countryRepository
            )
        }
        initializer  {
            CountryDetailViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = CountryNewsApplication().container.countryRepository
            )
        }
        initializer  {
            FavouriteCountriesViewModel(
                repository = CountryNewsApplication().container.countryRepository
            )
        }
        initializer {
            CreateUserViewModel(
                repository = CountryNewsApplication().container.userRepository
            )
        }
        initializer {
            ProfileViewModel(
                repository = CountryNewsApplication().container.userRepository
            )
        }
    }
}


/**
 * Retrieves the CountryNewsApplication instance from the CreationExtras.
 *
 * @return The CountryNewsApplication instance.
 */
fun CreationExtras.CountryNewsApplication() : CountryNewsApplication =
    (this[APPLICATION_KEY] as CountryNewsApplication)