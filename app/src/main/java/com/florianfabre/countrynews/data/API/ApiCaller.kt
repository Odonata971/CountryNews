package com.florianfabre.countrynews.data.API

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.florianfabre.countrynews.app.CountryNewsApplication
import com.florianfabre.countrynews.data.model.Country
import com.florianfabre.countrynews.data.repository.CountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


/**
 * ViewModel for making API calls.
 *
 * @property countriesRepository The repository for countries data.
 */
class ApiCaller(private val countriesRepository: CountryRepository) : ViewModel() {


    /**
     * Represents the response from the API.
     *
     * @property data The list of countries without IDs.
     * @property error Boolean indicating if there was an error.
     * @property msg The error message, if any.
     */
    data class ApiResponse(
        val data: List<CountryWithoutId>?,
        val error: Boolean?,
        val msg: String?
    )


    /**
     * Represents a country without an ID.
     *
     * @property name The name of the country.
     * @property iso2 The ISO2 code of the country.
     * @property iso3 The ISO3 code of the country.
     * @property currency The currency of the country.
     * @property flag The flag of the country.
     * @property capital The capital of the country.
     * @property dialCode The dial code of the country.
     */
    data class CountryWithoutId(
        val name: String,
        val iso2: String,
        val iso3: String,
        val currency: String,
        val flag: String,
        val capital: String,
        val dialCode: String
    )

    private val BASE_URL = "https://countriesnow.space/api/v0.1/"

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val countriesApi: RestCountriesService = retrofit.create(
        RestCountriesService::class.java)

    private val countriesLiveData = MutableLiveData<List<Country>>() // LiveData to hold the list of countries


    /**
     * Interface for the REST Countries API service.
     */
    interface RestCountriesService {

        @GET("countries/info?returns=iso2,currency,image,flag,capital,iso3,dialCode,name")


        /**
         * Fetches all countries from the API.
         *
         * @return The API response containing the list of countries.
         */
        suspend fun getAllCountries(): ApiResponse
    }


    /**
     * Fetches all countries from the API and updates the LiveData.
     */
    fun getAllCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("CountryViewModel", "URL: ${retrofit.baseUrl()}")
                val response = countriesApi.getAllCountries()
                if (response.error == true) {
                    Log.d("CountryViewModel", "Error message from API: ${response.msg}")
                }
                Log.d("CountryViewModel", "Full Response: ${response.data}") // Log the full response
                var countries: List<Country> = emptyList()

                var counter : Int = 0
                // get the countries from the response
                response.data?.let { countryList ->
                    countries = countryList.map { country ->
                        Country(
                            countryId = counter++,
                            name = country.name,
                            iso2 = country.iso2,
                            iso3 = country.iso3,
                            currency = country.currency,
                            flag = country.flag,
                            capital = country.capital,
                            dialCode = country.dialCode
                        )
                    }
                }

                // post the list of countries to the LiveData
                countriesLiveData.postValue(countries)

                // insert the countries into the database
                countriesRepository.insertAll(countries)

                // retrieve all countries from the database and log them
                val allCountries = countriesRepository.getAllCountries()
                Log.d("CountryViewModel", "All countries in the database: $allCountries")

            } catch (e: Exception) {
                Log.d("CountryViewModel", "Error: ${e.message}")
                // handle the error, post a empty list of countries
                countriesLiveData.postValue(emptyList())
            }
        }
    }


    /**
     * Factory for creating an instance of the ApiCaller ViewModel.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CountryNewsApplication)
                val countriesRepository = application.container.countryRepository
                ApiCaller(countriesRepository = countriesRepository)
            }
        }
    }
}