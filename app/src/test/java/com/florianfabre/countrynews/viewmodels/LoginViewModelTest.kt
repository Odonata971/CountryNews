package com.florianfabre.countrynews.viewmodels

import com.florianfabre.countrynews.data.model.User
import com.florianfabre.countrynews.data.repository.UserRepository
import com.florianfabre.countrynews.ui.userRelated.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val userRepository = mockk<UserRepository>()

    @Before
    fun setUp() {
        viewModel = LoginViewModel(userRepository)
        // Ensure this stubbing matches the call within your test
        coEvery { userRepository.verifyUser("Odonata", "azertyuiop") } returns true
    }

    @Test
    fun loginSuccess_updatesUiStateCorrectly() = runTest {
        // Arrange
        coEvery { userRepository.verifyUser("Odonata", "azertyuiop") } returns true
        coEvery { userRepository.getUserByLoginName("Odonata") } returns User(0, "Odonata", "hashedPassword")

        // Act
        viewModel.login()


        // Assert
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.errorMessage.isEmpty())
    }

    @Test
    fun loginFailure_updatesUiStateWithError() = runTest {
        // Arrange
        coEvery {userRepository.verifyUser("Odonata", "wrongPassword")} returns false

        // Act
        viewModel.login()

        // Assert
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.errorMessage.isEmpty())
    }
}