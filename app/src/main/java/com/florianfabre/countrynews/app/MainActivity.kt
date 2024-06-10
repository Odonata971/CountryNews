package com.florianfabre.countrynews.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.florianfabre.countrynews.data.model.User
import com.florianfabre.countrynews.data.API.ApiCaller
import com.florianfabre.countrynews.utilities.isOnline
import com.florianfabre.countrynews.ui.theme.CountryNewsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * The main activity for the CountryNews application.
 */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is starting.
     * This is where most initialization should go.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get an instance of CountryViewModel
        val application = (application as CountryNewsApplication)
        val apiCaller = ViewModelProvider(this, ApiCaller.Factory)[ApiCaller::class.java]

        // Call getAllCountries from a coroutine
        lifecycleScope.launch {
            val userRepository = application.container.userRepository
            val users = listOf(
                User(userId = 0, username = "Odonata", password = "azertyuiop"),
                User(userId = 1,username = "dirkhostens", password =  "password"),
                // add more users here
            )
            withContext(Dispatchers.IO) {
                userRepository.insertUsers(users)
            }
            apiCaller.getAllCountries()
        }
        if (!isOnline(this)) {
            Toast.makeText(this, "No Internet connexion, the application will close", Toast.LENGTH_LONG).show()
            finish()
        }
        setContent {
            CountryNewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CountryNews()
                }
            }
        }
    }
}