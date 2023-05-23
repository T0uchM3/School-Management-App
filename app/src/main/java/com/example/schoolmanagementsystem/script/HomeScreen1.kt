package com.example.schoolmanagementsystem.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.script.navbar.Screen
import java.lang.Math.abs


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen1(navCtr: NavHostController?, sharedViewModel: SharedViewModel?) {
    val name = navCtr?.previousBackStackEntry?.arguments?.getString("name")
    val user = sharedViewModel?.user
    //resetting tab focus
    sharedViewModel?.defineUsersFocus(false)


    LaunchedEffect(key1 = user) {
        sharedViewModel?.defineFabVisible(false)
        println(user?.name + "+++++++++++++++++++++")
    }
    println(user?.name + "----------------------")
    val id = navCtr?.previousBackStackEntry?.arguments?.getString("id")
    val role = navCtr?.previousBackStackEntry?.arguments?.getString("role")
    val swipeState = rememberSwipeableState(0)
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val minSwipeOffset by remember { mutableStateOf(300f) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        offsetX += dragAmount.x

                    },
                    onDragEnd = {

                        when {
                            (offsetX < 0F && abs(offsetX) > minSwipeOffset) -> {
                                println(" SwipeDirection.Left")
                                navCtr?.navigate(Screen.Users.route)
                            }

                            (offsetX > 0 && abs(offsetX) > minSwipeOffset) -> {
                                println(" SwipeDirection.Right")
                            }

                            else -> null

                        }
                        offsetX = 0F
                    }
                )
            }

//            .padding(vertical = 10.dp, horizontal = 50.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
//                .background(Color.LightGray)
                .fillMaxWidth()
                .fillMaxHeight(0.1f)

        ) {
            Column(
                modifier = Modifier
//                    .background(Color.White)
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = user?.name.toString(),
                    color = Color.DarkGray,
                    fontSize = 30.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = InterMedium,
                )
                Row(
                    modifier = Modifier
//                        .background(Color.)
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                ) {
                    Text(
                        text = "ID: 111111 | Staff",
                        color = Color.DarkGray,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = InterMedium,
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.10f)
        ) {
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

        }

    }

}


@Preview
@Composable
fun Preview1() {
    HomeScreen1(null, null)
}