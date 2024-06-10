package com.florianfabre.countrynews.ui.userRelated

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.florianfabre.countrynews.R
import com.florianfabre.countrynews.utilities.SingletonLoggedInUser
import com.florianfabre.countrynews.utilities.ViewModelProvider
import com.florianfabre.countrynews.ui.navigation.NavigationDestination


/**
 * Represents the navigation destination for the profile screen.
 */
object ProfileDestination : NavigationDestination {
    override val route = "Profile"
    override val titleRes = R.string.app_name
}


/**
 * Profile screen composable function.
 *
 * @param modifier Modifier
 * @param profileViewModel ProfileViewModel
 * @param logOutDelete Function
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel(factory = ViewModelProvider.Factory),
    logOutDelete : () -> Unit
    ) {
    val state = profileViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val toastMessageLogOut = profileViewModel.toastMessageLogOut.collectAsState()
    val toastMessageDelete = profileViewModel.toastMessageDelete.collectAsState()

    if (state.value.showDialogBox) {
        AlertDialog(
            onDismissRequest = { profileViewModel.toggleDialog() },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete your account?") },
            confirmButton = {
                Button(onClick = {
                    profileViewModel.deleteUser()
                    profileViewModel.toggleDialog()
                    logOutDelete()
                    if (toastMessageDelete.value.isNotEmpty()) {
                        val message = if (toastMessageDelete.value == "Account deleted successfully") {
                            context.getString(R.string.deleteSuccess)
                        } else {
                            toastMessageDelete.value
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        profileViewModel.toastMessageDelete.value = ""
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { profileViewModel.toggleDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = profileViewModel.username,
                onValueChange = {  },
                label = { Text(stringResource(id = R.string.username)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false
            )

            Spacer(modifier = Modifier.height(25.dp))
            OutlinedTextField(
                value = profileViewModel.username,
                onValueChange = { },
                label = { Text(stringResource(id = R.string.password)) },
                visualTransformation = if (state.value.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { profileViewModel.togglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (state.value.isPasswordVisible) Icons.Filled.Warning else Icons.Filled.Lock,
                            contentDescription = if (state.value
                                .isPasswordVisible) stringResource(R.string
                                    .hidePassword) else  stringResource(R
                                        .string.showPassword)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false
            )

            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = stringResource(id = R.string.logoutText),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    logOutDelete()
                    SingletonLoggedInUser.logOut()
                    if (toastMessageLogOut.value.isNotEmpty()) {
                        val message = if (toastMessageLogOut.value == "Logged out successfully") {
                            context.getString(R.string.logOutSuccess)
                        } else {
                            toastMessageLogOut.value
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        profileViewModel.toastMessageLogOut.value = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_red))
            ) {
                Text(stringResource(id = R.string.logout))
            }


            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = stringResource(id = R.string.deleteAccountText),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { profileViewModel.toggleDialog() },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.warm_red))
            ) {
                Text(stringResource(id = R.string.deleteAccount))
            }
        }
    }
}


/**
 * Profile screen preview composable function.
 *
 * @param modifier Modifier
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreenPreview(modifier : Modifier = Modifier) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(stringResource(id = R.string.username)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
            Spacer(modifier = Modifier.height(25.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(id = R.string.password)) },
                visualTransformation = if (showPassword) PasswordVisualTransformation() else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = if (showPassword) stringResource(
                                id = R.string.hidePassword) else
                                    stringResource(R.string.showPassword)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = stringResource(id = R.string.logoutText),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle delete account logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_red))
            ) {
                Text(stringResource(id = R.string.logout))
            }

            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = stringResource(id = R.string.deleteAccountText),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle delete account logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.warm_red))
            ) {
                Text(stringResource(id = R.string.deleteAccount))
            }
        }
    }
}


/**
 * Profile screen preview composable function.
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreviewProfile() {
    ProfileScreenPreview()
}