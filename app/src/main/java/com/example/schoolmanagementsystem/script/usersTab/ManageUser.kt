package com.example.schoolmanagementsystem.script

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.ui.theme.clearSearch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

var selectedUser: User? = null
var nameInput: MutableState<TextFieldValue>? = null
var cinInput: MutableState<TextFieldValue>? = null
var dnInput: MutableState<TextFieldValue>? = null
var emailInput: MutableState<TextFieldValue>? = null
var genderInput: MutableState<TextFieldValue>? = null
var roleInput: MutableState<TextFieldValue>? = null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUser(
    navCtr: NavHostController? = null,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope? = null,
    state: SheetState? = null,
    focusManager: FocusManager? = null
) {
    //searching through the users list for user that got selected in the previous (userTab)
//     selectedUser: User? = null
    for (user in sharedViewModel.userList) {
        if (user.id.toString() == sharedViewModel.selectedUserId)
            selectedUser = user
    }
    println(sharedViewModel.isNewUser.toString() + "  USER ID!::: " + selectedUser?.id)

    nameInput = remember {
        mutableStateOf(TextFieldValue(selectedUser?.name.toString()))
    }
    cinInput = remember {
        mutableStateOf(TextFieldValue(selectedUser?.cin.toString()))
    }
    dnInput = remember {
        mutableStateOf(TextFieldValue(selectedUser?.date_naiss.toString()))
    }
    emailInput = remember {
        mutableStateOf(TextFieldValue(selectedUser?.email.toString()))
    }
    genderInput = remember {
        mutableStateOf(TextFieldValue(selectedUser?.sex.toString()))
    }
    roleInput = remember {
        mutableStateOf(TextFieldValue(selectedUser?.role.toString()))
    }

    if (sharedViewModel.isNewUser) {
        //this will get recomposed, so better to trigger it once per fap pres
        sharedViewModel.defineIsNewUser(false)
        resetAllFields()
    }
    LaunchedEffect(key1 = Unit) {
        sharedViewModel.defineFABClicked(true)

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color.White)
    ) {

        Column(
            modifier = Modifier
//                .fillMaxSize()
//                .backgrouEnd(Color.Blue)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {
                        sharedViewModel.defineFABClicked(false)
                        scope?.launch { state?.hide() }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(text = "Cancel", fontSize = 17.sp)
                }
                Spacer(Modifier.weight(1f))
                Text(text = "New User", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        val user = User()
                        user.name = nameInput!!.value.text
                        user.cin = cinInput!!.value.text
                        user.date_naiss = dnInput!!.value.text
                        user.email = emailInput!!.value.text
                        user.sex = genderInput!!.value.text
                        user.role = roleInput!!.value.text
                        //clearing lists to avoid lazycol parsing error
                        sharedViewModel.userList.clear()
                        sharedViewModel.contractList.clear()

                        if (sharedViewModel.isNewUser) {
                            clearSearch(sharedViewModel, focusManager!!)
                            addUserAPI(user, sharedViewModel, true)
                            sharedViewModel.defineFABClicked(false)
                            scope?.launch { state?.hide() }
                        } else {
                            clearSearch(sharedViewModel, focusManager!!)
                            updateUser(
                                sharedViewModel.selectedUserId.toInt(),
                                user,
                                sharedViewModel,
                                true
                            )
                            sharedViewModel.defineFABClicked(false)
                            scope?.launch { state?.hide() }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    if (sharedViewModel.isNewUser)
                        Text(text = "Create", fontSize = 17.sp, color = Color(0xff386A1F))
                    else
                        Text(text = "Update", fontSize = 17.sp, color = Color(0xff386A1F))
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                OutlinedTextField(
                    value = nameInput!!.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { nameInput!!.value = it },
                    singleLine = true,
                    label = { Text("name") }, modifier = Modifier.fillMaxWidth(),
                    colors = sharedViewModel.tFColors(),

                    )
                OutlinedTextField(
                    value = cinInput!!.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { cinInput!!.value = it },
                    singleLine = true,
                    label = { Text("cin") }, modifier = Modifier.fillMaxWidth(),
                    colors = sharedViewModel.tFColors(),
                )
                OutlinedTextField(
                    value = dnInput!!.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { dnInput!!.value = it },
                    singleLine = true,
                    label = { Text("date_naiss") }, modifier = Modifier.fillMaxWidth(),
                    colors = sharedViewModel.tFColors(),
                )
                OutlinedTextField(
                    value = emailInput!!.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { emailInput!!.value = it },
                    singleLine = true,
                    label = { Text("email") }, modifier = Modifier.fillMaxWidth(),
                    colors = sharedViewModel.tFColors(),
                )
                OutlinedTextField(
                    value = genderInput!!.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { genderInput!!.value = it },
                    singleLine = true,
                    label = { Text("gender") }, modifier = Modifier.fillMaxWidth(),
                    colors = sharedViewModel.tFColors(),
                )
                OutlinedTextField(
                    value = roleInput!!.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { roleInput!!.value = it },
                    singleLine = true,
                    label = { Text("role") }, modifier = Modifier.fillMaxWidth(),
                    colors = sharedViewModel.tFColors(),
                )
            }
        }
    }

}

fun resetAllFields() {
    //resets all fields
    nameInput?.value = TextFieldValue("")
    cinInput?.value = TextFieldValue("")
    dnInput?.value = TextFieldValue("")
    emailInput?.value = TextFieldValue("")
    genderInput?.value = TextFieldValue("")
    roleInput?.value = TextFieldValue("")
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Preview55() {
//    ManageUser(TODO(), TODO(), focusManger = focusManager)
}