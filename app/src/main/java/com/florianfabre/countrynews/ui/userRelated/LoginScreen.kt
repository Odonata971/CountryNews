package com.florianfabre.countrynews.ui.userRelated

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.florianfabre.countrynews.R
import com.florianfabre.countrynews.utilities.AppViewModelProvider
import com.florianfabre.countrynews.ui.navigation.NavigationDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Represents the navigation destination for the login screen.
 */
object LoginDestination : NavigationDestination {
    override val route = "Login"
    override val titleRes = R.string.app_name
}


/**
 * Composable function that represents the login screen.
 *
 * @param modifier Modifier that will be applied to the composable.
 * @param loginViewModel LoginViewModel that will be used to handle the login logic.
 * @param loginSuccessFull Function that will be called when the login is successful.
 * @param createUser Function that will be called when the user wants to create an account.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
    loginSuccessFull: () -> Unit,
    createUser : () -> Unit
) {
    val state = loginViewModel.uiState.collectAsState()
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.countryapplogo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(200.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = loginViewModel.username,
                    onValueChange = { loginViewModel.updateUsername(it) },
                    label = { Text(stringResource(R.string.username)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = loginViewModel.password,
                    onValueChange = { loginViewModel.updatePassword(it) },
                    label = { Text(stringResource(R.string.password)) },
                    visualTransformation = if (state.value.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { loginViewModel.togglePasswordVisibility() }) {
                            Icon(
                                imageVector = if (state.value.isPasswordVisible) Icons.Filled.Warning else Icons.Filled.Lock,
                                contentDescription = if (state.value
                                    .isPasswordVisible) stringResource(R
                                        .string.hidePassword)  else
                                            stringResource(R.string.showPassword)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            val user = loginViewModel.login()
                            if (user != null) {
                                loginSuccessFull()
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.login))
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.noAccount),
                    style = MaterialTheme.typography.bodyLarge
                )
                TextButton(onClick = { createUser() }) {
                    Text(stringResource(R.string.createAccount))
                }
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = state.value.errorMessage ?: "",
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}