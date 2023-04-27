package com.example.schoolmanagementsystem.ui.theme

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.script.usersAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Pages
import androidx.compose.ui.draw.scale

//fun Float.dp(): Float = this * density + 0.5f

//val density: Float
//    get() = Resources.getSystem().displayMetrics.density
var userName = ""
var userId = ""
var userRole = ""

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun HomeScreen2(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    LaunchedEffect(key1 = Unit) {
        usersAPI(navCtr = navCtr, sharedViewModel = sharedViewModel)
    }
    val users = remember { sharedViewModel.userList }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
//            .padding(vertical = 50.dp, horizontal = 50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
//            loginAPI(navCtr, sharedViewModel)
                },
                modifier = Modifier
//                    .align(alignment = Alignment.End)
                    .scale(1.2f),
                border = BorderStroke(1.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray)

            ) {
                Text(text = "search")
            }
            Button(
                onClick = {
//            loginAPI(navCtr, sharedViewModel)
                },
                modifier = Modifier
//            .align(Alignment.BottomCenter)
                    .scale(1.2f),
                border = BorderStroke(1.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray)

            ) {
                Text(text = "+")
            }
        }
        println("fffffffffffffffffffffffffffffffffffffffff")
        if (sharedViewModel.userList != null) {
            println("ssssssssssssssssssssssssssssssssssssssss")
            LazyColumn {
                items(items = sharedViewModel.userList!!) { user ->
//                    Text(user.name.toString(), modifier = Modifier.padding(15.dp))
                    userName = user.name.toString()
                    userId = user.id.toString()
                    userRole = user.role.toString()
                    SwipeableBoxPreview(
                        Modifier.padding()
                    )
                    Divider()
                }
            }
        }
//        SwipeableBoxPreview(
//            Modifier.padding()
//        )
    }


//    val model: CardsScreenViewModel = viewModel()
//    CardsScreen(model)

}

@Composable
private fun SwipeableBoxPreview(modifier: Modifier = Modifier) {
    var isSnoozed by rememberSaveable { mutableStateOf(false) }
    var isArchived by rememberSaveable { mutableStateOf(false) }

    val editUser = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Edit),
        background = Color.Perfume,
        onSwipe = { println("Reply swiped") },
        isUndo = false,
    )
    val editContract = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Pages),
        background = Color.Fern,
        onSwipe = { isArchived = !isArchived },
        isUndo = isArchived,
    )
    val remove = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Delete),
        background = Color.SeaBuckthorn,
        onSwipe = { isSnoozed = !isSnoozed },
        isUndo = isSnoozed,
    )

    SwipeableActionsBox(
        modifier = modifier,
        startActions = listOf(editUser, editContract),
        endActions = listOf(remove),
        swipeThreshold = 40.dp,
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp)
    ) {
        BatmanIpsumItem(
            isSnoozed = isSnoozed
        )
    }
}

@Composable
private fun BatmanIpsumItem(
    modifier: Modifier = Modifier,
    isSnoozed: Boolean
) {
    Row(
        modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
            .padding(vertical = 16.dp, horizontal = 20.dp)
            .animateContentSize()

    ) {
        Box(
            Modifier
                .padding(top = 2.dp)
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )

        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "ID: $userId | Role: $userRole",
                style = MaterialTheme.typography.bodyMedium
            )

            if (isSnoozed) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .background(Color.SeaBuckthorn.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    text = "Snoozed until tomorrow",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

val Color.Companion.SeaBuckthorn get() = Color(0xFFF9A825)
val Color.Companion.Fern get() = Color(0xFF66BB6A)
val Color.Companion.Perfume get() = Color(0xFFD0BCFF)


@Preview
@Composable
fun Preview22() {
    HomeScreen2(TODO(), TODO())
}