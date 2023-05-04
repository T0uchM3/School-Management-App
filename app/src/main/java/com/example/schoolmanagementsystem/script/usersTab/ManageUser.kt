package com.example.schoolmanagementsystem.script

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun ManageUser(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    val nameInput = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val cinInput = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val dnInput = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val emailInput = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val genderInput = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val roleInput = remember {
        mutableStateOf(TextFieldValue(""))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier
//                .fillMaxSize()
//                .backgrouEnd(Color.Blue)
        ) {

            OutlinedTextField(value = nameInput.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { nameInput.value = it },
                singleLine = true,
                label = { Text("name") })
            OutlinedTextField(value = cinInput.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { cinInput.value = it },
                singleLine = true,
                label = { Text("cin") })
            OutlinedTextField(value = dnInput.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { dnInput.value = it },
                singleLine = true,
                label = { Text("date_naiss") })
            OutlinedTextField(value = emailInput.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { emailInput.value = it },
                singleLine = true,
                label = { Text("email") })
            OutlinedTextField(value = genderInput.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { genderInput.value = it },
                singleLine = true,
                label = { Text("gender") })
            OutlinedTextField(value = roleInput.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { roleInput.value = it },
                singleLine = true,
                label = { Text("role") })
//        }
        }
        Button(
            onClick = {
                val user = User()
                user.name = nameInput.value.text
                user.cin = cinInput.value.text
                user.date_naiss = dnInput.value.text
                user.email = emailInput.value.text
                user.sex = genderInput.value.text
                user.role = roleInput.value.text
                addUserAPI(user)

                //resets all fields
                nameInput.value = TextFieldValue("")
                cinInput.value = TextFieldValue("")
                dnInput.value = TextFieldValue("")
                emailInput.value = TextFieldValue("")
                genderInput.value = TextFieldValue("")
                roleInput.value = TextFieldValue("")
            },
            modifier = Modifier
//                .align(Alignment.BottomCenter)
                .scale(1.2f),
            border = BorderStroke(1.dp, Color.Gray),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray)

        ) {
            Text(text = "Create")
        }
    }
}

@Preview
@Composable
fun Preview55() {
    ManageUser(TODO(), TODO())
}