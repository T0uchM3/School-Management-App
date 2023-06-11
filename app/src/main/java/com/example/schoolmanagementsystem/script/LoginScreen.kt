package com.example.schoolmanagementsystem.script

import android.app.Activity
import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import com.example.schoolmanagementsystem.BuildConfig
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.navbar.Screen


@Composable
fun LoginScreen(navCtr: NavHostController? = null, sharedViewModel: SharedViewModel? = null) {
    val text2 = remember {
        mutableStateOf(TextFieldValue(BuildConfig.LoginMdp))
    }
    val passwordVisible: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val emaiInput = remember {
        mutableStateOf(TextFieldValue(BuildConfig.LoginMail))
    }
    var isClicked by remember { mutableStateOf(false) }
    sharedViewModel?.defineFABClicked(null)
    // Manually handling backpress (to prevent going back inside the app)
    BackPressHandler(navCtr = navCtr)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfff2f2f2)),
    ) {

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
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {

            Row(Modifier.padding(horizontal = 40.dp)) {

                Image(

                    painter = rememberAsyncImagePainter(R.drawable.logovertical),
                    contentDescription = "tests",
                    modifier = Modifier
                        .offset(y = (-0).dp)
                        .scale(1f)
                )
            }
            Text(
                text = "Member Login",
                Modifier.fillMaxWidth(),
                color = Color(0xFF386BA5),
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Add your details to login",
                Modifier.fillMaxWidth(),
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
        }
//        Spacer(modifier = Modifier.weight(1f))

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
                colors = sharedViewModel?.tFColors()!!,
                shape = RoundedCornerShape(30),
                textStyle = MaterialTheme.typography.titleSmall

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
                        Icon(imageVector = image, description, Modifier.scale(0.9f))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp)
                    .padding(bottom = 15.dp),
                colors = sharedViewModel.tFColors(),
                shape = RoundedCornerShape(30),
                textStyle = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Forgot password?",
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 70.dp),
                color = Color.Black,
                textAlign = TextAlign.Right,
                style = MaterialTheme.typography.titleSmall

            )
            Button(
                shape = RoundedCornerShape(30),
                onClick = {
                    loginAPI(navCtr, sharedViewModel!!, emaiInput.value.text, text2.value.text)
                    isClicked = true
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4884C9),
                    contentColor = Color(0xfff2f2f2)
                )

            ) {
                if (isClicked) {
                    LoadingIndicator()
                } else
                    Text(
                        text = "Login",
                        fontSize = 25.sp,
                        modifier = Modifier.padding(vertical = 2.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.surface
                    )
            }
        }

    }
}

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    navCtr: NavHostController? = null,
) {
    val activity = (LocalContext.current as? Activity)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}

@Composable
fun LoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition()

    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = AnimationConstants.DefaultDurationMillis
            }
        )
    )
    CircularProgressIndicator(
        progress = 0.7f,
        modifier = Modifier
            .padding(0.dp)
            .rotate(angle),
        color = Color(0xfff2f2f2),
        strokeWidth = 2.dp,
    )
}

@Preview
@Composable
fun Preview() {
    LoginScreen()
}