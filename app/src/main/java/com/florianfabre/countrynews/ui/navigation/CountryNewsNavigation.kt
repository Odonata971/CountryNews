package com.florianfabre.countrynews.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.florianfabre.countrynews.R
import com.florianfabre.countrynews.utilities.SingletonLoggedInUser
import com.florianfabre.countrynews.ui.countriesRelated.CountriesListDestination
import com.florianfabre.countrynews.ui.countriesRelated.CountriesListScreen
import com.florianfabre.countrynews.ui.countriesRelated.CountryDetailDestination
import com.florianfabre.countrynews.ui.countriesRelated.CountryDetailsScreen
import com.florianfabre.countrynews.ui.userRelated.CreateUserDestination
import com.florianfabre.countrynews.ui.userRelated.CreateUserScreen
import com.florianfabre.countrynews.ui.countriesRelated.FavouriteCountriesDestination
import com.florianfabre.countrynews.ui.countriesRelated.FavouriteCountriesScreen
import com.florianfabre.countrynews.ui.userRelated.LoginDestination
import com.florianfabre.countrynews.ui.userRelated.LoginScreen
import com.florianfabre.countrynews.ui.userRelated.ProfileDestination
import com.florianfabre.countrynews.ui.userRelated.ProfileScreen


/**
 * The main navigation structure of the CountryNews application.
 *
 * @param navController The NavController for the application.
 */
@Composable
fun CountryNews(navController: NavHostController = rememberNavController()) {
    Scaffold(
        topBar = {
            CountryNewsBar(
                navController = navController,
                onClick = {  }
            )
    }) { innerPadding ->
        NavHost(navController = navController,
                startDestination = LoginDestination.route,
                modifier = Modifier.padding(innerPadding)) {
            composable(route = LoginDestination.route) {
                LoginScreen(
                    loginSuccessFull = {
                    navController.navigate(CountriesListDestination.route)
                    },
                    createUser = {
                        navController.navigate(CreateUserDestination.route)
                    }
                )
            }
            composable(route = CountriesListDestination.route) {
                CountriesListScreen(
                    onCountrySelected = {
                        navController.navigate("${CountryDetailDestination.route}/${it}")
                    })
            }
            composable( route = CountryDetailDestination.routeWithArgs,
                arguments = listOf(
                    navArgument(CountryDetailDestination.COUNTRY_ISO2) {
                        type = NavType.StringType
                    })
            ) {
                CountryDetailsScreen(asFavourite = {
                    navController.navigate(CountriesListDestination.route)
                })
            }
            composable(route = FavouriteCountriesDestination.route) {
                FavouriteCountriesScreen(
                    onCountrySelected = {
                        navController.navigate("${CountryDetailDestination.route}/${it}")
                    })
            }
            composable(route = CreateUserDestination.route) {
                CreateUserScreen(
                    createSuccessFull = {
                        navController.navigate(LoginDestination.route)
                    }
                )
            }
            composable(route = ProfileDestination.route) {
                ProfileScreen(
                    logOutDelete = {
                        navController.navigate(LoginDestination.route)
                    }
                )
            }
        }
    }
}


/**
 * The top app bar for the CountryNews application.
 *
 * @param navController The NavController for the application.
 * @param modifier The Modifier to be applied to the top app bar.
 * @param onClick The action to be performed when the navigation icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryNewsBar(navController: NavHostController, modifier: Modifier = Modifier, onClick : () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val loggedInUser = SingletonLoggedInUser.loggedInUser.collectAsState(initial = null);

    val routeToTitleMap = mapOf(
        CountriesListDestination.route to stringResource(R.string.countriesListTitle),
        FavouriteCountriesDestination.route to stringResource(R.string.favouriteCountriesTitle),
        LoginDestination.route to stringResource(R.string.loginTitle),
        CreateUserDestination.route to stringResource(R.string.createUserTitle),
        CountryDetailDestination.route to stringResource(R.string.countryDetailTitle),
        ProfileDestination.route to stringResource(R.string.profileTitle)
    )

    // Observe the current back stack entry
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val title = routeToTitleMap[currentRoute] ?: stringResource(R.string.app_name)

    TopAppBar(
        title = { Text(title) },
        modifier = Modifier.background( Color(0xFFF0F0F0)),
        navigationIcon = {
            if (loggedInUser.value != null) {
                IconButton(onClick = {expanded = true}) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    // List of destinations
                    val destinations = listOf("Countries List", "Favourite Countries", "Profile")

                    destinations.forEach { destination ->
                        DropdownMenuItem(
                            text = { Text(destination) },
                            onClick = {
                                val route = when (destination) {
                                    "Countries List" -> CountriesListDestination.route
                                    "Favourite Countries" -> FavouriteCountriesDestination.route
                                    "Profile" -> ProfileDestination.route
                                    else -> LoginDestination.route
                                }
                                navController.navigate(route)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    )
}