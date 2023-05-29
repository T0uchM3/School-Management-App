package com.example.schoolmanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.schoolmanagementsystem.script.SharedViewModel
//import com.example.schoolmanagmentsystem.script.navbar.NavGraph
import com.example.schoolmanagementsystem.script.navbar.RootNavGraph
import com.example.schoolmanagementsystem.ui.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

sealed class Destination(val route: String) {
    object LoginScreen : Destination("loginScreen")
    object HomeScreen : Destination("homeScreen")
    object StaffScreen : Destination("staffScreen")
}


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val sysbar = WindowInsets.systemBars
            val bottomBarHeight = with(LocalDensity.current) {
                sysbar.getBottom(this).toDp()
            }
            val isDarkTheme = remember { mutableStateOf(false) }
            val sharedViewModel: SharedViewModel = viewModel()
            sharedViewModel.setWindow2(window)
            AppTheme(darkTheme = isDarkTheme.value, sharedViewModel) {

                Surface(
                    modifier = Modifier.fillMaxSize().padding(bottom = bottomBarHeight)
                ) {
                    val navController = rememberNavController()
                    RootNavGraph(navController = navController, sharedViewModel)
                }
            }

        }
    }
}