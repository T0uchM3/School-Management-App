package com.example.schoolmanagementsystem.script

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController


@SuppressLint("RememberReturnType")
@Composable
fun ProfileScreen(navController: NavHostController, sharedViewModel: SharedViewModel) {
    var text = remember { mutableStateOf("Hello, Mom!") }
    sharedViewModel.defineUsersFocus(false)

    MaterialTheme {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            repeat(100) {
//                item {
//                    Text("Test Test Test Test $it    ")
//                }
//            }
        }
    }

}

@Preview
@Composable
fun Preview44() {
//    HomeScreen4(TODO(), TODO())
}