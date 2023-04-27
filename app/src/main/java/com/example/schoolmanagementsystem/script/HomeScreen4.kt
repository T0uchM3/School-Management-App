package com.example.schoolmanagementsystem.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen4(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(vertical = 50.dp, horizontal = 50.dp)){
        Text(
            text=" 444",
            color = Color.Black,
            fontSize = 30.sp,
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .clickable {navController?.navigate(Destination.LoginScreen.route)},
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun Preview44(){
    HomeScreen4()
}