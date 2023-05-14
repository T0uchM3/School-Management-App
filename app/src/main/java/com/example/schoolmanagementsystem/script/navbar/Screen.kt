package com.example.schoolmanagementsystem.script.navbar

import com.example.schoolmanagementsystem.R


sealed class Screen(
    val route: String,
    val title: String,
    val icon: Int,
    val icon_focused: Int
) {

    // for 111
    object Home : Screen(
        route = "home",
        title = "Home",
        icon = R.drawable.plus,
        icon_focused = R.drawable.wrench
    )

    // for 2222
    object Users : Screen(
        route = "Users",
        title = "Users",
        icon = R.drawable.plus,
        icon_focused = R.drawable.wrench
    )

    object Students : Screen(
        route = "Students",
        title = "Students",
        icon = R.drawable.plus,
        icon_focused = R.drawable.wrench
    )

    // for 3333
    object Profile : Screen(
        route = "Profile",
        title = "Profile",
        icon = R.drawable.plus,
        icon_focused = R.drawable.wrench
    )

    object Login : Screen(
        route = "login",
        title = "login",
        icon = 0,
        icon_focused = 0
    )

    object NavBar : Screen(
        route = "navBar",
        title = "navBar",
        icon = 0,
        icon_focused = 0
    )

    object ManageUser : Screen(
        route = "ManageUser",
        title = "",
        icon = 0,
        icon_focused = 0
    )
    object Contract : Screen(
        route = "Contract",
        title = "",
        icon = 0,
        icon_focused = 0
    )
    object Payment : Screen(
        route = "Payment",
        title = "",
        icon = 0,
        icon_focused = 0
    )
}
