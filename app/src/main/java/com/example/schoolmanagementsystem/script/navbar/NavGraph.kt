package com.example.schoolmanagementsystem.script.navbar

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.schoolmanagementsystem.script.ContractUser
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.ui.theme.HomeScreen1
import com.example.schoolmanagementsystem.ui.theme.UsersTab
import com.example.schoolmanagementsystem.script.HomeScreen4
import com.example.schoolmanagementsystem.script.ManageUser
import com.example.schoolmanagementsystem.script.LoginScreen
import com.example.schoolmanagementsystem.script.PaymentScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable as c2

//import com.google.accompanist.navigation.animation.composable
@Composable
fun RootNavGraph(navController: NavHostController) {
    val sharedViewModel: SharedViewModel = viewModel()
    NavHost(
        navController = navController, route = Graph.ROOT, startDestination = Graph.AUTHENTICATION
    ) {
        authNavGraph(navController = navController, sharedViewModel = sharedViewModel)
        composable(route = Screen.NavBar.route) {
            BottomNav(navController = navController, sharedViewModel = sharedViewModel)
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


//@Composable
//fun BarNavGraph(
//    navController: NavHostController, sharedViewModel: SharedViewModel
//) {
////    val sharedViewModel: SharedViewModel = viewModel()
//    NavHost(
//        navController = navController, route = Graph.HOME, startDestination = Screen.Home.route
//    ) {
//        composable(route = Screen.Home.route) {
//            HomeScreen1(navCtr = navController, sharedViewModel = sharedViewModel)
////            println()
//        }
//        composable(route = Screen.Users.route) {
//            UsersTab(navCtr = navController, sharedViewModel = sharedViewModel)
//        }
//        composable(route = Screen.Students.route) {
//            ManageUser(navController, sharedViewModel)
//
//        }
//        composable(route = Screen.Profile.route) {
//            HomeScreen4()
//        }
//        composable(route = Screen.ManageUser.route) {
//            ManageUser(navController, sharedViewModel)
//        }
//        composable(route = Screen.Contract.route) {
//            ContractUser(navController, sharedViewModel)
//        }
//    }
//}

@OptIn(ExperimentalAnimationApi::class)


val springSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioMediumBouncy)
val tweenSpec =
    tween<IntOffset>(durationMillis = 2000, easing = CubicBezierEasing(0.08f, 0.93f, 0.68f, 1.27f))

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun AnimatedGraph(
    navController: NavHostController, sharedViewModel: SharedViewModel
) {
//    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = Screen.Home.route) {
        c2(Screen.Home.route, enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300)
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
        }) { HomeScreen1(navCtr = navController, sharedViewModel = sharedViewModel) }
        c2(Screen.Users.route, enterTransition = {
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
        }) { UsersTab(navCtr = navController, sharedViewModel = sharedViewModel) }
        c2(Screen.ManageUser.route, enterTransition = {
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
        }) { ManageUser(navController, sharedViewModel) }
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
        composable(route = Screen.Students.route) {
            HomeScreen4(navController, sharedViewModel)

        }
        composable(route = Screen.Profile.route) {
            HomeScreen4(navController, sharedViewModel)
        }
//        composable(route = Screen.ManageUser.route) {
//            ManageUser(navController, sharedViewModel)
//        }
//        composable(route = Screen.Contract.route) {
//            ContractUser(navController, sharedViewModel)
//        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"

    //    const val DETAILS = "details_graph"
    const val EDITUSER = "edit_user"
}