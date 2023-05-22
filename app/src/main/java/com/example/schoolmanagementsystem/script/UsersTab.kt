package com.example.schoolmanagementsystem.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Woman
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Pages
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.ManageUser
import com.example.schoolmanagementsystem.script.Payment
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.script.User
import com.example.schoolmanagementsystem.script.deleteUserAPI
import com.example.schoolmanagementsystem.script.getUserContract
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.example.schoolmanagementsystem.script.usersAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

var scope: CoroutineScope? = null

@OptIn(ExperimentalMaterial3Api::class)
var sheetState: SheetState? = null

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
)
@Composable
fun UsersTab(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    val minSwipeOffset by remember { mutableStateOf(300f) }
    var offsetX by remember { mutableStateOf(0f) }
    val localUserList = remember { SnapshotStateList<User>() }

    sheetState =
        remember {
            SheetState(
                skipHiddenState = false,
                skipPartiallyExpanded = false,
                initialValue = SheetValue.Hidden
            )
        }
    scope = rememberCoroutineScope()
    if (sheetState?.isVisible == true) {
        ModalBottomSheet(
            sheetState = sheetState!!,
            dragHandle = null,
            shape = RoundedCornerShape(
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
                topStart = 12.dp,
                topEnd = 12.dp
            ),
            onDismissRequest = {
                scope?.launch {
                    sheetState?.hide()
                }
            },
            content = {
                ManageUser(sharedViewModel = sharedViewModel, scope = scope, state = sheetState)
            }
        )
    }


    LaunchedEffect(key1 = Unit) {
        sharedViewModel.defineFabVisible(true)

        //setting up fab in user tab
        sharedViewModel.defineFabClick {
            sharedViewModel.defineIsNewUser(true)
            scope?.launch {
                sheetState?.show()
            }
        }
        sharedViewModel.userList.clear()
        sharedViewModel.contractList.clear()
        sharedViewModel.paymentList.clear()
        usersAPI(sharedViewModel = sharedViewModel)
    }
    localUserList.clear()
    sharedViewModel.userList.forEach { user -> localUserList.add(user) }

//    return
    val users = remember { sharedViewModel.userList }
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color(0xfff2f6f5))
            .background(Color(0xFFF3FAED))
