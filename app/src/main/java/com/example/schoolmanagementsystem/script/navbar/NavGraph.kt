package com.example.schoolmanagementsystem.script.navbar

import androidx.compose.runtime.Composable
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


@Composable
fun RootNavGraph(navController: NavHostController) {
    val sharedViewModel: SharedViewModel = viewModel()
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {
        authNavGraph(navController = navController, sharedViewModel = sharedViewModel)
        composable(route = Screen.NavBar.route) {
            BottomNav(navController = navController, sharedViewModel = sharedViewModel)
        }
    }
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(navCtr = navController, sharedViewModel = sharedViewModel)
        }
    }
}


@Composable
fun NavGraph2(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
//    val sharedViewModel: SharedViewModel = viewModel()
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route)
        {
            HomeScreen1(navCtr = navController, sharedViewModel = sharedViewModel)
//            println()
        }
        composable(route = Screen.Users.route)
        {
            UsersTab(navCtr = navController, sharedViewModel = sharedViewModel)
        }
        composable(route = Screen.Students.route)
        {
            ManageUser(navController, sharedViewModel)

        }
        composable(route = Screen.Profile.route)
        {
            HomeScreen4()
        }
        composable(route = Screen.ManageUser.route)
        {
            ManageUser(navController, sharedViewModel)
        }
        composable(route = Screen.Contract.route)
        {
            ContractUser(navController, sharedViewModel)
        }
    }
}


object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"

    //    const val DETAILS = "details_graph"
    const val EDITUSER = "edit_user"
}