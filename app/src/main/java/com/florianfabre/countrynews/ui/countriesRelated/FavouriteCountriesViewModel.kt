package com.florianfabre.countrynews.ui.countriesRelated

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.florianfabre.countrynews.utilities.SingletonLoggedInUser
import com.florianfabre.countrynews.data.model.Country
import com.florianfabre.countrynews.data.repository.CountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Represents the UI state of the favourite countries screen.
 *
 * @property errorMessage The error message to be displayed, if any.
 * @property isLoading A boolean indicating whether the data is loading.
 */
data class FavouriteCountriesUiState(
    val errorMessage: String = "",
    val isLoading: Boolean = false
)


/**
 * ViewModel for the favourite countries screen.
 *
 * @property repository The repository to fetch the data from.
 */
class FavouriteCountriesViewModel(private val repository: CountryRepository): ViewModel() {
    private var _uiState = MutableStateFlow(FavouriteCountriesUiState())
    val uiState: MutableStateFlow<FavouriteCountriesUiState> = _uiState

    private var _filteredCountries = MutableStateFlow(listOf<Country>() )
    val filteredCountries: StateFlow<List<Country>> = _filteredCountries

    var searchText = mutableStateOf("")


    /**
     * Initialization block that gets executed when the ViewModel is created.
     * It updates the countries with an empty search text.
     */
    init {
        updateCountries("")
    }


    /**
     * Updates the search text and filters the countries accordingly.
     *
     * @param searchText The search text to filter the countries by.
     */
    fun updateSearchText(searchText: String) {
        this.searchText.value = searchText
        updateCountries(searchText)
    }


    /**
     * Updates the countries based on the search text.
     *
     * @param searchText The search text to filter the countries by.
     */
    fun updateCountries(searchText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val countries = if (searchText.isEmpty()) {
                SingletonLoggedInUser.getCurrentUser()?.userId?.let { repository.getFavourites(it) }
            } else {
                SingletonLoggedInUser.getCurrentUser()?.userId?.let { repository.getFavouritesByName(searchText, it) }
            }
            withContext(Dispatchers.Main) {
                if (countries != null) {
                    _filteredCountries.value = countries.sortedBy { it.name }
                    Log.d("FavouriteCountriesViewModel", "Updated countries: ${_filteredCountries.value}")
                } else {
                    Log.d("FavouriteCountriesViewModel", "No countries found for search text: $searchText")
                }
            }
        }
    }
}