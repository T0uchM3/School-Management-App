package com.example.schoolmanagementsystem.script

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.example.schoolmanagementsystem.ui.theme.clearSearch
import com.example.schoolmanagementsystem.ui.theme.clearSearchStudent
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import java.io.File


@SuppressLint("RememberReturnType")
@Composable
fun ProfileScreen(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    sharedViewModel.defineUsersFocus(false)
    sharedViewModel.defineFabVisible(false)

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color(0xFFFCFCF5),
        darkIcons = true
    )
    val minSwipeOffset by remember { mutableStateOf(300f) }
    var offsetX by remember { mutableStateOf(0f) }

    nameInput = remember {
        mutableStateOf(TextFieldValue(sharedViewModel.user?.name.toString()))
    }
    cinInput = remember {
        mutableStateOf(TextFieldValue(sharedViewModel.user?.cin.toString()))
    }
    dnInput = remember {
        mutableStateOf(TextFieldValue(sharedViewModel.user?.date_naiss.toString()))
    }
    emailInput = remember {
        mutableStateOf(TextFieldValue(sharedViewModel.user?.email.toString()))
    }
    passwordInput = remember {
        mutableStateOf(TextFieldValue(sharedViewModel.user?.password.toString()))
    }
    positionInput = remember {
        mutableStateOf(TextFieldValue(if (sharedViewModel.user?.poste == null) "" else sharedViewModel.user?.poste.toString()))
    }
    ribInput = remember {
        mutableStateOf(TextFieldValue(if (sharedViewModel.user?.rib == null) "" else sharedViewModel.user?.rib.toString()))
    }
    phoneInput = remember {
        mutableStateOf(TextFieldValue(if (sharedViewModel.user?.tel == null) "" else sharedViewModel.user?.tel.toString()))
    }
    addressInput = remember {
        mutableStateOf(TextFieldValue(if (sharedViewModel.user?.adresse == null) "" else sharedViewModel.user?.adresse.toString()))
    }
    selectedSex = remember {
        mutableStateOf(-1)
    }
    selectedRole = remember {
        mutableStateOf(-1)
    }
    userPhoto = remember {
        mutableStateOf("")
    }
    selectedPhoto = remember {
        mutableStateOf(File(""))
    }
    photoIsSelected = remember {
        mutableStateOf(false)
    }
    // Editing user
    if (sharedViewModel.user?.date_naiss != null) {
        // Progressive app?!
        selectedSex = remember {
            mutableStateOf(if (sharedViewModel.user?.sex == "man") 1 else if (sharedViewModel.user?.sex == "woman") 2 else -1)
        }
        selectedRole = remember {
            mutableStateOf(if (sharedViewModel.user?.role == "staff") 1 else if (sharedViewModel.user?.role == "teacher") 2 else 3)
        }
        //format 2023-05-27
        stringToDate(sharedViewModel.user?.date_naiss!!)

        bank = remember {
            mutableStateOf(
                if (sharedViewModel.user?.bank == null) 0 else bankNames.indexOf(sharedViewModel.user?.bank)
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        // For status bar shadow simulation
        sharedViewModel.defineFABClicked(true)
//        if (sharedViewModel.isNewUser) {
//            //this will get recomposed, so better to trigger it once per fap pres
////            sharedViewModel.defineIsNewUser(false)
//            resetAllFields()
//        }
    }




    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
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
            androidx.compose.material.Text(
                text = "Profile",
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
                .fillMaxWidth()
                .padding(top = 0.dp)
                .verticalScroll(rememberScrollState())

//                .padding(horizontal = 20.dp)
        ) {

            //user's photo
            ImageSelection(LocalContext.current, userPhoto!!)


            TextField(sharedViewModel = sharedViewModel, nameInput!!, "name")
            TextField(sharedViewModel = sharedViewModel, cinInput!!, "cin")
            TextField(sharedViewModel = sharedViewModel, emailInput!!, "email")
            TextField(sharedViewModel = sharedViewModel, passwordInput!!, "password")


            if (sharedViewModel.user?.role == "admin" || sharedViewModel.user?.role == "teacher" ||
                sharedViewModel.user?.role == "staff"
            ) {

                if(sharedViewModel.user!!.role!="staff"){
                    Text(
                        text = "Role",
                        modifier = Modifier.padding(
                            top = 5.dp,
                            start = 15.dp,
                        ),
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
                    )
                    SegmentedButtonsRole(selectedRole!!, "Staff", "Teacher", "Admin")

                }
                TextField(sharedViewModel = sharedViewModel, positionInput!!, "Job")
            }


            Text(
                text = "Date of birth",
                modifier = Modifier.padding(
                    top = 5.dp,
                    start = 15.dp,
                ),
                style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
            )
            DatePickerUI(sharedViewModel, year, month, day)

            TextField(sharedViewModel = sharedViewModel, addressInput!!, "address")

            Text(
                text = "Gender",
                modifier = Modifier.padding(top = 5.dp, start = 15.dp),
                style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
            )
            SegmentedButtons(selectedSex!!)
            TextField(sharedViewModel = sharedViewModel, ribInput!!, "rib")
            TextField(sharedViewModel = sharedViewModel, phoneInput!!, "phone")
            var student = Student()
            if (sharedViewModel.user?.role == "student") {
                student =
                    sharedViewModel.studentList.filter { it.user?.id == sharedViewModel.user!!.id }[0]

                Text(
                    text = "Group",
                    modifier = Modifier.padding(top = 5.dp, start = 15.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
                )
                val groupName = sharedViewModel.groupList.map { it.name }
                GroupScroll(
                    onGroupChosen = { group.value = groupName.indexOf(it) },
                    isNewStudent = false, sharedViewModel
                )
                Text(
                    text = "Parent: " + student.parent,
                    modifier = Modifier.padding(top = 5.dp, start = 15.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.DarkGray)
                )
                Text(
                    text = "Remark: " + if(student.remarque == null) "None" else student.remarque,
                    modifier = Modifier.padding(top = 5.dp, start = 15.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.DarkGray)
                )
            }

            if (sharedViewModel.user?.role == "admin" || sharedViewModel.user?.role == "teacher" ||
                sharedViewModel.user?.role == "staff"
            ) {
                Text(
                    text = "Bank",
                    modifier = Modifier.padding(top = 5.dp, start = 15.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
                )
                BanksScroll(
                    onBankChosen = { bank.value = bankNames.indexOf(it) },
                    isNewUser = false
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

                Button(
                    onClick = {

                        if (sharedViewModel.user?.role == "student") {
                            val groupName = sharedViewModel.groupList.map { it.name }

                            val _student = Student()
                            _student.name = nameInput!!.value.text
                            _student.cin = cinInput!!.value.text
                            _student.date_naiss = "${year.value}-${month.value + 1}-${day.value}"
                            _student.email = emailInput!!.value.text
                            _student.sex =
                                if (selectedSex!!.value == 1) "man" else if (selectedSex!!.value == 2) "woman" else null
                            _student.tel = phoneInput!!.value.text
                            _student.adresse = addressInput!!.value.text
                            _student.password = passwordInput!!.value.text
                            var groupIndex =
                                if (sharedViewModel.groupList.isEmpty()) "" else groupName.elementAt(
                                    group.value
                                )
                            _student.groupe_id =
                                sharedViewModel.groupList.find { it.name == groupIndex }?.id.toString()
                            _student.remarque = if(student.remarque == null) "" else student.remarque
                            _student.parent = student.parent

                            // Updating student
                            _student.id = student!!.id
                            editStudent(_student, sharedViewModel, selectedPhoto, true)
                            sharedViewModel.defineFABClicked(false)
                            return@Button
                        }


                        val user = User()

                        user.name = nameInput!!.value.text
                        user.cin = cinInput!!.value.text
                        user.date_naiss = "${year.value}-${month.value + 1}-${day.value}"
                        user.email = emailInput!!.value.text
                        user.sex =
                            if (selectedSex!!.value == 1) "man" else if (selectedSex!!.value == 2) "woman" else null
                        user.bank = bankNames.elementAt(bank.value)
                        user.tel = phoneInput!!.value.text
                        user.rib = ribInput!!.value.text
                        user.poste = positionInput!!.value.text
                        user.adresse = addressInput!!.value.text
                        user.password = passwordInput!!.value.text
                        println("password   " + user.password)
                        user.role =
                            if (selectedRole!!.value == 1) "staff" else if (selectedRole!!.value == 2) "teacher" else "admin"
                        //clearing lists to avoid lazycol parsing error
//                sharedViewModel.userList.clear()
//                sharedViewModel.contractList.clear()

                        updateUser(
                            sharedViewModel.user!!.id.toInt(),
                            user,
                            selectedPhoto,
                            sharedViewModel,
                            true
                        )
//                sharedViewModel.defineFABClicked(false)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff4884C9),
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.DarkGray,
                        contentColor = Color(0xfff2f2f2)
                    ),
                    enabled =
                    if (sharedViewModel.user?.role == "admin" || sharedViewModel.user?.role == "teacher" ||
                        sharedViewModel.user?.role == "staff"
                    ) {
                        !(selectedRole!!.value == -1 || selectedSex!!.value == -1 || nameInput!!.value.text.isEmpty() ||
                                emailInput!!.value.text.isEmpty() || bankNames.elementAt(bank.value) == "Choose a bank")
                    } else {
                        true
//                        !(selectedRole!!.value == -1 || selectedSex!!.value == -1 || nameInput!!.value.text.isEmpty() ||
//                                emailInput!!.value.text.isEmpty() || bankNames.elementAt(bank.value) == "Choose a bank")
                    }

                ) {
                    Text(
                        text = "     Update     ",
                        modifier = Modifier.padding(vertical = 2.dp),
                        fontSize = 20.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

        }
    }
}

@Preview
@Composable
fun Preview44() {
//    HomeScreen4(TODO(), TODO())
}