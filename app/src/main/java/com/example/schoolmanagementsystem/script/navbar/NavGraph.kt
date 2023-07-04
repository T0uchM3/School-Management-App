package com.example.schoolmanagementsystem.script.navbar

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.schoolmanagementsystem.script.ContractUser
import com.example.schoolmanagementsystem.script.homeTab.GroupScreen
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.script.homeTab.HomeScreen1
import com.example.schoolmanagementsystem.ui.theme.UsersTab
import com.example.schoolmanagementsystem.script.ManageUser
import com.example.schoolmanagementsystem.script.homeTab.LoginScreen
import com.example.schoolmanagementsystem.script.MessageScreen
import com.example.schoolmanagementsystem.script.PaymentScreen
import com.example.schoolmanagementsystem.script.homeTab.ProfileScreen
import com.example.schoolmanagementsystem.script.homeTab.SettingsScreen
import com.example.schoolmanagementsystem.ui.theme.InboxScreen
import com.example.schoolmanagementsystem.script.homeTab.StudentScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable as c2

//import com.google.accompanist.navigation.animation.composable
@Composable
fun RootNavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    statusBarHeight: Dp
) {
    NavHost(
        navController = navController, route = Graph.ROOT, startDestination = Graph.AUTHENTICATION
    ) {
        authNavGraph(navController = navController, sharedViewModel = sharedViewModel)
        composable(route = Screen.NavBar.route) {
            BottomNav(navController = navController, sharedViewModel = sharedViewModel,statusBarHeight)
        }
    }
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController, sharedViewModel: SharedViewModel
) {
    navigation(
        route = Graph.AUTHENTICATION, startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(navCtr = navController, sharedViewModel = sharedViewModel)
        }

    }
}


val springSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioMediumBouncy)
val tweenSpec =
    tween<IntOffset>(durationMillis = 2000, easing = CubicBezierEasing(0.08f, 0.93f, 0.68f, 1.27f))

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
fun AnimatedGraph(
    navController: NavHostController, sharedViewModel: SharedViewModel
) {
//    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = Screen.Home.route) {
        c2(Screen.Home.route)
        { HomeScreen1(navCtr = navController, sharedViewModel = sharedViewModel) }
        c2(Screen.Users.route) { UsersTab(navCtr = navController, sharedViewModel = sharedViewModel) }
        c2(Screen.ManageUser.route) { ManageUser(navController, sharedViewModel) }
        c2(Screen.Contract.route, enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left, animationSpec = tween(300)
            )
        }, exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left, animationSpec = tween(300)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300)
            )
        }) { ContractUser(navController, sharedViewModel) }
//        composable(route = Screen.Users.route)
//        {
//            UsersTab(navCtr = navController, sharedViewModel = sharedViewModel)
//        }
        c2(Screen.Payment.route, enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left, animationSpec = tween(300)
            )
        }, exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left, animationSpec = tween(300)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300)
            )
        }) { PaymentScreen(navController, sharedViewModel) }
//        composable(route = Screen.Students.route) {
//            HomeScreen4(navController, sharedViewModel)
//        }

        //******************* login *********************
        c2(Screen.Login.route) {
            LoginScreen(navController, sharedViewModel)
        }

        //********************** inbox **************************
        c2(Screen.Inbox.route) {
            InboxScreen(navController, sharedViewModel)
        }
        //**************************** message ********************************
        c2(Screen.Messages.route) {
            MessageScreen(navController, sharedViewModel)
        }
        //**************************** settings ********************************
        c2(Screen.Settings.route, enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left, animationSpec = tween(300)
            )
        }, exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300)
            )
        }) {
            SettingsScreen(navController, sharedViewModel)
        }
        //******************* profile *********************
        c2(Screen.Profile.route, enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left, animationSpec = tween(300)
            )
        }, exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left, animationSpec = tween(300)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300)
            )
        }) {
            ProfileScreen(navController, sharedViewModel)
        }
        //******************* students *********************
        c2(Screen.Students.route) {
            StudentScreen(navController, sharedViewModel)
        }
        //******************* groups *********************
        c2(Screen.Groups.route) {
            GroupScreen(navController, sharedViewModel)
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
    const val EDITUSER = "edit_user"
}