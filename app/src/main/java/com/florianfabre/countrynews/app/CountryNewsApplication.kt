package com.florianfabre.countrynews.app

import android.app.Application
import com.florianfabre.countrynews.data.container.AppContainer
import com.florianfabre.countrynews.data.container.AppDataContainer


/**
 * Custom application class for the CountryNews app.
 *
 * @property container The AppContainer instance used by the rest of the classes to obtain dependencies.
 */
class CountryNewsApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer


    /**
     * Called when the application is starting, before any activity, service, or receiver objects have been created.
     * Initializes the AppContainer instance.
     */
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}