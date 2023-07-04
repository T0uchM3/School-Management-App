package com.example.schoolmanagementsystem.ui.theme

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBackIos
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
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
import com.example.schoolmanagementsystem.script.homeTab.LoadingIndicator
import com.example.schoolmanagementsystem.script.model.Message
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.script.model.User
import com.example.schoolmanagementsystem.script.allUsersAPI
import com.example.schoolmanagementsystem.script.getMessages
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

var localMessageList = SnapshotStateList<Message>()
var recipientSelected = mutableStateOf(false)
var userToMsg = User()
var unseenNbr: MutableList<Int> = ArrayList()
var unseenId: MutableList<String> = ArrayList()

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun InboxScreen(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    sharedViewModel.defineUsersFocus(false)
    val minSwipeOffset by remember { mutableStateOf(300f) }
    var offsetX by remember { mutableStateOf(0f) }
    val focusManager = LocalFocusManager.current
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = true
    )
    // make sure the fab is visible in this screen
    sharedViewModel?.defineFabVisible(true)
    // Setting up bottom sheet
    sheetState =
        remember {
            SheetState(
                skipHiddenState = false,
                skipPartiallyExpanded = true,
                initialValue = SheetValue.Hidden,
                confirmValueChange = { false }
            )
        }
    scope = rememberCoroutineScope()
    if (sheetState?.isVisible == true) {
        ModalBottomSheet(
            sheetState = sheetState!!,
            scrimColor = Color.Transparent,
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
                SelectRecipient(
                    navCtr = navCtr,
                    sharedViewModel = sharedViewModel,
                    scope = scope,
                    state = sheetState,
                )
            },
            modifier = Modifier.offset(y = 0.dp)
        )
    } else
        sharedViewModel.defineFABClicked(false)


    LaunchedEffect(key1 = Unit) {
        unseenId.clear()
        unseenNbr.clear()
        sharedViewModel.defineIsOnBox(true)
        sharedViewModel.defineFabVisible(true)
        clearSearch(sharedViewModel, focusManager)
        // Setting up fab in user tab
        sharedViewModel.defineFabClick {
            sharedViewModel.defineIsNewUser(true)
            scope?.launch {
                sheetState?.show()
//                sharedViewModel.defineFABClicked(true)
            }
        }

        // So getting messages can fail apparently and crash the app
        // used supervisorScope to fix the crash
        // and when that happens just wait 1 second and call again the api
        // meanwhile a nice loading indicator is keeping the user's company ;)
        sharedViewModel.defineGetMessageWorked(false)
        while (!sharedViewModel.getMessageWorked) {
            getMessages(sharedViewModel.user!!.id.toInt(), sharedViewModel)
            delay(1000)
        }
    }
//     unseenId= remember { ArrayList() }
//     unseenNbr= remember { ArrayList() }

    LaunchedEffect(key1 = sharedViewModel.messageList.size) {
        unseenId.clear()
        unseenNbr.clear()
        getUniqueMessages(sharedViewModel)
        println("unseenNbr SIZES  " + unseenNbr.size)
        println("unseenId SIZES  " + unseenId.size)
        unseenNbr.forEach { unbr ->
            println(" nbr " + unbr)
        }
        unseenId.forEach { unbr ->
            println(" stringId " + unbr)
        }
    }
    val pullRefreshState = rememberPullRefreshState(sharedViewModel.isRefreshing, {
        getMessages(sharedViewModel.user!!.id.toInt(), sharedViewModel)

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
                        searchMsgUser(textFieldValue, sharedViewModel)
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
                                    fontSize = 18.sp,
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
                            sharedViewModel.defineFabVisible(false)
                            sharedViewModel.defineSearchBarFocused(true)
                            sharedViewModel.tempMsgSearchList.clear()
                            sharedViewModel.defineTempMsgSearchList(localMessageList)
                            localMessageList.clear()
                            visible = true
                        }
                        .focusRequester(focusRequester)
                        .weight(1f, true)
                        .animateContentSize()
                )
                if (visible)
                    Button(
                        onClick = {
                            clearMsgSearch(sharedViewModel, focusManager)
                            sharedViewModel.defineSearchBarFocused(false)
                        },
                        colors = ButtonDefaults.buttonColors(Color.White),
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(25.dp))

                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 15.sp,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier
                        )
                    }
            }
        }

        Column(
            Modifier
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
                                        navCtr.navigate(Screen.Profile.route)
                                    }

                                    (offsetX > 0 && Math.abs(offsetX) > minSwipeOffset) -> {
                                        println(" SwipeDirection.Right")
                                        navCtr.navigate(Screen.Users.route)
                                    }

                                    else -> null

                                }
                                offsetX = 0F
                            }
                        )
                    }
            ) {

                /****************************************
                            message list
                 ***************************************/
                // Spin while messageList is empty AND getMessage DIDN'T work
                if (localMessageList.isEmpty() && !sharedViewModel.getMessageWorked)
                    Box(
                        Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
                    ) {
                        LoadingIndicator(isDark = true)
                    }
                else if (localMessageList.isEmpty() && sharedViewModel.getMessageWorked)
                    Box(
                        Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.no_conversations),
                            color = Color.DarkGray,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 35.dp)
                                .padding(vertical = 0.dp)
                        )
                    }
                else
                    LazyColumn(
                        state = rememberLazyListState(),
                        modifier = Modifier
                            .padding(bottom = 45.dp, top = 25.dp)
//                            .background(Color.Red)
                            .padding(horizontal = 7.dp)
                    ) {
                        items(localMessageList, key = { item -> item.id }) { message ->
                            Box(modifier = Modifier.animateItemPlacement()) {
                                SwipeableMsgBox(
                                    navCtr,
                                    Modifier.padding(),
                                    sharedViewModel,
                                    message,
                                    onRemoveClicked = {
//                                        sharedViewModel.userList.remove(user)
                                    },
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    }
            }
        }
        PullRefreshIndicator(refreshing = sharedViewModel.isRefreshing, state = pullRefreshState,Modifier.align(Alignment.TopCenter))
    }
}

