package com.example.schoolmanagementsystem.script

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@SuppressLint("RememberReturnType")
@Composable
fun ProfileScreen(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    var text = remember { mutableStateOf("Hello, Mom!") }
    sharedViewModel.defineUsersFocus(false)
    sharedViewModel.defineFabVisible(false)

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color(0xFFFCFCF5),
        darkIcons = true
    )
    val minSwipeOffset by remember { mutableStateOf(300f) }
    var offsetX by remember { mutableStateOf(0f) }
    MaterialTheme {
        LazyColumn(modifier = Modifier.fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        offsetX += dragAmount.x

                    },
                    onDragEnd = {

                        when {
                            (offsetX < 0F && Math.abs(offsetX) > minSwipeOffset) -> {
                                println(" SwipeDirection.Left")
//                                navCtr.navigate(Screen.Profile.route)
                            }

                            (offsetX > 0 && Math.abs(offsetX) > minSwipeOffset) -> {
                                println(" SwipeDirection.Right")
                                navCtr.navigate(Screen.Inbox.route)
                            }

                            else -> null

                        }
                        offsetX = 0F
                    }
                )
            }) {
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