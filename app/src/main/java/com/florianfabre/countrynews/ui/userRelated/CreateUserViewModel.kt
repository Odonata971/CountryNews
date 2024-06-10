package com.florianfabre.countrynews.ui.userRelated

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.florianfabre.countrynews.data.model.User
import com.florianfabre.countrynews.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext


/**
 * Represents the UI state of the user creation process.
 *
 * @property errorMessage The error message to be displayed, if any.
 * @property isLoading A boolean indicating whether the data is loading.
 * @property isPasswordVisible A boolean indicating if the password is visible.
 */
data class CreateUserUiState(
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false
)


/**
 * ViewModel for the user creation screen.
 *
 * @property repository The repository to interact with the user data.
 */
class CreateUserViewModel(private val repository: UserRepository): ViewModel() {
    private var _uiState = MutableStateFlow(CreateUserUiState())
    val uiState: StateFlow<CreateUserUiState> = _uiState

    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    val toastMessage = MutableStateFlow("")


    /**
     * Updates the username.
     *
     * @param username The new username.
     */
    fun updateUsername(username: String) {
        this.username = username
    }


    /**
     * Updates the password.
     *
     * @param password The new password.
     */
    fun updatePassword(password: String) {
        this.password = password
    }


    /**
     * Creates a new user account.
     *
     * @return A boolean indicating whether the account was created successfully.
     */
    suspend fun createAccount(): Boolean  {
        _uiState.update {
            it.copy(isLoading = true)
        }
        val user = withContext(Dispatchers.IO) {
            if (repository.getUserByLoginName(username) != null) {
                _uiState.update {
                    it.copy(errorMessage = "Invalid username or password")
                }
                return@withContext false
            }
            val user = User(username = username, password = password)
            repository.addNewUser(user)
        }
        _uiState.update {
            it.copy(isLoading = false)
        }
        toastMessage.value = "Account created successfully"
        return true
    }


    /**
     * Toggles the visibility of the password.
     */
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
}