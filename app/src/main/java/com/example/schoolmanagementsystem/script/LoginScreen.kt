package com.example.schoolmanagementsystem.script

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import com.example.schoolmanagementsystem.R


@Composable
fun LoginScreen(navCtr: NavHostController? = null, sharedViewModel: SharedViewModel? = null) {
    val text2 = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val passwordVisible: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val emaiInput = remember {
        mutableStateOf(TextFieldValue(""))
    }
    var isClicked by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
//            .background(Color.Yellow)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components {
//                    if (SDK_INT >= 28) {
//                        add(ImageDecoderDecoder.Factory())
//                    } else {
                add(GifDecoder.Factory())
//                    }
            }
            .build()

        Image(
            painter = rememberAsyncImagePainter(R.drawable.fader, imageLoader),
            contentDescription = "tests",
            Modifier
                .scale(1f)
                .weight(1f)
        )
        Image(

            painter = rememberAsyncImagePainter(R.drawable.schoomanager),
            contentDescription = "tests",
            modifier = Modifier
                .offset(y = (-0).dp)
                .scale(1f)
        )
        Spacer(modifier = Modifier.weight(1f))

        Column(
            Modifier
//                .background(Color.Blue)
                .fillMaxWidth()
//                .weight(1f)
                .padding(horizontal = 50.dp)
                .padding(top = 50.dp)
        ) {
            OutlinedTextField(
                value = emaiInput.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { emaiInput.value = it },
                singleLine = true,
                label = { Text("Your email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )

            OutlinedTextField(
                value = text2.value,
                onValueChange = { text2.value = it },
                label = { Text("Your password") },
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
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,

                    )
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 0.dp)
                .weight(1f)
                .padding(top = 50.dp, bottom = 0.dp)
        ) {
            Button(
                onClick = {
                    loginAPI(navCtr, sharedViewModel!!)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                border = BorderStroke(1.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray)

            ) {
                Text(text = "LOGIN")
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 30.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            Spacer(Modifier.weight(1f))
            Image(
                alpha = 150f,
                painter = rememberAsyncImagePainter(R.drawable.trees),
                contentDescription = "tree",
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    LoginScreen()
}