package com.example.schoolmanagementsystem.script.cards

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.schoolmanagementsystem.R

@Composable
fun ActionsRow(
    actionIconSize: Dp,
    onDelete: () -> Unit,
//    onEdit: () -> Unit,
    onFavorite: () -> Unit,
) {
    Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        IconButton(
            modifier = Modifier.size(actionIconSize),
            onClick = onDelete,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    tint = Color.Gray,
                    contentDescription = "delete action",
                )
            }
        )
//        IconButton(
//            modifier = Modifier.size(actionIconSize),
//            onClick = onEdit,
//            content = {
//                Icon(
//                    painter = painterResource(id = R.drawable.wrench),
//                    tint = Color.Gray,
//                    contentDescription = "edit action",
//                )
//            },
//        )
        IconButton(
            modifier = Modifier.size(actionIconSize),
            onClick = onFavorite,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    tint = Color.Red,
                    contentDescription = "Expandable Arrow",
                )
            }
        )
    }
}