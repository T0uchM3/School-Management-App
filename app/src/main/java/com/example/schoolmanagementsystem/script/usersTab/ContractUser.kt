package com.example.schoolmanagementsystem.script

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController

@Composable
fun ContractUser(
    navCtr: NavHostController,
    sharedViewModel: SharedViewModel,
) {
    val showDialog = remember { mutableStateOf(false) }
    val text = remember {
        mutableStateOf("ContractUser")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 50.dp, horizontal = 50.dp)
    ) {
        Text(
            text = text.value.toString(),
            color = Color.Black,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
        )
        Button(onClick = {
            showDialog.value = true
        }) {
            Text(text = "BUT")
        }
    }
    if (showDialog.value)
        CustomDialog(value = "", setShowDialog = {
            showDialog.value = it
        }) {
        }
}


@Composable
fun CustomDialog(
    value: String,
    setShowDialog: (Boolean) -> Unit,
    setValue: (String) -> Unit
) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp), color = Color.White
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.9f)
                    .background(Color.White)
                    .padding(vertical = 50.dp, horizontal = 50.dp)
            ) {
                Text(
                    text = "ddddd",
                    color = Color.Black,
                    fontSize = 30.sp,
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .clickable {navController?.navigate(Destination.LoginScreen.route)},
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.SansSerif,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Preview
@Composable
fun ContractUserPreview() {
    ManageUser(TODO(), TODO())
}