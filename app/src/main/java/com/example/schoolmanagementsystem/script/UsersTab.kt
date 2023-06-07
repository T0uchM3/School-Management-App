package com.example.schoolmanagementsystem.ui.theme

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Global.getString
import android.widget.ImageView
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Woman
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.imageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.schoolmanagementsystem.BuildConfig
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.LoadingIndicator
import com.example.schoolmanagementsystem.script.ManageUser
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.script.User
import com.example.schoolmanagementsystem.script.deleteUserAPI
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.example.schoolmanagementsystem.script.usersAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import okhttp3.internal.wait
import kotlin.concurrent.fixedRateTimer

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
)
@Composable
fun UsersTab(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    val minSwipeOffset by remember { mutableStateOf(300f) }
    var offsetX by remember { mutableStateOf(0f) }
//    val localUserList = remember { SnapshotStateList<User>() }
    val focusManager = LocalFocusManager.current
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


    LaunchedEffect(key1 = Unit) {
        sharedViewModel.defineFabVisible(true)
        clearSearch(sharedViewModel, focusManager)
        //setting up fab in user tab
        sharedViewModel.defineFabClick {
            sharedViewModel.defineIsNewUser(true)
            scope?.launch {
                sheetState?.show()
//                sharedViewModel.defineFABClicked(true)

            }
        }
        sharedViewModel.userList.clear()
        sharedViewModel.contractList.clear()
        sharedViewModel.paymentList.clear()
        usersAPI(sharedViewModel = sharedViewModel)
    }
    if (textFieldValue.isEmpty() && !visible) {

        localUserList.clear()
        sharedViewModel.userList.forEach { user -> localUserList.add(user) }
    }

//    return
    val users = remember { sharedViewModel.userList }
    Box(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF4884C9), Color(0xFF63A4EE))
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
                                    text = "Search users",
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

//                            if (isInitialFocus) {
                            if (!focusState.hasFocus) {
                                isInitialFocus = false
                                return@onFocusChanged
                            }
                            sharedViewModel.defineFabVisible(false)
                            localUserList.clear()
                            visible = true
                        }
                        .focusRequester(focusRequester)
                        .weight(1f, true)
                        .animateContentSize()
                )
                if (visible)
                    androidx.compose.material3.Button(
                        onClick = { clearSearch(sharedViewModel, focusManager) },
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

                if (localUserList.isEmpty() && !visible)
                    Box(
                        Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
                    ) {
                        LoadingIndicator()
                    }
                else
                    LazyColumn(
                        state = rememberLazyListState(),
                        modifier = Modifier
                            .padding(bottom = 45.dp, top = 25.dp)
//                            .background(Color.Red)
                            .padding(horizontal = 7.dp)
                    ) {
                        items(localUserList, key = { item -> item.id }) { user ->
                            Box(modifier = Modifier.animateItemPlacement()) {
                                SwipeableBoxPreview(
                                    navCtr,
                                    Modifier.padding(),
                                    sharedViewModel,
                                    user,
                                    onRemoveClicked = {
                                        sharedViewModel.userList.remove(user)
                                    },
                                    focusManager
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    }
            }
        }
    }

}

fun clearSearch(sharedViewModel: SharedViewModel, focusManager: FocusManager) {
    localUserList.clear()
    textFieldValue = ""
    visible = false
    isInitialFocus = true
    focusManager.clearFocus()
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
    focusManager: FocusManager
) {
    var isSnoozed by rememberSaveable { mutableStateOf(false) }
    var isArchived by rememberSaveable { mutableStateOf(false) }

    var isActive by rememberSaveable { mutableStateOf(false) }
    val editUser = SwipeAction(
        icon = {
            Icon(
                painterResource(id = R.drawable.j),
                contentDescription = "",
                tint = Color(0xCCF1F1F1),
                modifier = Modifier.scale(0.6f)
            )
//            painterResource(id = R.drawable.j),

        },
//                rememberVectorPainter(Icons.Outlined.Edit),
        background = MaterialTheme.colorScheme.onSurface,
        onSwipe = {
//            textFieldvalue=""
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
                isSnoozed = isSnoozed,
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
    /*
                      this come above the card
     */
    Row(
        modifier = Modifier
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
                                    if (Build.HARDWARE == "ranchu") "http://10.0.2.2:8000/" else "http://192.168.1.4:8000/"
                                } else
                                    BuildConfig.Host
                            host2 + user.photo
                        }
                    )
                    .size(coil.size.Size(width = 80, height = 80))
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
                        .size(60.dp),
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
                        .size(60.dp),
                    contentDescription = null
                )
//                LoadingAnimation()
            }
            if (painterState is AsyncImagePainter.State.Loading) {
                LoadingAnimation()
            }

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

            if (isSnoozed) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .background(
                            Color.SeaBuckthorn.copy(alpha = 0.4f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    text = "Snoozed until tomorrow",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        //setting the right vertical red marker
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

val Color.Companion.SeaBuckthorn get() = Color(0xFFF9A825)
val Color.Companion.Fern get() = Color(0xFF66BB6A)
val Color.Companion.Perfume get() = Color(0xFFD0BCFF)


@Preview
@Composable
fun Preview22() {
    UsersTab(TODO(), TODO())
}