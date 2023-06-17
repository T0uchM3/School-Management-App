package com.example.schoolmanagementsystem.script

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.CircleCropTransformation
import com.example.schoolmanagementsystem.BuildConfig
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.ui.theme.LoadingAnimation
import com.example.schoolmanagementsystem.ui.theme.focusRequester
import com.example.schoolmanagementsystem.ui.theme.isInitialFocus
import com.example.schoolmanagementsystem.ui.theme.localMessageList
import com.example.schoolmanagementsystem.ui.theme.localUserList
import com.example.schoolmanagementsystem.ui.theme.scope
import com.example.schoolmanagementsystem.ui.theme.searchUser
import com.example.schoolmanagementsystem.ui.theme.sheetState
import com.example.schoolmanagementsystem.ui.theme.textFieldValue
import com.example.schoolmanagementsystem.ui.theme.visible
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait

var waiting = mutableStateOf(false)
var msgLongPress = mutableStateOf(false)


@OptIn(
    ExperimentalMaterial3Api::class
)
@SuppressLint("RememberReturnType")
@Composable
fun MessageScreen(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    sharedViewModel.defineSearchBarFocused(false)
    val isEdit = remember { mutableStateOf(false) }
    payments = remember { SnapshotStateList<Payment>() }
    val userName = remember { mutableStateOf("") }
    var scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var runOnce by remember { mutableStateOf(true) }
    BackPressHandlerMsg(navCtr = navCtr, sharedViewModel = sharedViewModel)
    // In case user came to Contract view from searching
    // Fix status bar
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = true
    )
    sheetState = remember {
        SheetState(
            skipHiddenState = false, skipPartiallyExpanded = true, initialValue = SheetValue.Hidden
        )
    }
    scope = rememberCoroutineScope()
    if (sheetState?.isVisible == true) {
        ModalBottomSheet(sheetState = sheetState!!, dragHandle = null, shape = RoundedCornerShape(
            bottomStart = 0.dp, bottomEnd = 0.dp, topStart = 12.dp, topEnd = 12.dp
        ), onDismissRequest = {
            scope?.launch {
                sheetState?.hide()
            }
        }, content = {
//            if (sheetAction == "add") {
            isEdit.value = false

//            }
//            if (sheetAction == "edit") {
//                isEdit.value = true
//                ManagePaymentSheet(
//                    sharedViewModel = sharedViewModel,
//                    scope = scope,
//                    state = sheetState,
//                    isEdit = isEdit
//                )
//            }
        })
    } else sharedViewModel.defineFABClicked(false)

    // This will run only once
    LaunchedEffect(key1 = Unit) {

        scrollState.animateScrollToItem(sharedViewModel.targetedMessageList.size)
//        // Setting up fab
        sharedViewModel.defineFabVisible(false)
        // So getting messages can fail apparently and crash the app
        // used supervisorScope to fix the crash
        // and when that happens just wait 1 second and call again the api
        // meanwhile a nice loading indicator is keeping the user's company ;)
        sharedViewModel.defineGetMessageWorked(false)
        while (!sharedViewModel.getMessageWorked) {
            waiting.value = true
            getMessages(sharedViewModel.user!!.id.toInt(), sharedViewModel)
            delay(1000)
            waiting.value = false
        }
        // Updating messages view status

        updateMessageView(sharedViewModel.unseenMessages!!, sharedViewModel)
        while (true) {
            delay(100)
            scrollState.animateScrollToItem(sharedViewModel.targetedMessageList.size)
        }
    }
    // Setting up top bar holder
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF3F7CC4), Color(0xFF7AB8FF))
                )
            )
            .padding(top = 5.dp)
    ) {
        // Top bar that contains the back arrow
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            IconButton(onClick = {
                sharedViewModel.defineSelectedContract(null)
                sharedViewModel.messageList.clear()
                sharedViewModel.targetedMessageList.clear()
                navCtr.popBackStack()
            }) {
                Icon(
                    Icons.TwoTone.ArrowBack,
                    "",
                    tint = Color(0xCCFFFFFF),
                    modifier = Modifier
                        .scale(1.3f)
                        .padding(end = 5.dp)
                )
            }
            GetImage(sharedViewModel.userToMessage!!, true)
            Text(
                text = sharedViewModel.userToMessage!!.name.toString(),
                color = Color(0xCCFFFFFF),
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

        }
        Column(
            Modifier
                .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
//                .imePadding()
//                .fillMaxHeight()
        ) {
            /**
             *          setting up the list section
             */
            if (waiting.value)
//            if (sharedViewModel.messageList.isEmpty())
                Box(
                    Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
                ) {
                    LoadingIndicator(isDark = true)
                }

            println("tageted msg size  " + sharedViewModel.targetedMessageList.size)
            var dayHolder = 0
            // Doing "else" here really make this whole circus stop
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .padding(bottom = 0.dp, top = 10.dp)
                    .padding(horizontal = 12.dp)
                    .weight(1f, true)
            ) {
                items(
                    sharedViewModel.targetedMessageList.sortedBy { it.created_at },
                    key = { item -> item.id }) { message ->
                    Spacer(Modifier.height(10.dp))
                    /*
                                HANDLING DATES
                     */
                    if (dayHolder != 0) {
                        if (message.created_at.toString().substring(8, 10)
                                .toInt() != dayHolder
                        ) {
                            Text(
                                text = message.created_at.toString().substring(0, 10),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall.copy(color = Color.DarkGray),
                                modifier = Modifier
                                    .padding(horizontal = 13.dp, vertical = 5.dp)
                                    .fillMaxWidth()
                            )
                            dayHolder = message.created_at.toString().substring(8, 10).toInt()
                        }
                    } else {
                        dayHolder = message.created_at.toString().substring(8, 10).toInt()
                        Text(
                            text = message.created_at.toString().substring(0, 10),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleSmall.copy(color = Color.DarkGray),
                            modifier = Modifier
                                .padding(horizontal = 13.dp, vertical = 5.dp)
                                .fillMaxWidth()
                        )
                    }
                    /*
                                         HANDLING ACTUAL MESSAGE
                   */
                    Spacer(Modifier.height(10.dp))
                    MessageBox(
                        sharedViewModel,
                        message,
                        onRemoveClicked = {
                            sharedViewModel.targetedMessageList.remove(message)
                        },
                    )
                    Spacer(Modifier.height(20.dp))
                }
            }
//            Spacer(Modifier.weight(1f,true))

//            }
            Row(
                Modifier
                    .background(Color(0xFFF7F7F7))
                    .wrapContentHeight(Alignment.Bottom)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.Bottom
//                .align(Alignment.BottomEnd)
            ) {
                BasicTextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(onDone = {

                    }),
                    value = textFieldValue,
                    textStyle = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),

                    onValueChange = {
                        textFieldValue = it
                        searchUser(textFieldValue, localUserList, sharedViewModel)
                        visible = textFieldValue.isNotEmpty()
                    },
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 20.dp, end = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {

                                innerTextField()

                            }

                            if (textFieldValue.isEmpty()) {
                                androidx.compose.material.Text(
                                    text = "Enter message",
                                    color = Color.Gray,
                                    fontSize = 17.sp,
                                    modifier = Modifier
                                        .padding(start = 20.dp)
                                        .padding(vertical = 0.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(end = 5.dp, start = 0.dp)
                        .background(
                            Color(0x6FCACACA),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .onFocusChanged { focusState ->
                            if (!focusState.hasFocus) {
                                isInitialFocus = false
                                return@onFocusChanged
                            }
                            coroutineScope.launch {
//                                if (runOnce) {
                                while (true) {
                                    delay(300)
                                    // do something after 1 second
                                    scrollState.animateScrollToItem(sharedViewModel.targetedMessageList.size)
                                    runOnce = false
//                                    }
                                }
                            }
                            sharedViewModel.defineFabVisible(false)
                            localUserList.clear()
//                            visible = true
                        }
                        .focusRequester(focusRequester)
                        .weight(1f, true)
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearOutSlowInEasing
                            )
                        )
                )
                AnimatedVisibility(visible = visible) {
                    Box(
                        modifier = Modifier
                            .padding(top = 5.dp)

                            .clip(shape = CircleShape)
                            .background(MaterialTheme.colorScheme.inverseSurface)
//                            .padding(6.dp)
                            .padding(4.dp)
                            .size(23.dp)
                            .clickable(onClick = {
                                if (sharedViewModel.spinSendBtn)
                                    return@clickable
                                var message = MessageToSend()
//                                message.from_user_id = sharedViewModel.user!!.id.toString()
//                                message.to_user_id = sharedViewModel.userToMessage!!.id.toString()
                                message.sujet =
                                    if (textFieldValue.length > 30) textFieldValue.substring(
                                        0,
                                        textFieldValue.length / 2
                                    ) else textFieldValue// ah well! (not shown on phone ui)
                                message.to_user_id =
                                    listOf(sharedViewModel.userToMessage!!.id.toInt())
                                message.message = textFieldValue
                                textFieldValue = ""
                                sharedViewModel.defineSpinSendBtn(true)
                                sendMessage(
                                    sharedViewModel.user!!.id.toInt(),
                                    message,
                                    sharedViewModel,
                                    true
                                )
                                coroutineScope.launch {
//                                if (runOnce) {
                                    while (true) {
                                        delay(300)
                                        // do something after 1 second
                                        scrollState.animateScrollToItem(sharedViewModel.targetedMessageList.size)
                                        runOnce = false
//                                    }
                                    }
                                }
                            })
                    )
                    {
                        if (!sharedViewModel.spinSendBtn)
                            Icon(
                                painterResource(id = R.drawable.send2),
                                contentDescription = "",
                                tint = Color.White,
                            )
                        else
                            LoadingIndicator(small = true)
                    }
                }
            }
        }

    }
}


