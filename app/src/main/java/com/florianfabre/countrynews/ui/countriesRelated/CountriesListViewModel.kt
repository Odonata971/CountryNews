package com.florianfabre.countrynews.ui.countriesRelated

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.florianfabre.countrynews.data.model.Country
import com.florianfabre.countrynews.data.repository.CountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Represents the UI state of the countries list.
 *
 * @property errorMessage The error message to be displayed, if any.
 * @property isLoading A boolean indicating whether the data is currently being loaded.
 * @property isReversed A boolean indicating whether the list of countries is reversed.
 */
data class CountriesListUiState(
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isReversed : Boolean = false
)


/**
 * Provides the data for the countries list screen.
 *
 * @property repository The repository to fetch the data from.
 */
class CountriesListViewModel(private val repository: CountryRepository): ViewModel() {
    private var _uiState = MutableStateFlow(CountriesListUiState())
    val uiState: MutableStateFlow<CountriesListUiState> = _uiState

    private var _filteredCountries = MutableStateFlow(listOf<Country>() )
    val filteredCountries: StateFlow<List<Country>> = _filteredCountries

    var searchText = mutableStateOf("")


    /**
     * Gets executed when the ViewModel is created.
     * It calls the updateCountries function with an empty string as the parameter.
     */
    init {
        updateCountries("")
    }


    /**
     * Updates the search text and calls the updateCountries function with the new search text.
     *
     * @param searchText The new search text.
     */
    fun updateSearchText(searchText: String) {
        this.searchText.value = searchText
        updateCountries(searchText)
    }


    /**
     * Fetches the countries from the repository and updates the filteredCountries flow with the result.
     *
     * @param searchText The search text to filter the countries by.
     */
    private fun updateCountries(searchText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val countries = if (searchText.isEmpty()) {
                repository.getAllCountries()
            } else {
                repository.getCountriesByName(searchText)
            }
            withContext(Dispatchers.Main) {
                _filteredCountries.value = countries.sortedBy { it.name }
            }
        }
    }
}