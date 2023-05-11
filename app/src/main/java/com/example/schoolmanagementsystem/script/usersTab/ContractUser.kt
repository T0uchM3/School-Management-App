package com.example.schoolmanagementsystem.script

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Block
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.DisabledByDefault
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Pages
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.example.schoolmanagementsystem.ui.theme.Fern
import com.example.schoolmanagementsystem.ui.theme.Perfume
import com.example.schoolmanagementsystem.ui.theme.SeaBuckthorn
import com.example.schoolmanagementsystem.ui.theme.userId
import com.example.schoolmanagementsystem.ui.theme.userName
import com.example.schoolmanagementsystem.ui.theme.userRole
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


var sDate = ""
var eDate = ""
var period = ""
var salary = ""
var valid = true

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContractUser(
    navCtr: NavHostController,
    sharedViewModel: SharedViewModel,
) {
    val showDialog = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        sharedViewModel.contractList.clear()
    }
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .background(Color.Red)
                .fillMaxWidth()
        ) {
            Button(onClick = {
                showDialog.value = true
            }) {
                Text(text = "Add a Contract")
            }
        }

        if (sharedViewModel.contractList.isEmpty()) {
            Text(
                text = "This employee has no contracts!",
                color = Color.Black,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        LazyColumn(
            state = rememberLazyListState(), modifier = Modifier.padding(bottom = 40.dp)
        ) {
            items(sharedViewModel.contractList, key = { item -> item.id }) { contract ->
                sDate = contract.date_debut.toString()
                eDate = contract.date_fin.toString()
                period = contract.periode.toString()
                salary = contract.salaire.toString()
                valid = contract.valide.toBoolean()
                Box(modifier = Modifier.animateItemPlacement()) {
                    SwipeableBoxPreview(navCtr,
                        Modifier.padding(),
                        sharedViewModel,
                        contract,
                        onRemoveClicked = {
                            sharedViewModel.contractList.remove(contract)
                        })
                }

                Spacer(Modifier.height(1.dp))
//            Divider()
                Spacer(Modifier.height(1.dp))

            }
        }
    }
    if (showDialog.value)
        CustomDialog(value = "", setShowDialog = {
            showDialog.value = it
        }, sharedViewModel)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SwipeableBoxPreview(
    navCtr: NavHostController,
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    contract: Contract,
    onRemoveClicked: (contract: Contract) -> Unit
) {
    val isSnoozed by rememberSaveable { mutableStateOf(false) }
    val isArchived by rememberSaveable { mutableStateOf(false) }

    var isActive by rememberSaveable { mutableStateOf(false) }
    val editContract = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Edit),
        background = Color.Perfume,
        onSwipe = {

//            sharedViewModel.defineUsersFocus(true)
//            navCtr.navigate(Screen.ManageUser.route)
        },
        isUndo = false,
    )
    val payments = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Pages),
        background = Color.Fern,
        onSwipe = {
//            isArchived = !isArchived
//            sharedViewModel.defineUsersFocus(true)
//            userContractAPI(user.id.toInt(), sharedViewModel)
            println("Contra swiped")
//            navCtr.navigate(Screen.Contract.route)
        },
        isUndo = isArchived,
    )
//    if (isActive)

    val remove = SwipeAction(
//        icon = rememberVectorPainter(Icons.TwoTone.Delete),
        icon = { testSide() },
        background = MaterialTheme.colorScheme.error,
        onSwipe = {
//            isSnoozed = !isSnoozed
//            println("IDDDD" + user.id)
//            deleteUserAPI(user.id.toInt())
            deleteContract(contract.id.toInt())
            onRemoveClicked(contract)
//            sharedViewModel.userList.remove(user)
        },

        isUndo = isSnoozed,
    )
    val invalid = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Block),
        background = Color.SeaBuckthorn,
        onSwipe = {
//            isSnoozed = !isSnoozed
//            println("IDDDD" + user.id)
//            deleteUserAPI(user.id.toInt())
//            onRemoveClicked(user)
//            sharedViewModel.userList.remove(user)
        },

        isUndo = isSnoozed,
    )
    SwipeableActionsBox(
        modifier = modifier,
        startActions = listOf(payments, editContract),
        endActions = listOf(invalid, remove),
        swipeThreshold = 40.dp,
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp),
    ) {
        SwipeItem(
            sharedViewModel
        )
    }


}

@Composable
fun testSide() {
    Text("Snooze")
    Text("Blooooze")

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SwipeItem(
    sharedViewModel: SharedViewModel
) {
    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clickable(
                onClick = {
                    println("clicked")
                })
            .background(MaterialTheme.colorScheme.primaryContainer),

        Arrangement.SpaceEvenly

    ) {

        Column(
            Modifier
                .fillMaxHeight()
                .background(Color.Transparent)
                .padding(vertical = 16.dp, horizontal = 15.dp)
                .animateContentSize()

        ) {
            Text(
                text = "Start Date     ${sDate}",
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Text(
                text = "End Date       ${eDate}", color = Color.Black, fontSize = 20.sp
            )
        }
        Divider(
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .padding(vertical = 10.dp)
        )

        Column(
            Modifier
                .fillMaxHeight()
//                        .fillMaxWidth()
//                        .weight(1f)
//                        .shadow(1.dp)
                .background(Color.Transparent)
                .padding(vertical = 16.dp, horizontal = 15.dp)
                .animateContentSize()

        ) {
            Text(
                text = "Period ${period} ",
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = "Salary ${salary}", color = Color.Black, fontSize = 20.sp
            )
        }
    }
//            Spacer(Modifier.height(2.dp))
}

@Composable
fun CustomDialog(
    value: String, setShowDialog: (Boolean) -> Unit, sharedViewModel: SharedViewModel
) {
    val sDate = remember {
        mutableStateOf(TextFieldValue("2023-08-17"))
    }
    val periode = remember {
        mutableStateOf(TextFieldValue("1"))
    }
    val salary = remember {
        mutableStateOf(TextFieldValue("199"))
    }
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp), color = Color.White
        ) {
            Column() {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    OutlinedTextField(value = sDate.value,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        onValueChange = { sDate.value = it },
                        singleLine = true,
                        label = {
                            Text(
                                text = "Start Date",
                                fontSize = 15.sp,
                            )
                        })

                    OutlinedTextField(value = periode.value,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        onValueChange = { periode.value = it },
                        singleLine = true,
                        label = {
                            Text(
                                text = "period",
                                fontSize = 15.sp,
                            )
                        })

                    OutlinedTextField(value = salary.value,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        onValueChange = { salary.value = it },
                        singleLine = true,
                        label = {
                            Text(
                                text = "salary",
                                fontSize = 15.sp,
                            )
                        })
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 6.dp)
                        .height(50.dp),
                    onClick = {
                        val contract = Contract()
                        contract.date_debut = sDate.value.text
                        contract.periode = periode.value.text
                        contract.salaire = salary.value.text
                        contract.user_id = sharedViewModel.selectedUserId
                        addContract(contract)

                        //resets all fields
                        sDate.value = TextFieldValue("")
                        periode.value = TextFieldValue("")
                        salary.value = TextFieldValue("")

                        //close dialog
                        setShowDialog(false)
                    }) {
                    Text(text = "Create")
                }
            }
        }
    }
}

@Preview
@Composable
fun ContractUserPreview() {
    ManageUser(TODO(), TODO())
}