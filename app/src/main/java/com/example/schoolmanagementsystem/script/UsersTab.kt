package com.example.schoolmanagementsystem.ui.theme

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.GetImage
import com.example.schoolmanagementsystem.script.LoadingIndicator
import com.example.schoolmanagementsystem.script.ManageUser
import com.example.schoolmanagementsystem.script.Notification
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.script.User
import com.example.schoolmanagementsystem.script.deleteUserAPI
import com.example.schoolmanagementsystem.script.isInternetAvailable
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.example.schoolmanagementsystem.script.usersAPI
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

var scope: CoroutineScope? = null

@OptIn(ExperimentalMaterial3Api::class)
var sheetState: SheetState? = null

var localUserList = SnapshotStateList<User>()
var textFieldValue by mutableStateOf<String>("")
var visible by mutableStateOf(false)
val focusRequester = FocusRequester()

//val focusManager = LocalFocusManager
var isInitialFocus by mutableStateOf(true)

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun UsersTab(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    val minSwipeOffset by remember { mutableStateOf(300f) }
    var offsetX by remember { mutableStateOf(0f) }
    val focusManager = LocalFocusManager.current
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = true
    )
    if (sharedViewModel.user?.role == "admin")
    // make sure the fab is visible in this screen for this role
        sharedViewModel.defineFabVisible(true)
    else
        sharedViewModel.defineFabVisible(false)

    // Setting up bottom sheet
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
//            tonalElevation = 50.dp,
            scrimColor = Color.Transparent,
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
                ManageUser(
                    sharedViewModel = sharedViewModel,
                    scope = scope,
                    state = sheetState,
                    focusManager = focusManager
                )
            },
            modifier = Modifier.offset(y = 0.dp)
        )
    } else
        sharedViewModel.defineFABClicked(false)

    var context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        sharedViewModel.defineIsOnBox(false)
        clearSearch(sharedViewModel, focusManager)
        //setting up fab in user tab
        sharedViewModel.defineFabClick {
            sharedViewModel.defineIsNewUser(true)
            scope?.launch {
                sheetState?.show()

            }
        }
        if (!isInternetAvailable(context)) {
            var notif = Notification(context)
            notif.FireNotification()
            return@LaunchedEffect
        }
        sharedViewModel.userList.clear()
        sharedViewModel.contractList.clear()
        sharedViewModel.paymentList.clear()
        usersAPI(sharedViewModel = sharedViewModel)
    }
    // Feed unfiltered user list to display (localUserList)
    if (textFieldValue.isEmpty() && !visible) {

        localUserList.clear()
        sharedViewModel.userList.forEach { user -> localUserList.add(user) }
    }

//    return
    val users = remember { sharedViewModel.userList }
    val pullRefreshState = rememberPullRefreshState(sharedViewModel.isRefreshing, {
        sharedViewModel.userList.clear()
        sharedViewModel.contractList.clear()
        sharedViewModel.paymentList.clear()
        usersAPI(sharedViewModel = sharedViewModel)
    })
    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF3F7CC4), Color(0xFF7AB8FF))
                )
            )
            .padding(top = 5.dp)
    ) {

        /********************************************************
        setting up the search bar
         *********************************************************/

        BoxWithConstraints(
            Modifier
                .zIndex(1f)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 15.dp)
//                    .background(Color.Red)
                    .padding(bottom = 13.dp),
//                        .zIndex(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(onDone = { /* Do something */ }),
                    value = textFieldValue,
                    textStyle = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),

                    onValueChange = {
                        textFieldValue = it
                        searchUser(textFieldValue, localUserList, sharedViewModel)
                    },
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.o),
                                    contentDescription = "",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .padding(start = 6.dp, end = 7.dp)
                                        .size(17.dp)
                                        .scale(1.2f),
                                )
                                innerTextField()

                            }

                            if (textFieldValue.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.search_users),
                                    color = Color.Gray,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .padding(start = 35.dp)
                                        .padding(vertical = 0.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(end = 5.dp, start = 0.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .onFocusChanged { focusState ->
                            if (!focusState.hasFocus) {
                                isInitialFocus = false
                                return@onFocusChanged
                            }
                            // This happen when search bar is focused
                            sharedViewModel.defineFabVisible(false)
                            sharedViewModel.defineSearchBarFocused(true)
                            localUserList.clear()
                            visible = true
                        }
                        .focusRequester(focusRequester)
                        .weight(1f, true)
                        .animateContentSize()
                )
                if (visible)
                    androidx.compose.material3.Button(
                        onClick = {
                            clearSearch(sharedViewModel, focusManager)
                            sharedViewModel.defineSearchBarFocused(false)
                        },
                        colors = ButtonDefaults.buttonColors(Color.White),
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(25.dp))

                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            fontSize = 15.sp,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier
                        )
                    }
            }
        }
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = true
        )
        Column(
            Modifier
//                .background(Color.Red)
                .background(Color.Transparent)
                .padding(top = 0.dp, bottom = 0.dp)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
//                    .background(Color.Red)
                    .padding(top = 20.dp)
                    .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))

                    .background(MaterialTheme.colorScheme.surface)
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
                                        navCtr.navigate(Screen.Inbox.route)
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
                /****************************************
                users list
                 ***************************************/
