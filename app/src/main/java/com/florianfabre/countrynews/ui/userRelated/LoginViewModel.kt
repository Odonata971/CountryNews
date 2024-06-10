package com.florianfabre.countrynews.ui.userRelated

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.florianfabre.countrynews.utilities.SingletonLoggedInUser
import com.florianfabre.countrynews.data.model.User
import com.florianfabre.countrynews.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext


/**
 * Represents the UI state of the login process.
 *
 * @property errorMessage The error message to be displayed, if any.
 * @property isLoading A boolean indicating whether the data is loading.
 * @property isPasswordVisible A boolean indicating if the password is visible.
 */
data class LoginUiState(
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false
)


/**
 * ViewModel for the login screen.
 *
 * @param repository The repository to be used.
 */
class LoginViewModel(private val repository: UserRepository): ViewModel() {
    private var _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    var username by mutableStateOf("Odonata")
        private set
    var password by mutableStateOf("azertyuiop")
        private set
    fun updateUsername(username: String) {
        this.username = username
    }
    fun updatePassword(password: String) {
        this.password = password
    }


    /**
     * Logs in the user.
     *
     * @return The logged in user, if successful.
     */
    suspend fun login(): User?  {
        _uiState.update {
            it.copy(isLoading = true)
        }
        val user = withContext(Dispatchers.IO) {
            repository.getUser(username, password)
        }
        if (user == null) {
            _uiState.update {
                it.copy(errorMessage = "Invalid username or password")
            }
        }
        SingletonLoggedInUser.logIn(user)
        _uiState.update {
            it.copy(isLoading = false)
        }
        return user
    }


    /**
     * Toggles the visibility of the password.
     */
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
}