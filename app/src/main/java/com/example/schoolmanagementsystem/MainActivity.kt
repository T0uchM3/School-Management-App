package com.example.schoolmanagementsystem

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.schoolmanagementsystem.script.SharedViewModel
//import com.example.schoolmanagmentsystem.script.navbar.NavGraph
import com.example.schoolmanagementsystem.script.navbar.RootNavGraph
import com.example.schoolmanagementsystem.ui.theme.AppTheme

sealed class Destination(val route: String) {
    object LoginScreen : Destination("loginScreen")
    object HomeScreen : Destination("homeScreen")
    object StaffScreen : Destination("staffScreen")
}

val isKeyboardOpen: MutableState<Boolean> = mutableStateOf(false)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val sysbar = WindowInsets.systemBars
            val bottomBarHeight = with(LocalDensity.current) {
                sysbar.getBottom(this).toDp()
            }
            val statusBarHeight = with(LocalDensity.current) {
                sysbar.getTop(this).toDp()
            }
            val isDarkTheme = remember { mutableStateOf(false) }
            val sharedViewModel: SharedViewModel = viewModel()
            sharedViewModel.setWindow2(window)

            AppTheme(darkTheme = isDarkTheme.value, sharedViewModel) {

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        // Prevent the ui to be drawn under the system bottom bar, so we rised it
                        .padding(bottom = bottomBarHeight + 0.dp)
                ) {
                    // Some ducttape stuff going on here, to make sure the status bar facade is holding ;)
                    KeyboardStatus()
                    // Used 2 nested rows here cause having 1 row seems like it fill the whole surface
                    Row(
                        Modifier
                            .height(4.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = if (sharedViewModel.fabClicked != null)
                                        listOf(Color(0xFF4884C9), Color(0xFF63A4EE))
                                    else
                                        listOf(Color.Transparent, Color.Transparent)
                                )
                            )
                            .fillMaxWidth()
                    ) {
                        // This row will be changing height depends on the keyboard status (open or not)
                        // and changing color depends if buttom sheet is up or not
                        Row(
                            Modifier
                                .height(if (isKeyboardOpen.value) ((statusBarHeight) / 2) - 3.dp else statusBarHeight + 1.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = if (sharedViewModel.fabClicked == true)
                                            listOf(Color(0xFF315A87), Color(0xFF436FA0))
                                        else if (sharedViewModel.fabClicked == false)
                                            listOf(Color(0xFF4884C9), Color(0xFF63A4EE))
                                        else // when it's null, which will be set in the start of the loginscreen
                                            listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface)
                                    )
                                )
                                .fillMaxWidth()
                        ) {}
                    }
                    val navController = rememberNavController()
                    // The actual app ui start here
                    RootNavGraph(navController = navController, sharedViewModel, statusBarHeight)
                }
            }

        }
    }
}

/***
 *  Detecting if the keyboard is open or closed
 */
@Composable
fun KeyboardStatus() {
    val view = LocalView.current
    val viewTreeObserver = view.viewTreeObserver
    DisposableEffect(viewTreeObserver) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            isKeyboardOpen.value = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true

        }

        viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
}