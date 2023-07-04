package com.example.schoolmanagementsystem.script.navbar

import android.annotation.SuppressLint
import android.view.ViewTreeObserver
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomNav(
    navController: NavHostController?,
    sharedViewModel: SharedViewModel?,
    statusBarHeight: Dp
) {
    val navController2 = rememberAnimatedNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController2, sharedViewModel!!) },
        floatingActionButton = {
            Box(
                modifier = Modifier
//                    .offset(y = if (sharedViewModel?.selectedcontract != null) 50.dp else 0.dp)
                    .width(60.0.dp)
                    .height(60.0.dp)
            ) {
                AnimatedVisibility(
//                    visible = (sharedViewModel!!.fabVisible && sharedViewModel.user?.role=="admin") ||
//                            (sharedViewModel!!.fabVisible && sharedViewModel.user?.role!="admin" && sharedViewModel.isOnInbox), enter = scaleIn(),
                    visible = sharedViewModel!!.fabVisible,
                    exit = scaleOut(),
                ) {
                    FloatingActionButton(
                        modifier = Modifier
                            .scale(0.9f)
                            .background(Color(0x00000000)),
                        contentColor = Color(0xFFF1F1F1),
                        containerColor = Color(0xFFF1F1F1),
                        onClick = sharedViewModel!!.fabOnClick,
                    ) {
                        Icon(
                            painterResource(id = R.drawable.z),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(3.dp)
                                .scale(1.5f)
                        )
                        if (sharedViewModel.isOnInbox)
                            Icon(
                                painterResource(id = R.drawable.e),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.scale(0.7f)
                            )
                        else
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.scale(1.3f)
                            )
                    }
                }
            }
        },
        content = {
            AnimatedGraph(navController = navController2, sharedViewModel = sharedViewModel!!)
        }, modifier = Modifier
            .statusBarsPadding()
//            .background(Color.Green)
            .padding(top = statusBarHeight + 0.dp)
    )
}


@Composable
fun BottomBar(navController: NavHostController, sharedViewModel: SharedViewModel) {
    //screens to appear in bottom bar
    val screens = listOf(
        Screen.Home,
        Screen.Users,
        Screen.Inbox,
        Screen.Settings
    )
    val screensForStudent = listOf(
        Screen.Home,
        Screen.Students,
        Screen.Inbox,
        Screen.Settings
    )
    //screens to show bottom bar in
    val navBarScreen = listOf(
        Screen.Home,
        Screen.Users,
        Screen.Inbox,
        Screen.Settings,
        Screen.Contract,
    )
    val navBarScreenForStudent = listOf(
        Screen.Home,
        Screen.Users,
        Screen.Inbox,
        Screen.Settings,
        Screen.Contract,
        Screen.Students
    )
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    val bottomBarDestination = if(sharedViewModel.user?.role == "student") navBarScreenForStudent.any { it.route == currentDestination?.route }
    else navBarScreen.any { it.route == currentDestination?.route }
    //making sure no bottom bar in payment screen...
    if (bottomBarDestination)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                    .background(MaterialTheme.colorScheme.inverseSurface)
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 6.dp)

            ) {
                if(sharedViewModel.user?.role == "student")
                screensForStudent.forEach { screen ->
                    AddItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navController = navController,
                        sharedViewModel
                    )
                }
                else
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


}


@Composable
fun AddItem(
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
        if (selected) MaterialTheme.colorScheme.surfaceTint
        else Color.Transparent

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
                .padding(start = 21.dp, end = 21.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!selected)
                Icon(
                    painterResource(id = screen.icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.surface
                )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = screen.title,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        if (screen.route == "Inbox" && sharedViewModel.unseenMsgExist)
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier
                    .scale(0.4f)
                    .align(Alignment.TopCenter)
                    .offset(y = -20.dp)
            )
    }
}

@Composable
@Preview
fun BottomNavPreview() {
//    BottomNav(null, null, statusBarHeight)
}