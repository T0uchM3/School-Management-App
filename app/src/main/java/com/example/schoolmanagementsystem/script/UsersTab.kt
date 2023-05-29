package com.example.schoolmanagementsystem.ui.theme

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
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
            }
        )
    }


    LaunchedEffect(key1 = Unit) {
        sharedViewModel.defineFabVisible(true)
//        if (textFieldValue.isNotEmpty())
        clearSearch(sharedViewModel, focusManager)
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
    if (textFieldValue.isEmpty() && !visible) {

        localUserList.clear()
        sharedViewModel.userList.forEach { user -> localUserList.add(user) }
    }

//    return
    val users = remember { sharedViewModel.userList }
    Box(
        Modifier
            .fillMaxSize()
//            .background(Color.Blue)
    ) {
        /********************************************************
        setting up the search bar
         *********************************************************/
        BoxWithConstraints(
            Modifier
                .zIndex(1f)
//                .offset(y = 5.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 15.dp)
//                    .background(Color.Red)
                    .padding(bottom = 13.dp),
//                        .zIndex(1f),
//                horizontalArrangement = Arrangement.Center
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { /* Do something */ }),
                    value = textFieldValue,
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
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.search2),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(start = 3.dp, end = 6.dp)
                                        .scale(1.2f),
                                )
                                innerTextField()

                            }

                            if (textFieldValue.isEmpty()) {
                                Text(
                                    text = "Search users",
                                    color = Color.Gray,
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .padding(start = 35.dp)
                                        .padding(vertical = 3.dp)
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
                        colors =ButtonDefaults.buttonColors(Color.White),
                       modifier = Modifier
                            .clip(shape = RoundedCornerShape(25.dp))
//                            .background(MaterialTheme.colorScheme.surface)

                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 15.sp,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier
//                                .clickable {
////                                    clearSearch(sharedViewModel, focusManager)
//                                }
//                                .padding(horizontal = 15.dp)
                        )
                    }

            }
        }

        Column(
            Modifier
//                .background(Color.Red)
                .background(MaterialTheme.colorScheme.inverseSurface)
                .padding(top = 0.dp, bottom = 10.dp)
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
                            .padding(bottom = 40.dp, top = 30.dp)
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
        icon = rememberVectorPainter(Icons.Outlined.Edit),
        background = MaterialTheme.colorScheme.outline,
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
//        border = BorderStroke(
//            1.4.dp,
//            Color.Gray,
//        ),
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
        modifier = Modifier
            .clickable(
                onClick = {
                    sharedViewModel.defineSelectedUserId(user.id.toString())
                    sharedViewModel.defineUsersFocus(true)
//                    getUserContract(user.id.toInt(), sharedViewModel)
                    println("Contra swiped")
                    navCtr.navigate(Screen.Contract.route)
                })
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
//            .shadow(1.dp)
//            .background(Color.Red)
//            .padding(vertical = 16.dp, horizontal = 20.dp)

            .animateContentSize()

    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxHeight()
//                .width(10.dp)
//                .background(Color.Red)
//        ) {
////           Button(onClick = { /*TODO*/ },Modifier.background(Color.Transparent).fillMaxHeight()) {
//
//           }
        var size by remember { mutableStateOf(Size.Zero) }

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
                    .height(20.dp)
//                    .onGloballyPositioned { coo -> size = coo.size.toSize() }
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.inverseSurface)
            )
        }
//        }
        Row(
            Modifier
                .padding(top = 2.dp, start = 10.dp)
                .fillMaxHeight()
//                .size(40.dp)
                .background(color = Color.Transparent, shape = CircleShape),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    if (user.role == "teacher" && user.sex == "woman") R.drawable.teacherw
                    else if (user.role == "teacher" && user.sex == "man") R.drawable.teacherm
                    else if (user.role == "staff" && user.sex == "woman") R.drawable.staffw
                    else if (user.role == "staff" && user.sex == "man") R.drawable.staffm
                    else if (user.role == "admin" && user.sex == "woman") R.drawable.adminw
                    else if (user.role == "admin" && user.sex == "man") R.drawable.adminm
                    else
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = BuildConfig.Host + user.photo)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                            }).build()
                ),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )


        }

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
                    .height(20.dp)
                    .onGloballyPositioned { coo -> size = coo.size.toSize() }
                    .clip(shape)
                    .background(Color(0x80FFD7D7))
            )
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