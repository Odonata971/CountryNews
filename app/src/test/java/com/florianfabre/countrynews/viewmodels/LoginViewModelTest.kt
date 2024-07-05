package com.florianfabre.countrynews.viewmodels

import com.florianfabre.countrynews.data.repository.UserRepository
import com.florianfabre.countrynews.ui.userRelated.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val userRepository = mock<UserRepository>()

    @Before
    fun setUp() {
        viewModel = LoginViewModel(userRepository)
    }

    @Test
    fun loginSuccess_updatesUiStateCorrectly(): Unit = runBlockingTest {
        // Arrange
        whenever(userRepository.verifyUser("Odonata", "azertyuiop")).thenReturn(true)
        whenever(userRepository.getUserByLoginName("Odonata")).thenReturn(User(0, "Odonata", "hashedPassword"))

        // Act
        viewModel.login()


        // Assert
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.errorMessage.isEmpty())
    }

    private fun runBlockingTest(function: () -> Unit) {

    }

    @Test
    fun loginFailure_updatesUiStateWithError() = runBlockingTest {
        // Arrange
        whenever(userRepository.verifyUser("Odonata", "wrongPassword")).thenReturn(false)

        // Act
        viewModel.login()

        // Assert
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.errorMessage.isEmpty())
    }
}