@Composable
fun GetImage(user: User, smallSize: Boolean = false) {
    var painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(

                if (user.photo == null) {
                    if (user.role == "teacher" && user.sex == "woman") R.drawable.teacherw
                    else if (user.role == "teacher" && user.sex == "man") R.drawable.teacherm
                    else if (user.role == "staff" && user.sex == "woman") R.drawable.staffw
                    else if (user.role == "staff" && user.sex == "man") R.drawable.staffm
                    else if (user.role == "admin" && user.sex == "woman") R.drawable.adminw
                    else R.drawable.adminm
                } else {
                    val host2 =
                        if (BuildConfig.DEV.toBoolean()) {
                            if (Build.HARDWARE == "ranchu") "http://10.0.2.2:8000/" else "http://192.168.1.5:8000/"
                        } else
                            BuildConfig.Host
                    host2 + user.photo
                }
            )
            .size(Size(width = 80, height = 80))
//                    .memoryCachePolicy(CachePolicy.DISABLED)
            .setHeader("User-Agent", "Mozilla/5.0")
            .transformations(
                CircleCropTransformation()
            )
            .build()
    )
    val painterState = painter.state

    if (painterState is AsyncImagePainter.State.Success) {
        Image(
            painter = painter,
            modifier = Modifier
                .size(if (smallSize) 40.dp else 50.dp),
            contentDescription = null
        )
    }
    var show by remember { mutableStateOf(false) }

    if (painterState is AsyncImagePainter.State.Error) {
        // Fallback to local images when loading url fail
        Image(
            painter = rememberAsyncImagePainter(
                if (user.role == "teacher" && user.sex == "woman") R.drawable.teacherw
                else if (user.role == "teacher" && user.sex == "man") R.drawable.teacherm
                else if (user.role == "staff" && user.sex == "woman") R.drawable.staffw
                else if (user.role == "staff" && user.sex == "man") R.drawable.staffm
                else if (user.role == "admin" && user.sex == "woman") R.drawable.adminw
                else R.drawable.adminm
            ),
            modifier = Modifier
                .size(50.dp),
            contentDescription = null
        )
//                LoadingAnimation()
    }
    if (painterState is AsyncImagePainter.State.Loading) {
        LoadingAnimation()
    }
}