//            .padding(vertical = 50.dp, horizontal = 50.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        offsetX += dragAmount.x

                    },
                    onDragEnd = {

                        when {
                            (offsetX < 0F && Math.abs(offsetX) > minSwipeOffset) -> {
                                println(" SwipeDirection.Left")

                            }

                            (offsetX > 0 && Math.abs(offsetX) > minSwipeOffset) -> {
                                println(" SwipeDirection.Right")
                                navCtr.navigate(Screen.Home.route)
                            }

                            else -> null

                        }
                        offsetX = 0F
                    }
                )
            }
    ) {
        BoxWithConstraints {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .padding(vertical = 13.dp),
//                horizontalArrangement = Arrangement.Center
                verticalAlignment = Alignment.CenterVertically
            ) {
                var visible by remember { mutableStateOf(false) }
                val focusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current
                var isInitialFocus by remember { mutableStateOf(true) }

                var text by remember { mutableStateOf("") }

                BasicTextField(
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { /* Do something */ }),
                    value = text,
                    onValueChange = { text = it },
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(

                                    painter = rememberAsyncImagePainter(R.drawable.search),
                                    contentDescription = null,
                                    modifier = Modifier.padding(start = 3.dp, end = 6.dp)

                                )
                                innerTextField()

                            }

                            if (text.isEmpty()) {
                                Text(
                                    text = "Search users",
                                    color = Color.Gray,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 35.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .onFocusChanged { focusState ->
                            if (isInitialFocus) {
                                isInitialFocus = false
                                return@onFocusChanged
                            }
                            localUserList.clear()
                            visible = true
                        }
                        .focusRequester(focusRequester)
                        .weight(1f, true)
                        .animateContentSize()
                )
                if (visible)
                    Text(
                        text = "Cancel",
                        fontSize = 15.sp,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .clickable {
                                visible = false
                                isInitialFocus = true
                                focusManager.clearFocus()
                                sharedViewModel.userList.forEach { user ->
                                    localUserList.add(
                                        user
                                    )
                                }
                            }
                            .padding(horizontal = 15.dp)
                    )
            }
        }
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            items(localUserList, key = { item -> item.id }) { user ->
                Box(modifier = Modifier.animateItemPlacement()) {
                    SwipeableBoxPreview(
                        navCtr, Modifier.padding(), sharedViewModel, user, onRemoveClicked = {
                            sharedViewModel.userList.remove(user)
                        }
                    )
                }

                Spacer(Modifier.height(1.dp))
                Divider(Modifier.padding(horizontal = 20.dp))
                Spacer(Modifier.height(1.dp))

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableBoxPreview(
    navCtr: NavHostController,
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    user: User,
    onRemoveClicked: (user: User) -> Unit
) {
    var isSnoozed by rememberSaveable { mutableStateOf(false) }
    var isArchived by rememberSaveable { mutableStateOf(false) }

    var isActive by rememberSaveable { mutableStateOf(false) }
    val editUser = SwipeAction(
        icon = rememberVectorPainter(Icons.Outlined.Edit),
        background = MaterialTheme.colorScheme.outline,
        onSwipe = {
            sharedViewModel.defineSelectedUserId(user.id.toString())
            sharedViewModel.defineIsNewUser(false)
            sharedViewModel.defineUsersFocus(true)
            scope?.launch {
                sheetState?.expand()
            }
//            navCtr.navigate(Screen.ManageUser.route)
        },
        isUndo = false,
    )

    val remove = SwipeAction(
        icon =
        rememberVectorPainter(Icons.TwoTone.Delete),
        background = MaterialTheme.colorScheme.error,
        onSwipe = {
//            isSnoozed = !isSnoozed
            println("IDDDD" + user.id)
            deleteUserAPI(user.id.toInt())
            onRemoveClicked(user)
            sharedViewModel.userList.remove(user)
        },
        isUndo = isSnoozed,
    )

    SwipeableActionsBox(
        modifier = modifier,
        startActions = listOf(editUser),
        endActions = listOf(remove),
        swipeThreshold = 100.dp,
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp),
    ) {
        SwipeItem(
            isSnoozed = isSnoozed, sharedViewModel = sharedViewModel, navCtr = navCtr, user = user
        )
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SwipeItem(
    modifier: Modifier = Modifier,
    isSnoozed: Boolean,
    sharedViewModel: SharedViewModel,
    navCtr: NavHostController,
    user: User
) {
    var nbrContracts: Int = 0
    for (contract in sharedViewModel.contractList) {
        if (contract.user_id == user.id.toString())
            nbrContracts++
    }
    Row(
        modifier
            .clickable(
                onClick = {
                    sharedViewModel.defineSelectedUserId(user.id.toString())
                    sharedViewModel.defineUsersFocus(true)
//                    getUserContract(user.id.toInt(), sharedViewModel)
                    println("Contra swiped")
                    navCtr.navigate(Screen.Contract.route)
                })
            .fillMaxWidth()
//            .shadow(1.dp)
//            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
            .padding(vertical = 16.dp, horizontal = 20.dp)

            .animateContentSize()

    ) {
        Box(
            Modifier
                .padding(top = 2.dp)
                .size(40.dp)
                .background(color = Color(0xFFC2C6BF), shape = CircleShape)
        ) {
            if (user.sex == "woman")
                Icon(Icons.Filled.Woman, "", Modifier.fillMaxSize())
            else
                Icon(Icons.Filled.Man, "", Modifier.fillMaxSize())


        }

        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = user.name.toString(), style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "ID: ${user.id} | Role: ${user.role} | Contracts: $nbrContracts"
            )

            if (isSnoozed) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .background(Color.SeaBuckthorn.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    text = "Snoozed until tomorrow",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

val Color.Companion.SeaBuckthorn get() = Color(0xFFF9A825)
val Color.Companion.Fern get() = Color(0xFF66BB6A)
val Color.Companion.Perfume get() = Color(0xFFD0BCFF)


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun Preview22() {
    UsersTab(TODO(), TODO())
}