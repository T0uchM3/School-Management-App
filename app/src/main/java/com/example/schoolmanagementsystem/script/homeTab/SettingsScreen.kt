package com.example.schoolmanagementsystem.script.homeTab

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.storage.StorageManager
import android.provider.Settings
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
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.BuildConfig
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.script.StoreData
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale


@SuppressLint("RememberReturnType")
@Composable
fun SettingsScreen(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    sharedViewModel.defineFABClicked(null)
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current
    val resources = context.resources
    val store = StoreData(context)

    var locale = Locale.getDefault()
    val configuration = Configuration(resources.configuration)
    systemUiController.setSystemBarsColor(
        color = Color(0xfff2f2f2),
        darkIcons = true
    )
    // make sure the fab is hidden in this screen
    sharedViewModel.defineFabVisible(false)
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
//            IconButton(onClick = {
//                sharedViewModel.defineSelectedContract(null)
//                navCtr.popBackStack()
//            }) {
//                Icon(
//                    Icons.TwoTone.ArrowBack,
//                    "",
//                    tint = Color.DarkGray,
//                    modifier = Modifier
//                        .scale(1.3f)
//                        .padding(end = 20.dp)
//                )
//            }
            Text(
                text = stringResource(R.string.settings),
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
                .padding(start = 10.dp, top = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Divider(
                Modifier
//                    .padding(horizontal = 15.dp)
                    .background(Color(0xFF79B6FD))
            )
            Text(
                text = stringResource(R.string.general_settings),
                color = Color.Gray,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
            )
            val isFrench = store.getFromDataStore.collectAsState(initial = "").value == "fr"

            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {

                        CoroutineScope(Dispatchers.IO).launch {
                            if (isFrench)
                                store.saveToDataStore(newLang = "en")
                            else
                                store.saveToDataStore(newLang = "fr")

                        }
                        val locale = Locale("fr")
                        configuration.setLocale(locale)
                        resources.updateConfiguration(configuration, resources.displayMetrics)
                        context
                            .findActivity()
                            ?.recreate()
                    }
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
                    text = stringResource(R.string.set_language),
                    color = Color.DarkGray,
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )

                Text(
                    modifier = Modifier.padding(start = 50.dp),
                    text = "FR",
//                    color =  if (locale == Locale.FRENCH) Color.DarkGray else Color.LightGray,
                    color = if (isFrench) Color.DarkGray else Color.LightGray,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = "ENG",
                    color = if (!isFrench) Color.DarkGray else Color.LightGray,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            var context = LocalContext.current

            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {

                        val i = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                        )
                        context.startActivity(i)


                    }
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
                    text = stringResource(R.string.allow_to_access),
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
                    .clickable {
                        if (sharedViewModel.notifEnabled)
                            sharedViewModel.defineNotifEnabled(false)
                        else
                            sharedViewModel.defineNotifEnabled(true)
                    }
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
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = if (sharedViewModel.notifEnabled) "Enabled" else "Disabled",
                    color = if (!isFrench) Color.DarkGray else Color.LightGray,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            val cacheDir = context.cacheDir
            for (file: File in context?.cacheDir?.listFiles()!!) {
                file.delete()
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        deleteCache(context = context)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            StorageManager.ACTION_CLEAR_APP_CACHE
                        }
                    }


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
                    text = stringResource(R.string.clear_cache),
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
                text = stringResource(R.string.account),
                color = Color.Gray,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            store.saveNewLoginState(newState = "loggedout")
                        }
                        navCtr?.navigate(Screen.Login.route) })
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
                    text = stringResource(R.string.logout),
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )

            }
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun deleteCache(context: Context) {
    try {
        val dir = context.cacheDir
        if (dir != null && dir.isDirectory) {
            deleteDir(dir)
        }
    } catch (e: Exception) {
    }
}

fun deleteDir(dir: File?): Boolean {
    if (dir != null && dir.isDirectory) {
        val children = dir.list()
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
        return dir.delete()
    } else if (dir != null && dir.isFile) {
        return dir.delete()
    }
    return false
}