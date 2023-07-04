package com.example.schoolmanagementsystem.ui.theme

import android.annotation.SuppressLint
import android.media.Image
import android.text.Layout
import android.view.WindowManager
import android.widget.Button
import android.widget.GridLayout
import android.widget.Space
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TopAppBar
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
//import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.key.Key.Companion.Window
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.schoolmanagementsystem.BuildConfig
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.HomeItems
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.script.navbar.RootNavGraph
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mrerror.singleRowCalendar.SingleRowCalendar
import java.lang.Math.abs
import java.util.Date


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen1(navCtr: NavHostController?, sharedViewModel: SharedViewModel?) {
    val name = navCtr?.previousBackStackEntry?.arguments?.getString("name")
    val user = sharedViewModel?.user
    // make sure the fab is hidden in this screen
    sharedViewModel?.defineFabVisible(false)
    sharedViewModel?.defineWaiting(true)
    //resetting tab focus
    sharedViewModel?.defineUsersFocus(false)
    sharedViewModel?.defineFABClicked(false)
    //making the app ui draw behind the top bar and under the system nav bar(if it exist)
    WindowCompat.setDecorFitsSystemWindows(sharedViewModel?.window!!, false)
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = true
    )

    BackPressHandler(navCtr = navCtr, sharedViewModel = sharedViewModel!!)
    LaunchedEffect(key1 = user) {
        sharedViewModel.defineFabVisible(false)
    }
    val id = navCtr?.previousBackStackEntry?.arguments?.getString("id")
    val role = navCtr?.previousBackStackEntry?.arguments?.getString("role")
    val swipeState = rememberSwipeableState(0)
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val minSwipeOffset by remember { mutableStateOf(300f) }
    Column(
//        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF3F7CC4), Color(0xFF7AB8FF))
                )
            )
            .padding(top = 0.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        offsetX += dragAmount.x

                    },
                    onDragEnd = {

                        when {
                            (offsetX < 0F && abs(offsetX) > minSwipeOffset) -> {
                                println(" SwipeDirection.Left")
                                navCtr?.navigate(Screen.Users.route)
                            }

                            (offsetX > 0 && abs(offsetX) > minSwipeOffset) -> {
                                println(" SwipeDirection.Right")
                            }

                            else -> null

                        }
                        offsetX = 0F
                    }
                )
            }

    ) {
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .weight(0.08f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pt12),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart)
                    .alpha(0.2f),
                contentScale = ContentScale.FillBounds
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 15.dp)
                    .background(
                        Color.Transparent
                    )
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.82f)
                        .padding(start = 0.dp)
                        .clickable(onClick = {
                            navCtr?.navigate(Screen.Profile.route)
                        })
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = user?.name.toString(),
                            color = Color(0xCCFFFFFF),
                            fontSize = 30.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Icon(
                            painterResource(id = R.drawable.j),
                            contentDescription = "",
                            tint =  Color(0xCCFFFFFF),
                            modifier = Modifier.size(40.dp).padding(start = 10.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f)
                    ) {
                        Text(
                            text = "CIN: ${sharedViewModel?.user?.cin} | ${sharedViewModel?.user?.role}",
                            color = Color(0xCCFFFFFF),
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .padding(10.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .weight(0.18f)
//                        .padding(6.dp)
                )
//
                {
                    Icon(
                        painterResource(id = R.drawable.q),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier.scale(0.7f)
                    )
                }
            }
        }
        Divider(
            Modifier
                .padding(horizontal = 15.dp)
                .background(Color(0xFF79B6FD)))

        var day = remember { mutableStateOf(Date()) }
        SingleRowCalendar(
//            Modifier.weight(0.3f),
            selectedDayBackgroundColor = Color(0xFFFFFFFF),
            selectedDayTextColor = MaterialTheme.colorScheme.inverseSurface,
            dayNumTextColor = Color(0xCCFFFFFF),
            dayTextColor = Color(0xCCFFFFFF),
            iconsTintColor = Color(0xCCFFFFFF),
            headTextColor = Color(0xCCFFFFFF),
            headTextStyle = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
            nextDrawableRes = R.drawable.round_arrow_forward,
            prevDrawableRes = R.drawable.round_arrow_back,
            onSelectedDayChange = {
                day.value = it
            })


        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
//                    .weight(9f)
//                .paint(painterResource(id = R.drawable.pattern))
                .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(0.5f)
        ) {
            val listOfItemForStudent = listOf(
                HomeItems.note,
                HomeItems.subscription,
                HomeItems.attendance,
                HomeItems.message,
                HomeItems.setting,
                HomeItems.student,
//                HomeItems.contract
            )
            val listOfItem = listOf(
                HomeItems.note,
                HomeItems.subscription,
                HomeItems.attendance,
                HomeItems.message,
                HomeItems.setting,
                HomeItems.group,
                HomeItems.student,
//                HomeItems.contract
            )
            val listOfItemForStaff = listOf(
                HomeItems.note,
                HomeItems.subscription,
                HomeItems.attendance,
                HomeItems.message,
                HomeItems.setting,
//                HomeItems.contract
            )
            LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
                items(count = if (sharedViewModel.user?.role == "student") listOfItemForStudent.size else if(sharedViewModel.user?.role == "staff") listOfItemForStaff.size else  listOfItem.size) { item ->

                    Button(
                        onClick = {
                            if (listOfItem[item].title == "Settings")
                                navCtr?.navigate(Screen.Settings.route)
                            if (listOfItem[item].title == "Messages")
                                navCtr?.navigate(Screen.Inbox.route)
                            if (listOfItem[item].title == "Students")
                                navCtr?.navigate(Screen.Students.route)
                            if (listOfItem[item].title == "Groups")
                                navCtr?.navigate(Screen.Groups.route)
                        },
                        colors = androidx.compose.material.ButtonDefaults.buttonColors(
                            contentColor = Color.Transparent, backgroundColor = Color.Transparent
                        ),
                        elevation = androidx.compose.material.ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),

                        content = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Icon(
                                    painterResource(id = listOfItem[item].icon),
                                    contentDescription = "",
                                    tint = listOfItem[item].color,
                                    modifier = Modifier.scale(0.5f)
                                )
                                Text(
                                    text = listOfItem[item].title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 2
                                )
                            }
                        })
                }
            })
        }

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

    }


}

@Composable
fun BackPressHandler(
    sharedViewModel: SharedViewModel,
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    navCtr: NavHostController? = null,
) {

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                sharedViewModel?.defineFABClicked(null)
                navCtr?.navigate(Screen.Login.route)
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

@Preview
@Composable
fun Preview1() {
    HomeScreen1(null, null)
}