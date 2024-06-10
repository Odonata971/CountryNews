package com.florianfabre.countrynews.ui.userRelated

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.florianfabre.countrynews.R
import com.florianfabre.countrynews.utilities.ViewModelProvider
import com.florianfabre.countrynews.ui.navigation.NavigationDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Represents the navigation destination for creating a user.
 */
object CreateUserDestination : NavigationDestination {
    override val route = "CreateUser"
    override val titleRes = R.string.app_name
}


/**
 * Composable function for the create user screen.
 *
 * @param modifier Modifier to apply to this layout node.
 * @param createUserViewModel ViewModel for the create user screen.
 * @param createSuccessFull Callback to be called when the user is created successfully.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateUserScreen(
    modifier: Modifier = Modifier,
    createUserViewModel: CreateUserViewModel = viewModel(factory = ViewModelProvider.Factory),
    createSuccessFull: () -> Unit
) {
    val state = createUserViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val toastMessage = createUserViewModel.toastMessage.collectAsState()

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
                    value = createUserViewModel.username,
                    onValueChange = { createUserViewModel.updateUsername(it) },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = createUserViewModel.password,
                    onValueChange = { createUserViewModel.updatePassword(it) },
                    label = { Text("Password") },
                    visualTransformation = if (state.value.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { createUserViewModel.togglePasswordVisibility() }) {
                            Icon(
                                imageVector = if (state.value.isPasswordVisible) Icons.Filled.Warning else Icons.Filled.Lock,
                                contentDescription = if (state.value.isPasswordVisible) "Hide password" else "Show password"
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
                            val isCreated = createUserViewModel.createAccount()
                            if (isCreated) {
                                createSuccessFull()
                            }
                        }
                    }
                ) {
                    Text("Create Account")
                }

                LaunchedEffect(toastMessage.value) {
                    if (toastMessage.value.isNotEmpty()) {
                        val message = if (toastMessage.value == "Account created successfully") {
                            context.getString(R.string.accountCreated)
                        } else {
                            toastMessage.value
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        createUserViewModel.toastMessage.value = ""
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = state.value.errorMessage ?: "",
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                //To be the same height as the login page
                Spacer(modifier = Modifier.height(98.dp))
            }
        }
    )
}


/**
 * Preview function for the create user screen.
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreviewCreateUserScreen() {
    CreateUserScreen (createSuccessFull = {})
}