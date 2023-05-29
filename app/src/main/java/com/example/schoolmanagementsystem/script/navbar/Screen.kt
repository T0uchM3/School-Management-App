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

    // for 111
    object Home : Screen(
        route = "home",
        title = "Home",
//        icon = Icons.Outlined.Home,
        icon = R.drawable.a,
        icon_focused = null,
    )

    // for 2222
    object Users : Screen(
        route = "Users",
        title = "Users",
        icon = R.drawable.g,
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
        icon = R.drawable.e,
        icon_focused = null,
    )

    // for 3333
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
}
