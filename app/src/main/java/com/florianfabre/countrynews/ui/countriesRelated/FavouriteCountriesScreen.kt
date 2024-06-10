package com.florianfabre.countrynews.ui.countriesRelated

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.florianfabre.countrynews.R
import com.florianfabre.countrynews.data.model.Country
import com.florianfabre.countrynews.utilities.AppViewModelProvider
import com.florianfabre.countrynews.ui.navigation.NavigationDestination


/**
 * Represents the navigation destination for favourite countries.
 */
object FavouriteCountriesDestination : NavigationDestination {
    override val route = "FavouriteCountries"
    override val titleRes = R.string.app_name
}


/**
 * Screen displaying the favourite countries.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param viewModel The ViewModel to be used for the screen.
 * @param onCountrySelected The callback to be invoked when a country is selected.
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FavouriteCountriesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavouriteCountriesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onCountrySelected: (String) -> Unit = {}
) {
    val state = viewModel.uiState.collectAsState()
    val filteredCountries = viewModel.filteredCountries.collectAsState()

    Log.d("FavouriteCountriesScreen", "Recomposing with countries: ${filteredCountries.value}")

    LaunchedEffect(viewModel) {
        viewModel.updateCountries(viewModel.searchText.value)
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = viewModel.searchText.value,
                onValueChange = { viewModel.updateSearchText(it) },
                label = { Text(stringResource(id = R.string.search)) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                singleLine = true
            )
            if (filteredCountries.value.isEmpty()) {
                Text(
                    text = if(viewModel.searchText.value.isEmpty())
                        stringResource(R.string.noFavouriteCountriesAdded)
                    else stringResource(R.string.noFavouriteCountriesFound),
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography
                        .bodyLarge.copy(color = MaterialTheme.colorScheme
                            .onBackground.copy(alpha = 0.5f))
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(filteredCountries.value) { country ->
                        CountryItem(
                            country = country,
                            onCountrySelected = onCountrySelected
                        )
                    }
                }
            }
        }
    }
}


/**
 * Test version of the screen displaying the favourite countries.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param onCountrySelected The callback to be invoked when a country is selected.
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FavouriteCountriesScreenTest(
    modifier: Modifier = Modifier,
    onCountrySelected: (String) -> Unit = {}
) {
    val filteredCountries: List<Country> = listOf(
        Country(
            countryId = 1,
            name = "France",
            iso2 = "FR",
            iso3 = "FRA",
            currency = "Euro",
            flag = null,
            capital = "Paris",
            dialCode = "+33"
        ),
        Country(
            countryId = 2,
            name = "Germany",
            iso2 = "DE",
            iso3 = "DEU",
            currency = "Euro",
            flag = null,
            capital = "Berlin",
            dialCode = "+49"
        ),
        Country(
            countryId = 3,
            name = "Italy",
            iso2 = "IT",
            iso3 = "ITA",
            currency = "Euro",
            flag = null,
            capital = "Rome",
            dialCode = "+39"
        )
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = "",
                onValueChange = { },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                singleLine = true
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(filteredCountries) { country ->
                    CountryItem(
                        country = country,
                        onCountrySelected = onCountrySelected
                    )
                }
            }
        }
    }
}


/**
 * Preview function for the FavouriteCountriesScreenTest composable.
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FavouriteCountriesScreenTest()
}