//                val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

                if (localUserList.isEmpty() && !visible)
                    Box(
                        Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
                    ) {
                        LoadingIndicator(true)
                    }
                else {
//                    localUserList.filter { it.id == sharedViewModel.user?.id }
                    LazyColumn(
                        state = rememberLazyListState(),
                        modifier = Modifier
                            .padding(bottom = 45.dp, top = 8.dp)
                            .padding(horizontal = 7.dp)
                    ) {
                        item(key = "0") {
                            Spacer(Modifier.height(20.dp)) // To add some padding on top of the list
                        }
                        items(
                            items = if (sharedViewModel.user?.role != "admin") localUserList.filter { it.id == sharedViewModel.user?.id }
                            else localUserList,
                            key = { item -> item.id }) { user ->

                            Box(modifier = Modifier.animateItemPlacement()) {
                                if (sharedViewModel.user?.role == "admin")
                                    SwipeableBoxPreview(
                                        navCtr,
                                        Modifier.padding(),
                                        sharedViewModel,
                                        user,
                                        onRemoveClicked = {
                                            sharedViewModel.userList.remove(user)
                                        },
                                    )
                                else
                                    Card(
                                        Modifier
                                            .fillMaxWidth()
                                            .background(Color.White),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                                    ) {
                                        SwipeItem(
                                            sharedViewModel = sharedViewModel,
                                            navCtr = navCtr,
                                            user = user
                                        )
                                    }


                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = sharedViewModel.isRefreshing,
            state = pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }

}

fun clearSearch(sharedViewModel: SharedViewModel, focusManager: FocusManager) {
    localUserList.clear()
    textFieldValue = ""
    visible = false
    isInitialFocus = true
    focusManager.clearFocus()
    if (sharedViewModel?.user?.role == "admin")
        sharedViewModel.defineFabVisible(true)
    sharedViewModel.userList.forEach { user ->
        localUserList.add(
            user
        )
    }
}

fun searchUser(
    name: String,
    localUserList: SnapshotStateList<User>,
    sharedViewModel: SharedViewModel
) {
    if (name.isEmpty()) {
        localUserList.clear()
        return
    }
    localUserList.clear()
    sharedViewModel.userList.forEach { user ->
        if (user.name!!.contains(name, true)) localUserList.add(
            user
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableBoxPreview(
    navCtr: NavHostController,
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    user: User,
    onRemoveClicked: (user: User) -> Unit,
) {
    var isSnoozed by rememberSaveable { mutableStateOf(false) }

    val editUser = SwipeAction(
        icon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "",
                tint = Color(0xFFFFFFFF),
                modifier = Modifier.scale(1f)
            )

        },
        background = MaterialTheme.colorScheme.onSurface,
        onSwipe = {
            sharedViewModel.defineSelectedUserId(user.id.toString())
            sharedViewModel.defineIsNewUser(false)
            sharedViewModel.defineUsersFocus(true)
            scope?.launch {
                sheetState?.expand()
            }
        },
        isUndo = false,
    )

    val remove = SwipeAction(
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                tint = Color(0xCCFFFFFF),
                modifier = Modifier.scale(1f)
            )
        },
        background = MaterialTheme.colorScheme.error,
        onSwipe = {
            println("IDDDD" + user.id)
            deleteUserAPI(user.id.toInt())
            onRemoveClicked(user)
            sharedViewModel.userList.remove(user)
        },
        isUndo = isSnoozed,
    )
    Card(
        Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)

    ) {

        SwipeableActionsBox(
            modifier = modifier.background(Color.White),
            startActions = listOf(editUser),
            endActions = listOf(remove),
            swipeThreshold = 100.dp,
            backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp),
        ) {
            SwipeItem(
                sharedViewModel = sharedViewModel,
                navCtr = navCtr,
                user = user
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun SwipeItem(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    navCtr: NavHostController,
    user: User
) {
    var nbrContracts: Int = 0
    for (contract in sharedViewModel.contractList) {
        if (contract.user_id == user.id.toString())
            nbrContracts++
    }
    /*
                      this come above the card
     */
    Row(
        modifier = Modifier
            .background(Color.Transparent)
            .clickable(
                onClick = {
                    sharedViewModel.defineSelectedUserId(user.id.toString())
                    sharedViewModel.defineUsersFocus(true)
                    println("Contra swiped")
                    navCtr.navigate(Screen.Contract.route)
                })
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)

            .animateContentSize()

    ) {
/*
                setting the left vertical blue marker
 */
        if (sharedViewModel.user?.role == "admin")
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .height(80.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(8.dp)
//                    .clip(shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                        .height(20.dp)
                        .background(MaterialTheme.colorScheme.inverseSurface)
                )
            }
        /*
                    setting up the left image
         */
        Row(
            Modifier
                .padding(top = 2.dp, start = 10.dp)
                .fillMaxHeight()
//                .size(40.dp)
                .background(color = Color.Transparent, shape = CircleShape),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            // Handling user photo whether it's from url or it doesn't exist
            GetImage(user = user)
        }
//setting up the middle texts
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp)
//                .background(Color.Yellow)
                .fillMaxHeight(),
//            Arrangement.Center
        ) {
            Text(
                text = user.name.toString(), style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "CIN: ${user.cin} \nRole: ${user.role} | Contracts: $nbrContracts"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        //setting the right vertical red marker
        if (sharedViewModel.user?.role == "admin")
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .height(80.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(8.dp)
                        .clip(shape = RoundedCornerShape(topEnd = 25.dp, bottomEnd = 25.dp))
                        .height(20.dp)
//                    .onGloballyPositioned { coo -> size = coo.size.toSize() }
                        .background(Color(0x80FFD7D7))
                )
            }
    }
}

@Composable
fun LoadingAnimation() {
    val animation = rememberInfiniteTransition()
    val progress by animation.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart,
        )
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .scale(progress)
            .alpha(1f - progress)
            .border(
                5.dp,
                color = Color.Black,
                shape = CircleShape
            )
    )
}

@Preview
@Composable
fun Preview22() {
    UsersTab(TODO(), TODO())
}