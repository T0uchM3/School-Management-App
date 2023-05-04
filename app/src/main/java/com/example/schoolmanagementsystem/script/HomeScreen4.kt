package com.example.schoolmanagementsystem.script

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@SuppressLint("RememberReturnType")
@Composable
fun HomeScreen4(){
    var text = remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            repeat(100) {
                item {
                    Text("Test Test Test Test $it    ")
                }
            }
        }
    }
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(Color.White)
//        .padding(vertical = 50.dp, horizontal = 50.dp)){
//        Text(
//            text=" 444",
//            color = Color.Black,
//            fontSize = 30.sp,
////            modifier = Modifier
////                .align(Alignment.TopCenter)
////                .clickable {navController?.navigate(Destination.LoginScreen.route)},
//            textAlign = TextAlign.Center,
//            fontFamily = FontFamily.SansSerif,
//            fontStyle = FontStyle.Italic,
//            fontWeight = FontWeight.Bold,
//        )
//    }
}

@Preview
@Composable
fun Preview44(){
    HomeScreen4()
}