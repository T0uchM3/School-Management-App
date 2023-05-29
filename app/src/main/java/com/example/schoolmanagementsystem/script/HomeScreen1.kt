package com.example.schoolmanagementsystem.ui.theme

import android.annotation.SuppressLint
import android.media.Image
import android.text.Layout
import android.view.WindowManager
import android.widget.Button
import android.widget.GridLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
//import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.key.Key.Companion.Window
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import java.lang.Math.abs


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen1(navCtr: NavHostController?, sharedViewModel: SharedViewModel?) {
    val name = navCtr?.previousBackStackEntry?.arguments?.getString("name")
    val user = sharedViewModel?.user

    //resetting tab focus
    sharedViewModel?.defineUsersFocus(false)
    //making the app ui draw behind the top bar and under the system nav bar(if it exist)
    val systemUiController = rememberSystemUiController()
    WindowCompat.setDecorFitsSystemWindows(sharedViewModel?.window!!, false)
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = true
    )

    BackPressHandler(navCtr = navCtr)
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
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF4884C9), Color(0xFF63A4EE))
                )
            )
            .padding(top = 25.dp)
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

                .weight(0.15f)
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
                        .weight(8f)
                        .padding(start = 0.dp)
                ) {
                    Text(
                        text = user?.name.toString(),
                        color = Color(0xCCFFFFFF),
                        fontSize = 40.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f)
                    ) {
                        Text(
                            text = "CIN: ${sharedViewModel?.user?.cin} | ${sharedViewModel?.user?.role}",
                            color = Color(0xCCFFFFFF),
                            fontSize = 20.sp,
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
                        .weight(2f)
//                        .padding(6.dp)
                )
//
                {
                    Icon(
                        painterResource(id = R.drawable.q),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier.scale(0.8f)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
//                    .weight(9f)
//                .paint(painterResource(id = R.drawable.pattern))
                .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(Color(0xFFF7FBFE))
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(0.85f)
        ) {
            val listOfItem = listOf(
                HomeItems.note,
                HomeItems.subscription,
                HomeItems.attendance,
                HomeItems.message,
                HomeItems.setting
            )
            LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
//            LazyVerticalGrid(columns = object :GridCells{
//                override fun Density.calculateCrossAxisCellSizes(
//                    availableSize: Int,
//                    spacing: Int
//                ): List<Int> {
//                    val firstColumn = (availableSize - spacing) * 2 / 3
//                    val secondColumn = availableSize - spacing - firstColumn
//                    return listOf(firstColumn, secondColumn)
//                }
//            }, content = {
                items(listOfItem.size) { item ->

                    Button(
                        onClick = { /*TODO*/ },
                        colors = androidx.compose.material.ButtonDefaults.buttonColors(
                            contentColor = Color.Transparent, backgroundColor = Color.Transparent
                        ),
                        elevation = androidx.compose.material.ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),
//                        colors = ButtonDefaults.buttonColors(Color.Transparent),
//                        modifier = Modifier.width(200.dp)

//                    )
                        content = {
//                    {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
//                            modifier = Modifier.width(200.dp)
                            ) {
                                Icon(
                                    painterResource(id = listOfItem[item].icon),
                                    contentDescription = "",
                                    tint = listOfItem[item].color,
                                    modifier = Modifier.scale(0.6f)
                                )
                                Text(
                                    text = listOfItem[item].title,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 2
                                )
                            }
//                    }
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

fun verticalGradient(
    vararg colorStops: Pair<Float, Color>,
    startY: Float = 0.0f,
    endY: Float = Float.POSITIVE_INFINITY,
    tileMode: TileMode = TileMode.Clamp
) {
}

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    navCtr: NavHostController? = null,
) {

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
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