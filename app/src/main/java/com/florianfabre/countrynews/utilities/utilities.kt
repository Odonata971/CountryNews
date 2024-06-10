package com.florianfabre.countrynews.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.caverock.androidsvg.SVG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.MalformedURLException
import java.net.URL


/**
 * Loads an SVG image from a URL and converts it to a Bitmap.
 *
 * @param context The context of the app.
 * @param url The URL of the SVG image.
 * @return The Bitmap representation of the SVG image, or null if the URL is invalid.
 */
suspend fun loadSvgFromUrl(context: Context, url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        if(isValidUrl(url)) {
            val svg = SVG.getFromInputStream(URL(url).openStream())
            val picture = svg.renderToPicture()
            val bitmap = Bitmap.createBitmap(picture.width, picture.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            picture.draw(canvas)
            bitmap
        } else {
            null
        }
    }
}


/**
 * Check if a URL is valid.
 *
 * @param url The URL to check.
 * @return True if the URL is valid, false otherwise.
 */
fun isValidUrl(url: String): Boolean {
    return try {
        URL(url)
        true
    } catch (e: MalformedURLException) {
        false
    }
}


/**
 * Check if the device is connected to the internet, Ethernet, Wifi or Cellular
 * @param context The context of the app
 * @return True if the device is connected to the internet, false otherwise
 */
fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return capabilities != null &&
            (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
}