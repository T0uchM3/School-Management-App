package com.example.schoolmanagementsystem.script.navbar

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterialApi::class,
)
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
                    .offset(y = if (sharedViewModel?.selectedcontract != null) 50.dp else 0.dp)
//                    .background(Color.T)
                    .width(60.0.dp)
                    .height(60.0.dp)

//                    .clip(shape = RoundedCornerShape(22.dp))
//                    .paint(painterResource(id = R.drawable.z))

            ) {
                AnimatedVisibility(
                    visible = sharedViewModel!!.fabVisible, enter = scaleIn(),
                    exit = scaleOut(),
                ) {


                    FloatingActionButton(
                        modifier = Modifier
//                            .clip(shape = RoundedCornerShape(22.dp))
                            .scale(0.9f)
                            .background(Color(0x00000000)),
                        contentColor = Color(0xFFF1F1F1),
                        containerColor = Color(0xFFF1F1F1),
                        onClick = sharedViewModel.fabOnClick,
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
                        Icon(
                            Icons.Default.Add,
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
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (sharedViewModel!!.fabClicked)
                        listOf(Color(0xFF315A87), Color(0xFF436FA0))
                    else
                        listOf(Color(0xFF4884C9), Color(0xFF63A4EE))
                )
            )
            .padding(top = statusBarHeight + 1.dp)
    )
//    }
}

//var state = false

@Composable
fun BottomBar(navController: NavHostController, sharedViewModel: SharedViewModel) {
    //screens to appear in bottom bar
    val screens = listOf(
        Screen.Home,
        Screen.Users,
        Screen.Students,
        Screen.Profile,
    )
    //screens to show bottom bar in
    val navBarScreen = listOf(
        Screen.Home,
        Screen.Users,
        Screen.Students,
        Screen.Profile,
        Screen.Contract
    )
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    val bottomBarDestination = navBarScreen.any { it.route == currentDestination?.route }
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
    }
//    }
}

@Composable
@Preview
fun BottomNavPreview() {
//    BottomNav(null, null, statusBarHeight)
}