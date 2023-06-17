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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.schoolmanagementsystem.script.getMessages
//import com.example.schoolmanagmentsystem.script.navbar.NavGraph
import com.example.schoolmanagementsystem.script.navbar.RootNavGraph
import com.example.schoolmanagementsystem.ui.theme.AppTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.insets.toPaddingValues
import kotlinx.coroutines.delay
import okhttp3.internal.wait

sealed class Destination(val route: String) {
    object LoginScreen : Destination("loginScreen")
    object HomeScreen : Destination("homeScreen")
    object StaffScreen : Destination("staffScreen")
}

val isKeyboardOpen: MutableState<Boolean> = mutableStateOf(false)

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // This is one of the pillars holding the bottom bar up and preventing it
            // from going under system navigation bar
            WindowInsets.systemBars
            val isDarkTheme = remember { mutableStateOf(false) }
            val sharedViewModel: SharedViewModel = viewModel()
            sharedViewModel.setWindow2(window)


            ProvideWindowInsets {
                val insets = LocalWindowInsets.current
                var statusBarHeight = with(LocalDensity.current) {
                    insets.statusBars.top.toDp()
                }
                var statusBarHeight2 = insets.statusBars.top
                // Doing it like this to fix a problem with status bar color problem
                // when search bars are focused
                val mod = if (!sharedViewModel.searchBarFocused)
                    Modifier
                        .navigationBarsWithImePadding()
                else
                    Modifier


                AppTheme(darkTheme = isDarkTheme.value, sharedViewModel) {

                    Surface(
                        modifier = mod
                            .fillMaxSize()
//                            .background(MaterialTheme.colorScheme.surface)
                            // Prevent the ui to be drawn under the system bottom bar, so we rised it
                    ) {
                        // Some duct tape stuff going on here, to make sure the status bar facade is holding ;)
                        KeyboardStatus()
                        // Used 2 nested rows here cause having 1 row seems like it fill the whole surface
                        Row(
                            Modifier
                                .height(4.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = if (sharedViewModel.fabClicked != null)
                                            listOf(Color(0xFF3F7CC4), Color(0xFF7AB8FF))
                                        else
                                            listOf(Color.Transparent, Color.Transparent)
                                    )
                                )
                                .fillMaxWidth()
                        ) {
//                            println("status bar height "+statusBarHeight2)
//                            // This row will be changing height depends on the keyboard status (open or not)
//                            // and changing color depends if buttom sheet is up or not
//                            Row(
//                                Modifier
////                                    .height(if (isKeyboardOpen.value) ((statusBarHeight) / 2) - 3.dp else statusBarHeight + 1.dp)
//                                    .height(if (sharedViewModel.usersFocus == true && isKeyboardOpen.value) ((statusBarHeight) / 2) else statusBarHeight + 1.dp)
//                                    .background(
//                                        brush = Brush.horizontalGradient(
//                                            colors = if (sharedViewModel.fabClicked == true)
//                                                listOf(Color(0xFF2B5484), Color(0xFF537DAC))
//                                            else if (sharedViewModel.fabClicked == false)
//                                                listOf(Color(0xFF3F7CC4), Color(0xFF7AB8FF))
//                                            else // when it's null, which will be set in the start of the loginscreen
//                                                listOf(
//                                                    MaterialTheme.colorScheme.surface,
//                                                    MaterialTheme.colorScheme.surface
//                                                )
//                                        )
//                                    )
//                                    .fillMaxWidth()
//                            ) {}
                        }
                        val navController = rememberNavController()
                        // The actual app ui start here
                        RootNavGraph(
                            navController = navController,
                            sharedViewModel,
                            statusBarHeight
                        )
                        // Timer to retrieve messages every 5 seconds
                        LaunchedEffect(key1 = sharedViewModel.waiting) {
                            while (sharedViewModel.waiting) {
                                delay(5000)
                                getMessages(sharedViewModel.user!!.id.toInt(), sharedViewModel)
                                println("WAITED 5 SECS")
                            }
                        }

                    }
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