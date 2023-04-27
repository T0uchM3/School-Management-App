package com.example.schoolmanagementsystem.script

import com.example.schoolmanagementsystem.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.commandiron.bubble_navigation_bar_compose.BubbleNavigationBar
import com.commandiron.bubble_navigation_bar_compose.BubbleNavigationBarItem
import com.example.schoolmanagementsystem.ui.theme.InterMedium

@Composable
fun StaffScreen(navController: NavHostController?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
//            .padding(vertical = 10.dp, horizontal = 50.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .background(Color.LightGray)
                .fillMaxWidth()
                .fillMaxHeight(0.1f)

        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "Elanos NS",
                    color = Color.DarkGray,
                    fontSize = 30.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .clickable { navController?.navigate(Destination.LoginScreen.route) },
//                    textAlign = TextAlign.Center,
                    fontFamily = InterMedium,
//                    fontWeight = FontWeight.Bold,
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
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .clickable { navController?.navigate(Destination.LoginScreen.route) },
//                    textAlign = TextAlign.Center,
                        fontFamily = InterMedium,
//                    fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxWidth()
                .fillMaxHeight(0.10f)
        ) {
//            val navigationItems = listOf(
//                BubbleNavigationBarItem(
//                    selected = true,
//                    onClick = { println("ffffffffffffffff") },
//                    iconPainter = painterResource(id = R.drawable.plus),
////                    icon = ImageVector.vectorResource(id = com.example.schoolmanagmentsystem.R.drawable.gotolocation),
////                    icon = painterResource(com.example.schoolmanagmentsystem.R.drawable.gotolocation),
//                    selectedColor = Color.Transparent,
//                    title = "ooooooo"
//                ),
//                BubbleNavigationBarItem(
//                    selected = false,
//                    onClick = { /*TODO*/ },
//                    iconPainter = painterResource(id = R.drawable.plus),
////                    icon = ImageVector.vectorResource(id = com.example.schoolmanagmentsystem.R.drawable.gotolocation),
////                    icon = painterResource(com.example.schoolmanagmentsystem.R.drawable.gotolocation),
//                    selectedColor = Color.Transparent,
//                    title = "xxxxxxx"
//                )
//            )
            BubbleNavigationBar(
                modifier = Modifier
                    .background(Color.White)
            ) {
                val selected = remember { mutableStateOf(false) }
                BubbleNavigationBarItem(
                    selected = true,
                    onClick = {
                        //Navigate
                    },
                    iconPainter = painterResource(id = R.drawable.ait),
                    title = "555555",
                    selectedColor = Color.White
                );
                BubbleNavigationBarItem(
                    selected = selected.value,
                    onClick = {
                        selected.value = !selected.value
                    },
                    iconPainter = painterResource(id = R.drawable.plus),
                    title = "2222",
                    selectedColor = Color.Transparent
                );
                BubbleNavigationBarItem(
                    selected = false,
                    onClick = {
                        //Navigate
                    },
                    iconPainter = painterResource(id = R.drawable.wrench),
                    title = "33333",
                    selectedColor = Color.White
                )

            }
        }
        Row(
            modifier = Modifier
//                .padding(vertical = 1.dp, horizontal = 50.dp)
                .background(Color.Gray)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

        }

    }
}


@Preview
@Composable
fun Preview3() {
    StaffScreen(null)
}