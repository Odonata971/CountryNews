package com.florianfabre.countrynews.ui.countriesRelated

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import com.florianfabre.countrynews.R
import com.florianfabre.countrynews.data.model.Country
import com.florianfabre.countrynews.utilities.AppViewModelProvider
import com.florianfabre.countrynews.utilities.loadSvgFromUrl
import com.florianfabre.countrynews.ui.navigation.NavigationDestination
import kotlinx.coroutines.async


/**
 * Object that represents the destination for the countries list in the navigation graph.
 */
object CountriesListDestination : NavigationDestination {
    override val route = "CountriesList"
    override val titleRes = R.string.app_name
}


/**
 * Composable function that represents the countries list screen.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param viewModel ViewModel that provides the data for the screen.
 * @param onCountrySelected Callback that gets triggered when a country is selected.
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CountriesListScreen(
    modifier: Modifier = Modifier,
    viewModel: CountriesListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onCountrySelected: (String) -> Unit = {}
) {
    val state = viewModel.uiState.collectAsState()
    val filteredCountries = viewModel.filteredCountries.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = viewModel.searchText.value,
                onValueChange = { viewModel.updateSearchText(it) },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                singleLine = true
            )
            if (filteredCountries.value.isEmpty()) {
                Text(
                    text =if(viewModel.searchText.value.isEmpty()) "Error loading countries, please reload the app"
                    else "No countries found for this research",
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
 * Composable function that represents a single country item in the list.
 *
 * @param country The country data to be displayed.
 * @param onCountrySelected Callback that gets triggered when the country item is selected.
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun CountryItem(country: Country, onCountrySelected: (String) -> Unit = {}) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }


    LaunchedEffect(country.flag) {
        bitmap = if (country.flag != null) {
            coroutineScope.async { loadSvgFromUrl(context, country.flag) }.await()
        } else {
            coroutineScope.async { loadSvgFromUrl(context, "https://upload.wikimedia.org/wikipedia/commons/b/b0/No_flag.svg") }.await()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {onCountrySelected(country.iso2) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = country.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Capital: ${country.capital?:"N/A"}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


/**
 * Composable function that provides a preview of the countries list screen.
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreviewCountriesList() {
    CountriesListScreen()
}
