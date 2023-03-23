package com.cg.lib.socialloginlib.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.cg.lib.socialloginlib.R

/**
 * Composable that provides the login card having email & password field
 * and button Login
 * @param eventListener-> event callback on button click
 * */
@Composable
fun LoginCard(eventListener: (Int) -> Unit) {

    val email = remember { mutableStateOf<String>("") }
    val password = remember { mutableStateOf<String>("") }
    val passwordVisible = remember { mutableStateOf<Boolean>(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email.value,
                onValueChange = { newValue ->
                    email.value = newValue
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                placeholder = { Text(text = "user@email.com") },
                label = { Text(text = "Email ID") }
            )
            Spacer(modifier = Modifier.padding(4.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password.value,
                onValueChange = { newValue ->
                    password.value = newValue
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Password") },
                maxLines = 1,
                trailingIcon = {

                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        IconPassword(visible = passwordVisible.value)
                    }
                }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = {
                    eventListener(0)
                }) {
                Text(
                    text = "Login",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

/**
 * Composable function to define the password hide/show button view inside field
 * @param visible-> boolean value that will hide/show the password in password field
 * */
@Composable
fun IconPassword(visible: Boolean) {
    // Please provide localized description for accessibility services
    val description = if (visible) "Hide password" else "Show password"

    val image = if (visible)
        Icon(
            painter = painterResource(id = R.drawable.ic_password),
            contentDescription = description
        )
    else
        Icon(
            painter = painterResource(id = R.drawable.ic_password_off),
            contentDescription = description
        )

}