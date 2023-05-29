package com.example.schoolmanagementsystem.script

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.schoolmanagementsystem.R


sealed class HomeItems(
    val route: String,
    val title: String,
    val icon: Int,
    val color : Color
) {

    object attendance : HomeItems(
        route = "home",
        title = "Attendance",
        icon = R.drawable.s,
        color =  Color(0xFF02C40B)
    )
    object subscription : HomeItems(
        route = "home",
        title = "Subscriptions",
        icon = R.drawable.t,
        color =  Color(0xFF048FDB)
    )
    object note : HomeItems(
        route = "home",
        title = "Notes",
        icon = R.drawable.r,
        color =  Color(0xFF8D0CFF)
    )
    object setting : HomeItems(
        route = "home",
        title = "Settings",
        icon = R.drawable.u,
        color =  Color(0xFFFF8400)
    )
    object message : HomeItems(
        route = "home",
        title = "Messages",
        icon = R.drawable.w,
        color =  Color(0xFF02C40B)
    )
}