fun searchMsgUser(
    name: String,
    sharedViewModel: SharedViewModel
) {
    println("inside search msg user")
    // First thing we clear the list
    if (name.isEmpty()) {
        localMessageList.clear()
        return
    }
    localMessageList.clear()
    sharedViewModel.tempMsgSearchList.forEach { msg ->
        if (msg.from_user?.name!!.contains(name, true) ||msg.to_user?.name!!.contains(name, true)) localMessageList.add(
            msg
        )
    }
}

fun clearMsgSearch(sharedViewModel: SharedViewModel, focusManager: FocusManager) {
    localUserList.clear()
    textFieldValue = ""
    visible = false
    isInitialFocus = true
    focusManager.clearFocus()
    sharedViewModel.defineFabVisible(true)
    localMessageList.clear()
    sharedViewModel.tempMsgSearchList.forEach { msg ->
        localMessageList.add(
            msg
        )
    }
}

fun getUniqueMessages(sharedViewModel: SharedViewModel) {
    if (textFieldValue.isEmpty() && !visible) {
        localMessageList.clear()
        // So we have 2 message lists here, messageList which is filled directly from API call
        // and localMessageList which is a filtered list that contains only 1 instance of message.
        sharedViewModel.messageList.forEach { message ->
            var foundUnseen = false
            var skip = false
            // We check if this message in this loop exist inside localMessageList or not
            localMessageList.forEach { innerMessage ->
                // Ex: 6+36 in localMessageList, 6+61 is inc msg to check (this should fail this condition and get added to locallist)
                if ((message.to_user_id == innerMessage.to_user_id || message.to_user_id == innerMessage.from_user_id) &&
                    (message.from_user_id == innerMessage.to_user_id || message.from_user_id == innerMessage.from_user_id)
                ) {
                    skip = true
                }
            }
            if (!skip) {
                localMessageList.add(message)
                if (message.vu == "0" && message.to_user_id == sharedViewModel.user?.id.toString()) {
                    // Saving values like this: 6+36 6+73 6+61
                    unseenId.add(message.from_user_id + "+" + message.to_user_id)
                    unseenNbr.add(0)// this will get incremented next call bellow
                }

                skip = false
            }

            if (message.vu == "0" && message.to_user_id == sharedViewModel.user?.id.toString()) {
                unseenId.forEach { msgId ->
                    if (msgId == message.from_user_id + "+" + message.to_user_id) {
                        unseenNbr[unseenId.indexOf(msgId)] =
                            unseenNbr[unseenId.indexOf(msgId)] + 1
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SelectRecipient(
    sharedViewModel: SharedViewModel,
    navCtr: NavHostController,
    scope: CoroutineScope? = null,
    state: SheetState? = null,
) {
    LaunchedEffect(key1 = Unit) {
        sharedViewModel.defineFABClicked(true)
        if (sharedViewModel.userList.isEmpty())
//            usersAPI(sharedViewModel = sharedViewModel)
            allUsersAPI(sharedViewModel=sharedViewModel)
    }

    Column(
        Modifier
//            .background(Color.Red)
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                sharedViewModel.defineFABClicked(false)
                scope?.launch { state?.hide() }
            }) {
                Icon(
                    Icons.TwoTone.ArrowBackIos,
                    "",
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .scale(1.3f)
                        .padding(end = 10.dp)
                )
            }
//            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(R.string.select_recipient),
                fontSize = 21.sp,
                color = Color.DarkGray,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        var selectedIndex by remember { mutableStateOf(0) }
        val onItemClick = { index: Int -> selectedIndex = index }
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
                    .padding(bottom = 8.dp, top = 2.dp)
                    .padding(horizontal = 7.dp)
            ) {
                item(key = "0") {
                    Spacer(Modifier.height(5.dp)) // To add some padding on top of the list
                }
                items(sharedViewModel.userList.filterNot { it.id == sharedViewModel.user?.id }, key = { item -> item.id }) { user ->
                    Spacer(Modifier.height(8.dp))
                    Box(modifier = Modifier.animateItemPlacement()) {
                        RecipientToSelect(
                            selected = selectedIndex == user.id.toInt(),
                            onClick = onItemClick,
                            navCtr = navCtr,
                            sharedViewModel = sharedViewModel,
                            user = user,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Divider()
                }
            }
            //         FAB
            SelectRecipientFAB(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp),
                isVisibleBecauseUserSelected = recipientSelected.value,
                sharedViewModel = sharedViewModel,
                navCtr = navCtr
            )
        }

    }
}

@Composable
private fun SelectRecipientFAB(
    modifier: Modifier,
    isVisibleBecauseUserSelected: Boolean,
    sharedViewModel: SharedViewModel,
    navCtr: NavHostController
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisibleBecauseUserSelected,
        enter = slideInVertically {
            with(density) { 80.dp.roundToPx() }
        } + fadeIn(),
        exit = fadeOut(
            animationSpec = keyframes {
                this.durationMillis = 80
            }
        )
    ) {
        ExtendedFloatingActionButton(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(40.dp),
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            onClick = {
                recipientSelected.value = false
                sharedViewModel.targetedMessageList.clear()
                sharedViewModel.messageList.clear()
                sharedViewModel.defineUserToMsg(userToMsg)
                navCtr.navigate(Screen.Messages.route)
            },
        ) {
            androidx.compose.material3.Text(
                text = stringResource(R.string.done),
                fontSize = 17.sp,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun RecipientToSelect(
    navCtr: NavHostController,
    sharedViewModel: SharedViewModel,
    user: User,
    selected: Boolean,
    onClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                if (selected) {     // Deselect
                    onClick.invoke(-1)
                    recipientSelected.value = false
                } else {        // Select
                    onClick.invoke(user.id.toInt())
                    userToMsg = user
                    recipientSelected.value = true
                }

            }
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)

            .animateContentSize(),
        Arrangement.Center,
        Alignment.CenterVertically

    ) {
        /*
                    setting up the left image
         */
        Row(
            Modifier
                .fillMaxHeight()
                .background(color = Color.Transparent, shape = CircleShape),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {

            // Handling user photo whether it's from url or it doesn't exist
            GetImage(user = user)
        }
        // Setting up the middle texts
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp)
                .weight(0.8f)
                .fillMaxHeight(),
        ) {
            Text(
                text = user.name.toString(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 15.sp,
                    color = Color(0xFF3C3C3C)
                ),
                text = "Email: ${user.email} \nPhone: ${if (user.tel == null) "None" else user.tel}"
            )

        }
//        Spacer(modifier = Modifier.weight(1f))
        Row(
            Modifier
                .padding(top = 2.dp, start = 10.dp)
                .weight(0.2f)
                .fillMaxHeight(), Arrangement.Center,
            Alignment.CenterVertically
        ) {

            // Checkbox image for indicating of user is selected or not
            Icon(
                painterResource(id = R.drawable.x),
                contentDescription = "",
                tint = if (selected) MaterialTheme.colorScheme.inverseSurface else Color.LightGray,
                modifier = Modifier.scale(0.6f)

            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableMsgBox(
    navCtr: NavHostController,
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    message: Message,
    onRemoveClicked: (message: Message) -> Unit,
) {
    var isSnoozed by rememberSaveable { mutableStateOf(false) }

    val editUser = SwipeAction(
        icon = {
            Icon(
                painterResource(id = R.drawable.j),
                contentDescription = "",
                tint = Color(0xCCF1F1F1),
                modifier = Modifier.scale(0.6f)
            )

        },
        background = MaterialTheme.colorScheme.onSurface,
        onSwipe = {
//            sharedViewModel.defineSelectedUserId(message.id.toString())
//            sharedViewModel.defineIsNewUser(false)
//            sharedViewModel.defineUsersFocus(true)
//            scope?.launch {
//                sheetState?.expand()
//            }
        },
        isUndo = false,
    )

    val remove = SwipeAction(
        icon =
        rememberVectorPainter(Icons.TwoTone.Delete),
        background = MaterialTheme.colorScheme.error,
        onSwipe = {
//            println("IDDDD" + message.id)
//            deleteUserAPI(user.id.toInt())
//            onRemoveClicked(user)
//            sharedViewModel.userList.remove(user)
        },
        isUndo = isSnoozed,
    )
    Card(
        Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Color.LightGray)
//        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
//        colors = CardDefaults.cardColors(Color.Transparent)
    ) {

        SwipeableActionsBox(
            modifier = modifier.background(Color.White),
//            startActions = listOf(editUser),
//            endActions = listOf(remove),
            swipeThreshold = 100.dp,
            backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp),
        ) {
            SwipeInboxItem(
                sharedViewModel = sharedViewModel,
                navCtr = navCtr,
                message = message
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun SwipeInboxItem(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    navCtr: NavHostController,
    message: Message
) {
    var nbrContracts: Int = 0
    // So each message has a sender and receiver, one of those two is the current user
    // So we only take and save the one who isn't the current user.
    val contactedUser =
        if (message.to_user!!.id != sharedViewModel.user!!.id) message.to_user else message.from_user
    /*
                      this come above the card
     */
    Row(
        modifier = Modifier
            .clickable(
                onClick = {
                    println("user to message " + contactedUser?.id)
                    sharedViewModel.defineUserToMsg(contactedUser!!)
                    navCtr.navigate(Screen.Messages.route)
                })
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
            .background(MaterialTheme.colorScheme.surface)
            .animateContentSize()

    ) {
/*
                setting the left vertical blue marker
 */
//        Column(
//            modifier = Modifier
//                .fillMaxHeight()
//                .height(80.dp)
//                .wrapContentSize(Alignment.Center)
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(8.dp)
////                    .clip(shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
//                    .height(20.dp)
//                    .background(MaterialTheme.colorScheme.inverseSurface)
//            )
//        }
        /*
                    setting up the left image
         */


        Row(
            Modifier
                .padding(top = 2.dp, start = 10.dp)
                .fillMaxHeight()
//                .size(40.dp)
                .background(color = Color.Transparent, shape = CircleShape)
                .padding(5.dp),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            GetImage(user = contactedUser!!)
        }
        //                  setting up the middle texts
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp)
//                .background(Color.Yellow)
                .fillMaxHeight(),
//            Arrangement.Center
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = contactedUser?.name.toString(),
                    modifier = Modifier.weight(0.7f),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                )
//                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = message.created_at.toString().substring(0, 10),
                    modifier = Modifier.weight(0.3f),
                    style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF3C3C3C), fontSize = 12.sp)
                )
            }
            var unseenMessages by remember { mutableStateOf(0) }
            println(message.from_user_id + " message${unseenId.size}IDS " + message.to_user_id)
            unseenId.forEach { unseeen ->
                if (unseeen == message.from_user_id + "+" + message.to_user_id) {

                    unseenMessages = unseenNbr.get(unseenId.indexOf(unseeen))
                } else if (unseeen == message.from_user_id + "+" + message.to_user_id) {
                    unseenMessages = unseenNbr.get(unseenId.indexOf(unseeen))

                }
                println("unseenMessages " + unseenMessages)
            }
            Row(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .weight(0.83f),
                    text = message.message.toString(),
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF3C3C3C))
                )
                Spacer(modifier = Modifier.weight(0.1f))
                if (unseenMessages != 0)
                    Text(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.inverseSurface,
                                CircleShape
                            )
                            .padding(horizontal = 5.dp, vertical = 3.dp)
                            .weight(0.08f),
                        text = " $unseenMessages ",
                        color = Color.White,
                        fontSize = 13.sp

                    )
            }

        }
        Spacer(modifier = Modifier.weight(1f))
        //setting the right vertical red marker
//        Column(
//            modifier = Modifier
//                .fillMaxHeight()
//                .height(80.dp)
//                .wrapContentSize(Alignment.Center)
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(8.dp)
//                    .clip(shape = RoundedCornerShape(topEnd = 25.dp, bottomEnd = 25.dp))
//                    .height(20.dp)
////                    .onGloballyPositioned { coo -> size = coo.size.toSize() }
//                    .background(Color(0x80FFD7D7))
//            )
//        }

    }
}

fun searchMsg(
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

@Preview
@Composable
fun Preview33() {
//    MessageSecreen()
}