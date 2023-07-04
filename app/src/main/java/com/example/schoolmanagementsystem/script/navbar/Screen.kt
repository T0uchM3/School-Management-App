package com.example.schoolmanagementsystem.script.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.schoolmanagementsystem.R


sealed class Screen(
    val route: String,
    val title: String,
    val icon: Int,
    val icon_focused: ImageVector?
) {

    object Home : Screen(
        route = "home",
        title = "Home",
        icon = R.drawable.a,
        icon_focused = null,
    )

    object Users : Screen(
        route = "Users",
        title = "Users",
//        icon = R.drawable.d,
        icon = R.drawable.g,
        icon_focused = null,
    )
    object Inbox : Screen(
        route = "Inbox",
        title = "Inbox",
        icon = R.drawable.e,
        icon_focused = null,
    )
    object Messages : Screen(
        route = "Messages",
        title = "Messages",
        icon = R.drawable.e,
        icon_focused = null,
    )
    object Students : Screen(
        route = "Students",
        title = "Students",
        icon = R.drawable.d,
        icon_focused = null,
    )

    object Profile : Screen(
        route = "Profile",
        title = "Profile",
        icon = R.drawable.d,
        icon_focused = null,
    )

    object Login : Screen(
        route = "login",
        title = "login",
        icon = 0,
        icon_focused = null,
    )

    object NavBar : Screen(
        route = "navBar",
        title = "navBar",
        icon =0,
        icon_focused = null,
    )

    object ManageUser : Screen(
        route = "ManageUser",
        title = "",
        icon = 0,
        icon_focused = null,
    )

    object Contract : Screen(
        route = "Contract",
        title = "",
        icon = 0,
        icon_focused = null,
    )

    object Payment : Screen(
        route = "Payment",
        title = "",
        icon = 0,
        icon_focused = null,
    )
    object Settings : Screen(
        route = "Settings",
        title = "Settings",
        icon = R.drawable.u,
        icon_focused = null,
    )
    object Groups : Screen(
        route = "Groups",
        title = "Groups",
        icon = R.drawable.u,
        icon_focused = null,
    )
}
