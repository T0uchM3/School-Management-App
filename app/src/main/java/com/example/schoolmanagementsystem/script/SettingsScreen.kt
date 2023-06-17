package com.example.schoolmanagementsystem.script

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@SuppressLint("RememberReturnType")
@Composable
fun SettingsScreen(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    sharedViewModel.defineFABClicked(null)
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color(0xfff2f2f2),
        darkIcons = true
    )
    systemUiController.setSystemBarsColor(
        color = Color.Transparent, darkIcons = true
    )
    var text = remember { mutableStateOf("Hello, World!") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 5.dp, start = 20.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            IconButton(onClick = {
                sharedViewModel.defineSelectedContract(null)
                navCtr.popBackStack()
            }) {
                Icon(
                    Icons.TwoTone.ArrowBack,
                    "",
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .scale(1.3f)
                        .padding(end = 20.dp)
                )
            }
            Text(
                text = "Settings",
                color = Color.DarkGray,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.weight(1f))

        }
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(start = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Divider(
                Modifier
//                    .padding(horizontal = 15.dp)
                    .background(Color(0xFF79B6FD))
            )
            Text(
                text = "Set Language",
                color = Color.Gray,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.aa),
                    modifier = Modifier.size(30.dp),
                    contentDescription = "",
                    tint = Color.LightGray,
                )
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = "Set Language",
                    color = Color.DarkGray,
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )

            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.access),
                    modifier = Modifier.size(30.dp),
                    contentDescription = "",
                    tint = Color.LightGray,
                )
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = "Allow to Access",
                    color = Color.DarkGray,
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )

            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.q),
                    modifier = Modifier.size(30.dp),
                    contentDescription = "",
                    tint = Color.LightGray,
                )
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = "Notifications",
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )

            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.k),
                    contentDescription = "",
                    modifier = Modifier.size(30.dp),
                    tint = Color.LightGray,
                )
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = "Clear Cache",
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )

            }
            Divider(
                Modifier
//                    .padding(horizontal = 15.dp)
                    .background(Color(0xFF79B6FD))
            )
            Text(
                text = "Account",
                color = Color.Gray,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {  navCtr?.navigate(Screen.Login.route)})
                    .padding(vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.i),
                    modifier = Modifier.size(30.dp),
                    contentDescription = "",
                    tint = Color.LightGray,
                )
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = "Logout",
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )

            }
//            Divider(
//                Modifier
////                    .padding(horizontal = 15.dp)
//                    .background(Color(0xFF79B6FD))
//            )
        }
    }
}
