package com.florianfabre.countrynews.utilities

import com.florianfabre.countrynews.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


/**
 * Singleton object that manages the logged-in user state.
 *
 * @property loggedInUser The Flow of the currently logged-in user.
 */
object SingletonLoggedInUser {

    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser: Flow<User?> get() = _loggedInUser

    /**
     * Gets the currently logged-in user.
     *
     * @return The currently logged-in user, or null if no user is logged in.
     */
    fun getCurrentUser(): User? {
        return _loggedInUser.value
    }

    /**
     * Logs in a user.
     *
     * @param user The user to log in.
     */
    fun logIn(user: User?) {
        _loggedInUser.value = user
    }

    /**
     * Logs out the currently logged-in user.
     */
    fun logOut() {
        _loggedInUser.value = null
    }
}