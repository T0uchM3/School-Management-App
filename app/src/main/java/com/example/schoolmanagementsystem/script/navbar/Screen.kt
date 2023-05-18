package com.example.schoolmanagementsystem.script.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Group
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource


sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector?,
    val icon_focused: ImageVector?
) {

    // for 111
    object Home : Screen(
        route = "home",
        title = "Home",
        icon = Icons.TwoTone.Home,
        icon_focused = null,
    )

    // for 2222
    object Users : Screen(
        route = "Users",
        title = "Users",
        icon = Icons.TwoTone.Group,
        icon_focused = null,
    )

    object Students : Screen(
        route = "Students",
        title = "Students",
        icon = Icons.TwoTone.Person,
        icon_focused = null,
    )

    // for 3333
    object Profile : Screen(
        route = "Profile",
        title = "Profile",
        icon = Icons.TwoTone.Person,
        icon_focused = null,
    )

    object Login : Screen(
        route = "login",
        title = "login",
        icon = null,
        icon_focused = null,
    )

    object NavBar : Screen(
        route = "navBar",
        title = "navBar",
        icon = null,
        icon_focused = null,
    )

    object ManageUser : Screen(
        route = "ManageUser",
        title = "",
        icon = null,
        icon_focused = null,
    )

    object Contract : Screen(
        route = "Contract",
        title = "",
        icon = null,
        icon_focused = null,
    )

    object Payment : Screen(
        route = "Payment",
        title = "",
        icon = null,
        icon_focused = null,
    )
}
