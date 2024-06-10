package com.florianfabre.countrynews.ui.countriesRelated

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.florianfabre.countrynews.R
import com.florianfabre.countrynews.utilities.SingletonLoggedInUser
import com.florianfabre.countrynews.data.model.Country
import com.florianfabre.countrynews.utilities.AppViewModelProvider
import com.florianfabre.countrynews.utilities.loadSvgFromUrl
import com.florianfabre.countrynews.ui.navigation.NavigationDestination
import kotlinx.coroutines.async

object CountryDetailDestination : NavigationDestination {
    override val route = "CountryDetail"
    override val titleRes = R.string.app_name
    const val COUNTRY_ISO2 = "item"
    val routeWithArgs = "$route/{$COUNTRY_ISO2}"
}

@Composable
fun CountryDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: CountryDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    asFavourite :() -> Unit = {}) {

    val uiState = viewModel.uiState.collectAsState();
    val country  by viewModel.countryLiveData.observeAsState()
    val isFavourite = uiState.value.isFavourite
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(country?.flag) {
            bitmap = if (country?.flag != null) {
                coroutineScope.async { country!!.flag?.let { loadSvgFromUrl(context, it) } }.await()
            } else {
                coroutineScope.async { loadSvgFromUrl(context, "https://upload.wikimedia.org/wikipedia/commons/b/b0/No_flag.svg") }.await()
            }
        }


    //Log.d("CountryDetailScreen", "CountryDetailsScreen: ")
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Country Name
            //Log.d("CountryItem", "CountryItem: ${country.name}")
            if (country != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(2.dp, Color.Black, RoundedCornerShape(4.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween

                ){

                    Text(
                        text = country!!.name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = {
                            if (isFavourite) {
                                country!!.countryId?.let { countryId ->
                                    SingletonLoggedInUser.getCurrentUser()?.let { user ->
                                        user.userId?.let {
                                            viewModel.removeFavourite(
                                                countryId,
                                                it
                                            )
                                        }
                                    }
                                } // replace with actual countryId and userId
                            } else {
                                country!!.countryId?.let { countryId ->
                                    SingletonLoggedInUser.getCurrentUser()?.let { user ->
                                        user.userId?.let {
                                            viewModel.addFavourite(
                                                countryId,
                                                it
                                            )
                                        }
                                    }
                                }// replace with actual countryId and userId
                            }
                        }
                    ) {
                        Icon(
                            painter = if (isFavourite) painterResource(id = R.drawable.vraistar) else painterResource(id = R.drawable.vraiemptystar),
                            contentDescription = if (isFavourite) "Remove from favourites" else "Add to favourites",
                            //TODO : change the size of the icon
                            modifier = Modifier.size(40.dp)
                        )
                    }

                }

            }

            // Image
            bitmap?.let {
                if (country != null) {
                    Image(
                        bitmap = it.asImageBitmap(), // Replace with your image resource
                        contentDescription = "${country!!.name} Flag",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp)
                    )
                }
            }
            // General Information
            Text(
                text = "General Informations",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Capital
            if (country != null) {
                InfoItem(label = "Capital:", value = country!!.capital ?: "N/A", country = country!!)
                // Currency
                InfoItem(label = "Currency:", value = country!!.currency ?: "N/A", country = country!!)
                // ISO 2
                InfoItem(label = "ISO 2:", value = country!!.iso2, country = country!!)
                // ISO 3
                InfoItem(label = "ISO 3:", value = country!!.iso3, country = country!!)
                // Dial Code
                InfoItem(label = "Dial Code:", value = country!!.dialCode?: "N/A", country = country!!)
            }
        }
    }
}

@Composable
fun BodyContent( modifier : Modifier = Modifier ) {
    //Log.d("CountryDetailScreen", "CountryDetailsScreen: ")
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Country Name
            Log.d("CountryItem", "CountryItem: ${"Afghanistan"}")
            Text(
                text = "Afghanistan",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )

            // Image
            Image(
                 painter = painterResource(R.drawable.flag_of_bolivia), // Replace with your image resource
                 contentDescription = "Country Flag",
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(200.dp)
                     .padding(16.dp)
             )

            // General Information
            Text(
                text = "General Informations",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )

            val country = Country(0,"Afghanistan", "AG", "AFG", "Rouble", "image a la con", "Kaboul", "93")
            // Capital
            InfoItem(label = "Capital:", value = "Kaboul", country = country)

            // Currency
            InfoItem(label = "Currency:", value = "Rouble", country = country)

            // ISO 2
            InfoItem(label = "ISO 2:", value = "AG", country = country)

            // ISO 3
            InfoItem(label = "ISO 3:", value = "AFG", country = country)

            // Dial Code
            InfoItem(label = "Dial Code:", value = "+93", country = country)
        }
    }
}

@Composable
fun InfoItem(label: String, value: String, country: Country) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),

    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(0.dp)) // Add space here
        Text(
            text = if (label == "Dial Code:" && value.length in 2..3) "+ $value" else value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CountryDetailScreenPreview() {
    BodyContent()
}