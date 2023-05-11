package com.example.schoolmanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
//import com.example.schoolmanagmentsystem.script.navbar.NavGraph
import com.example.schoolmanagementsystem.script.navbar.RootNavGraph
import com.example.schoolmanagementsystem.ui.theme.AppTheme

sealed class Destination(val route: String) {
    object LoginScreen : Destination("loginScreen")
    object HomeScreen : Destination("homeScreen")
    object StaffScreen : Destination("staffScreen")
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme = remember { mutableStateOf(false) }

            AppTheme(darkTheme = isDarkTheme.value) {

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    RootNavGraph(navController = navController)
                }
            }

        }
    }
}