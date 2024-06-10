package com.florianfabre.countrynews.ui.countriesRelated

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.florianfabre.countrynews.data.model.Country
import com.florianfabre.countrynews.data.repository.CountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Represents the UI state of the country detail.
 *
 * @property errorMessage The error message to be displayed, if any.
 * @property isLoading A boolean indicating whether the data is loading.
 * @property isFavourite A boolean indicating if the country is a favourite.
 */
data class CountryDetailUiState(
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isFavourite: Boolean = false
)


/**
 * ViewModel for the country detail screen.
 *
 * @property savedStateHandle The saved state handle for the ViewModel.
 * @property repository The repository to fetch the country data from.
 */
class CountryDetailViewModel(
    val savedStateHandle: SavedStateHandle,
    val repository: CountryRepository
)  : ViewModel() {

    private var _uiState = MutableStateFlow(CountryDetailUiState())
    val uiState: MutableStateFlow<CountryDetailUiState> = _uiState

    private val countryIso2 = checkNotNull(savedStateHandle[CountryDetailDestination.COUNTRY_ISO2]).toString()

    val countryLiveData = MutableLiveData<Country>()


    /**
     * Initialization block that gets executed when the ViewModel is created.
     * It fetches the country by its ISO2 code and checks if it's a favourite.
     */
    init {
        viewModelScope.launch {
            val country = getCountryByIso2(countryIso2)
            country.countryId?.let { isFavourite(it, 1) }
            countryLiveData.postValue(country)
        }
    }


    /**
     * Fetches a country by its ISO2 code.
     *
     * @param iso2 The ISO2 code of the country.
     * @return The country with the given ISO2 code.
     */
    private suspend fun getCountryByIso2(iso2: String) = withContext(Dispatchers.IO) {
        repository.getCountryByIso2(iso2)
    }


    /**
     * Adds a country to the user's favourites.
     *
     * @param countryId The ID of the country to add to the favourites.
     * @param userId The ID of the user adding the country to the favourites.
     */
    fun addFavourite(countryId: Int, userId: Int) {
        viewModelScope.launch {
            repository.addFavourite(countryId, userId)
            _uiState.update { it.copy(isFavourite = true) }
            Log.d("CountryDetailViewModel", "addFavourite")
        }
    }


    /**
     * Removes a country from the favourites.
     *
     * @param countryId The ID of the country.
     * @param userId The ID of the user.
     */
    fun removeFavourite(countryId: Int, userId: Int) {
        viewModelScope.launch {
            repository.removeFavourite(countryId, userId)
            _uiState.update { it.copy(isFavourite = false) }
            Log.d("CountryDetailViewModel", "removeFavourite")
        }
    }


    /**
     * Checks if a country is a favourite.
     *
     * @param countryId The ID of the country.
     * @param userId The ID of the user.
     */
    fun isFavourite(countryId: Int, userId: Int) {
        viewModelScope.launch {
            val isFavourite = repository.isFavourite(countryId, userId)
            _uiState.update { it.copy(isFavourite = isFavourite) }
            Log.d("CountryDetailViewModel", "isFavourite")
        }
    }
}