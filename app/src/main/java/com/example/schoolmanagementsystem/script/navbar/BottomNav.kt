package com.example.schoolmanagementsystem.script.navbar

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.ui.theme.Purple500
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNav(navController: NavHostController?, sharedViewModel: SharedViewModel?) {
    val navController2 = rememberAnimatedNavController()
    if (sharedViewModel != null) {
        LaunchedEffect(key1 = Unit) {
        }
        Scaffold(
            bottomBar = { BottomBar(navController = navController2, sharedViewModel) }
        ) {
            AnimatedGraph(navController = navController2, sharedViewModel = sharedViewModel)
        }
    }
}

//var state = false

@Composable
fun BottomBar(navController: NavHostController, sharedViewModel: SharedViewModel) {
    val screens = listOf(
        Screen.Home,
        Screen.Users,
        Screen.Students,
        Screen.Profile,
    )
//    state = sharedViewModel.state
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
//            .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            .clip(shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
            .background(Color.Red)
            .fillMaxWidth()

    ) {

        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController,
                sharedViewModel
            )
        }
    }


}

val selected2 = false

@Composable
fun RowScope.AddItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
//    val selected = remember { mutableStateOf<Boolean>(false) }.value
    var selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    println("ROUTE= " + screen.route)
    if (sharedViewModel.usersFocus && screen.route == "Users") {
        selected = true
    }
    val background =
        if (selected) Purple500.copy(alpha = 0.6f) else Color.Transparent


    val contentColor =
        if (selected) Color.White else Color.Black

    val screens = listOf(
        Screen.Home,
        Screen.Users,
        Screen.Students,
        Screen.Profile,
        Screen.Contract
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination2 = navBackStackEntry?.destination
    val bottomBarDestination = screens.any { it.route == currentDestination2?.route }
    if (bottomBarDestination){

        Box(
            modifier = Modifier
                .height(40.dp)
                .clip(CircleShape)
                .background(background)
                .clickable(onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                })
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = if (selected) screen.icon_focused else screen.icon),
                    contentDescription = "icon",
                    tint = contentColor
                )
                AnimatedVisibility(visible = selected) {
                    Text(
                        text = screen.title,
                        color = contentColor
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun BottomNavPreview() {
    BottomNav(null, null)
}