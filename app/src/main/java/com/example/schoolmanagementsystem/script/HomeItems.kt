package com.example.schoolmanagementsystem.script

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.schoolmanagementsystem.R


sealed class HomeItems(
    val title: String,
    val icon: Int,
    val color : Color
) {

    object attendance : HomeItems(
        title = "Attendance",
        icon = R.drawable.s,
        color =  Color(0xFF02C40B)
    )
    object subscription : HomeItems(
        title = "Subscriptions",
        icon = R.drawable.t,
        color =  Color(0xFF048FDB)
    )
    object note : HomeItems(
        title = "Notes",
        icon = R.drawable.r,
        color =  Color(0xFF8D0CFF)
    )
    object setting : HomeItems(
        title = "Settings",
        icon = R.drawable.u,
        color =  Color(0xFFFF8400)
    )
    object message : HomeItems(
        title = "Messages",
        icon = R.drawable.w,
        color =  Color(0xFF02C40B)
    )
    object group : HomeItems(
        title = "Groups",
        icon = R.drawable.g,
        color =  Color(0xFFD4E157)
    )
    object student : HomeItems(
        title = "Students",
        icon = R.drawable.d,
        color =  Color(0xFF048FDB)
    )
    object contract : HomeItems(
        title = "Contract",
        icon = R.drawable.n,
        color =  Color(0xFFFF7043)
    )
}
