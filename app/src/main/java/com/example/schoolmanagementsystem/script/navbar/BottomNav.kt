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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.ui.theme.Purple500
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterialApi::class,
)
@Composable
fun BottomNav(navController: NavHostController?, sharedViewModel: SharedViewModel?) {

    val navController2 = rememberAnimatedNavController()


    Scaffold(
        bottomBar = { BottomBar(navController = navController2, sharedViewModel!!) },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .width(56.0.dp)
                    .height(56.0.dp)
            ) {
                AnimatedVisibility(
                    visible = sharedViewModel!!.fabVisible, enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    FloatingActionButton(
                        onClick = sharedViewModel.fabOnClick,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            }
        },
        content = {

            AnimatedGraph(navController = navController2, sharedViewModel = sharedViewModel!!)
        }


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
    //screens to show bottom bar in, making sure no bottom bar in payment screen
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
    if (bottomBarDestination)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                .background(MaterialTheme.colorScheme.inverseSurface)
                .fillMaxWidth()
                .padding(vertical = 6.dp)

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
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            //hack to only show tab's name when focused
            val emptyIcon = ImageVector.Builder(
                defaultHeight = 0.dp,
                defaultWidth = 0.dp,
                viewportHeight = 0f,
                viewportWidth = 0f
            ).build()

            Icon(
                imageVector = if (selected) emptyIcon else screen.icon!!,
                contentDescription = ""
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = screen.title,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
//    }
}

@Composable
@Preview
fun BottomNavPreview() {
    BottomNav(null, null)
}