// Handling the fab position when going back without the top arrow
@Composable
fun BackPressHandlerMsg(
    backPressedDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    navCtr: NavHostController? = null,
    sharedViewModel: SharedViewModel
) {

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                sharedViewModel.messageList.clear()
//                sharedViewModel.targetedMessageList.clear()
                sharedViewModel.defineSelectedContract(null)
                localMessageList.clear()
//                getMessages(sharedViewModel.user!!.id.toInt(), sharedViewModel)
                navCtr?.popBackStack()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}

// Actual message boxes
@Composable
private fun MessageBox(
    sharedViewModel: SharedViewModel,
    message: Message,
    onRemoveClicked: (msg: Message) -> Unit

) {

    val isIncoming =
        message.from_user_id != sharedViewModel.user!!.id.toString() // Equal true if it's incoming
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(Color.Transparent),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = if (isIncoming) Arrangement.Start else Arrangement.End

    ) {
        // Setting up the message time when the connected user send it
        if (!isIncoming) {
            Text(
                text = message.created_at.toString().substring(11, 16),
                fontSize = 10.sp,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.titleSmall.copy(color = Color.DarkGray),
                modifier = Modifier
                    .padding(horizontal = 13.dp)
                    .widthIn(max = Dp(LocalConfiguration.current.screenWidthDp * 0.75f))
            )
            Icon(
                painterResource(id = R.drawable.f),
                modifier = Modifier
                    .size(20.dp)
                    .offset(y = -7.dp, x = -5.dp)
                    .alpha(0.6f)
                    .clip(CircleShape)
                    .clickable(onClick = {
                        onRemoveClicked(message)
                        // When user delete their side messages, they get deleted from everyone's side
                        deleteThisMsg(message.id.toInt(),sharedViewModel, true)
                    }),
                contentDescription = "",
                tint = Color.Gray,
            )
        }

        Box(
            Modifier
//                .padding(10.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(if (isIncoming) Color.Transparent else Color(0xFF5491D8))
                .border(
                    0.6.dp, color = if (isIncoming) Color.DarkGray else Color.Transparent,
                    RoundedCornerShape(20.dp)
                )
        ) {
            Text(
                text = message.message.toString(),
                fontSize = 15.sp,
//                textAlign = TextAlign.,
                style = MaterialTheme.typography.titleSmall.copy(color = if (isIncoming) Color.Black else Color.White),
                modifier = Modifier
                    .padding(horizontal = 13.dp, vertical = 5.dp)
                    .widthIn(max = Dp(LocalConfiguration.current.screenWidthDp * 0.6f))
            )
        }
        if (isIncoming) {
            Icon(
                painterResource(id = R.drawable.f),
                modifier = Modifier
                    .size(20.dp)
                    .offset(y = -7.dp, x = 5.dp)
                    .alpha(0.6f)
                    .clip(CircleShape)
                    .clickable(onClick = {
                        onRemoveClicked(message)
                        deleteThisMsg(message.id.toInt(),sharedViewModel)
                    }),
                contentDescription = "",
                tint = Color.Gray,
            )
            Text(
                text = message.created_at.toString().substring(11, 16),
                fontSize = 12.sp,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.titleSmall.copy(color = Color.DarkGray),
                modifier = Modifier
                    .padding(horizontal = 13.dp)
            )

        }
    }
}


@Preview
@Composable
fun Preview4423() {
}