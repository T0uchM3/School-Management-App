package com.example.schoolmanagementsystem.script

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.script.navbar.Screen


@Composable
fun LoginScreen(navCtr: NavHostController?, sharedViewModel: SharedViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
//            .padding(vertical = 50.dp, horizontal = 50.dp)
        ) {
            Text(
                text = " School \n Management \n System",
                color = Color.Black,
                fontSize = 30.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .clickable { navCtr?.navigate(Screen.Home.route) },
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
            )
        }
        Box(
            modifier = Modifier
//            .fillMaxSize()
                .background(Color.White)
                .padding(vertical = 50.dp, horizontal = 50.dp)
        ) {
            val emaiInput = remember {
                mutableStateOf(TextFieldValue(""))
            }
            OutlinedTextField(value = emaiInput.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { emaiInput.value = it },
                singleLine = true,
                label = { Text("Mail") })
            val text2 = remember {
                mutableStateOf(TextFieldValue(""))
            }
            val passwordVisible: MutableState<Boolean> = remember {
                mutableStateOf(false)
            }

            OutlinedTextField(
                value = text2.value,
                onValueChange = { text2.value = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible.value)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description =
                        if (passwordVisible.value) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(imageVector = image, description)
                    }
                },
                modifier = Modifier.padding(vertical = 80.dp)
            )
            var isClicked by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    loginAPI(navCtr, sharedViewModel)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .scale(1.2f),
                border = BorderStroke(1.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray)

            ) {
                Text(text = "Login")
            }
        }
    }
}


@Preview
@Composable
fun Preview() {
    LoginScreen(null, TODO())
}