package com.florianfabre.countrynews.ui.userRelated

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.florianfabre.countrynews.utilities.SingletonLoggedInUser
import com.florianfabre.countrynews.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Represents the UI state of the profile screen.
 *
 * @property errorMessage The error message to be displayed, if any.
 * @property isLoading A boolean indicating whether the data is loading.
 * @property isPasswordVisible A boolean indicating if the password is visible.
 * @property showDialogBox A boolean indicating if the dialog box is visible.
 */
data class ProfileUiState(
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val showDialogBox: Boolean = false
)


/**
 * ViewModel for the profile screen.
 *
 * @param repository The repository to fetch user data from.
 */
class ProfileViewModel(private val repository: UserRepository): ViewModel() {
    private var _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    val toastMessageLogOut = MutableStateFlow("")
    val toastMessageDelete = MutableStateFlow("")

    val userN = SingletonLoggedInUser.getCurrentUser()?.username
    val passW = SingletonLoggedInUser.getCurrentUser()?.password

    var username by mutableStateOf(userN ?: "")
        private set
    var password by mutableStateOf(passW ?:"")
        private set


    /**
     * Init block to set the initial values for the toast messages.
     */
    init {
        toastMessageLogOut.value = "Logged out successfully"
        toastMessageDelete.value = "Account deleted successfully"
    }


    /**
     * Toggles the visibility of the password.
     */
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }


    /**
     * Deletes the user.
     */
    fun deleteUser() {
        viewModelScope.launch {
            try {
                Log.d("ProfileViewModel", "deleteUser: $username")
                withContext(Dispatchers.IO) {
                    repository.deleteUser(username)
                    val user = repository.getUserByLoginName(username)
                    Log.d("ProfileViewModel", "deleteUser: $user")
                }
                SingletonLoggedInUser.logOut()

                // Handle any actions after successful deletion, like navigating to a different screen
            } catch (e: Exception) {
                // Handle any errors during deletion, like showing an error message
                Log.e("ProfileViewModel", "Exception : ${e.message}")
            }
        }
    }


    /**
     * Toggles the visibility of the dialog box.
     */
    fun toggleDialog() {
        _uiState.update { it.copy(showDialogBox = !it.showDialogBox) }
    }
}