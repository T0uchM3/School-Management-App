package com.example.schoolmanagementsystem.script

import android.annotation.SuppressLint
import android.media.Image
import android.widget.ImageButton
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.LineAxis
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.ShapeLine
import androidx.compose.material.icons.outlined.South
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Block
import androidx.compose.material.icons.twotone.Circle
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.DisabledByDefault
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Pages
import androidx.compose.material.icons.twotone.Radio
import androidx.compose.material.icons.twotone.RadioButtonChecked
import androidx.compose.material.icons.twotone.ReplayCircleFilled
import androidx.compose.material.icons.twotone.South
import androidx.compose.material.icons.twotone.Straight
import androidx.compose.material.icons.twotone.Timeline
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.example.schoolmanagementsystem.ui.theme.Fern
import com.example.schoolmanagementsystem.ui.theme.Perfume
import com.example.schoolmanagementsystem.ui.theme.SeaBuckthorn
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ContractUser(
    navCtr: NavHostController? = null,
    sharedViewModel: SharedViewModel? = null,
) {
    val showAddDialog = remember { mutableStateOf(false) }
    val contracts = remember { SnapshotStateList<Contract>() }
    val userName = remember { mutableStateOf("Admin") }

    LaunchedEffect(key1 = sharedViewModel?.contractList?.size) {
        contracts.clear()
        //search through contract list, and save to new list any contract
        //that it's user id match the one we selected in the previous view.
        for (contract in sharedViewModel!!.contractList) {
            if (contract.user_id.toString() == sharedViewModel.selectedUserId) {
                contracts.add(contract)
            }
        }
        userName.value =
            sharedViewModel.userList.find { it.id.toString() == sharedViewModel.selectedUserId }?.name.toString()
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Row(
            Modifier
//                .background(Color.Red)
                .fillMaxWidth()
                .shadow(1.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            IconButton(onClick = { navCtr?.popBackStack() }) {
                Icon(Icons.TwoTone.ArrowBack, "")
            }
//            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = userName.value.toString(), fontSize = 20.sp,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {
                showAddDialog.value = true
            }) {
                Text(text = "Add Contract")
            }
        }
//        return
        if (sharedViewModel?.contractList!!.isEmpty()) {
            Text(
                text = "This employee has no contracts!",
                color = Color.Black,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {

            LazyColumn(
                state = rememberLazyListState(), modifier = Modifier.padding(bottom = 40.dp)
            ) {
                items(contracts, key = { item -> item.id }) { contract ->
                    Box(modifier = Modifier.animateItemPlacement()) {
                        SwipeableBoxPreview(navCtr,
                            Modifier.padding(),
                            sharedViewModel,
                            contract,
                            onRemoveClicked = {
                                contracts.remove(contract)
                                sharedViewModel.contractList.remove(contract)
                            })
                    }

                    Spacer(Modifier.height(1.dp))
//            Divider()
                    Spacer(Modifier.height(1.dp))

                }
            }
        }
    }
    if (showAddDialog.value)
        AddContractDialog(value = "", setShowDialog = {
            showAddDialog.value = it
        }, sharedViewModel)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SwipeableBoxPreview(
    navCtr: NavHostController?,
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    contract: Contract,
    onRemoveClicked: (contract: Contract) -> Unit
) {
    var isValid by rememberSaveable { mutableStateOf(true) }
    isValid = contract.valide == "1"
    val isArchived by rememberSaveable { mutableStateOf(false) }
    var showEditDialog = remember { mutableStateOf(false) }

    var isActive by rememberSaveable { mutableStateOf(false) }
    val editContract = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Edit),
        background = Color.Perfume,
        onSwipe = {
            showEditDialog.value = true
        },
        isUndo = false,
    )

    val removeOrDisable = SwipeAction(
        icon = {
            if (contract.valide == "0")//not valid
                Image(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = ""
                )
            else
                Image(
                    imageVector = Icons.TwoTone.Block,
                    contentDescription = ""
                )
        },
        background = MaterialTheme.colorScheme.error,
        onSwipe = {
            if (contract.valide == "0") {//only delete when the contract is invalid
                println("11111111+1")
                deleteContract(contract.id.toInt())
                onRemoveClicked(contract)
            } else {
                invalidContract(contract.id.toInt())
                println("2222222+2")
                contract.valide = "0"
                isValid = false
            }
        },

        isUndo = isValid,
    )
    SwipeableActionsBox(
        modifier = modifier,
        startActions = listOf(editContract),
        endActions = listOf(removeOrDisable),
        swipeThreshold = 40.dp,
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp),
    ) {
        SwipeItem(
            sharedViewModel, navCtr = navCtr, contract = contract, valid = isValid
        )
    }
    if (showEditDialog.value)
        EditContractDialog(value = "", setShowDialog = {
            showEditDialog.value = it
        }, sharedViewModel, contract)

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SwipeItem(
    sharedViewModel: SharedViewModel? = null,
    navCtr: NavHostController? = null,
    contract: Contract? = null,
    valid: Boolean? = null
) {
    println("VVVVVVVVVVVVVVVVVVVVVVValide " + valid)
//    if (sharedViewModel != null && navCtr != null && contract != null)
    var nbrPayments: Int = 0
    for (payment in sharedViewModel!!.paymentList) {
//            println(sharedViewModel.selectedContractId + " payment id " + payment.contrat_id)
        if (payment.contrat_id == contract?.id.toString()) {
            nbrPayments++
            println("found one")
        }
    }
    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clickable(
                onClick = {
                    println("clicked")
                    sharedViewModel?.defineSelectedContractId(contract?.id.toString())
                    navCtr?.navigate(Screen.Payment.route)

                })

            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 10.dp),

//        Arrangement.SpaceEvenly

    ) {
        Column(
            Modifier
                .width(IntrinsicSize.Min)
                .padding(vertical = 12.dp)
        ) {
            Image(
                imageVector = Icons.Filled.RadioButtonChecked,
                contentDescription = "",
                Modifier
                    .fillMaxWidth()
                    .size(13.dp)
                    .offset(y = 4.dp)
                    .scale(1.5f)
                    .zIndex(1f)
            )
            Column(
                Modifier
                    .weight(2f)
                    .fillMaxWidth()

            ) {

                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .weight(2f)
                        .width(1.5.dp)
                        .offset(x = 6.dp, y = 5.dp)
                        .zIndex(1f)
                )
                Image(
                    imageVector = Icons.Outlined.ExpandMore,
                    contentDescription = "",
                    modifier = Modifier
                        .scale(1.4f)
                        .fillMaxWidth()
                        .height(10.dp)
                        .aspectRatio(1f)
                        .offset(y = 1.dp),
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
            Image(
                imageVector = Icons.Filled.Circle,
                contentDescription = "",
                Modifier
                    .fillMaxWidth()
                    .size(13.dp)
                    .offset(y = -2.dp)
                    .scale(0.9f)
                    .zIndex(1f)
            )
        }

        Column(
            Modifier
                .fillMaxHeight()
                .background(Color.Transparent)
                .padding(vertical = 12.dp, horizontal = 5.dp)
                .padding(end = 5.dp)
                .animateContentSize()

        ) {
            Text(
                text = "Start:  ${contract?.date_debut}",
                color = Color.Black,
                fontSize = 17.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .weight(1f)
            )
            Text(
                text = "End:    ${contract?.date_fin}",
                color = Color.Black,
                fontSize = 17.sp
            )
        }
        Divider(
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .padding(vertical = 25.dp)
        )

        Column(
            Modifier
                .fillMaxHeight()
                .padding(start = 10.dp)

//                        .fillMaxWidth()
//                        .weight(1f)
//                        .shadow(1.dp)
//                .background(Color.Red)
                .animateContentSize()
                .weight(1f)

        ) {
            Row(Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                if (valid == true)
                    Text(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.tertiaryContainer,
                                RoundedCornerShape(bottomStart = 8.dp)
                            )
                            .padding(horizontal = 0.dp, vertical = 4.dp),
                        text = " valid ",
                        style = MaterialTheme.typography.labelLarge
                    )
                else
                    Text(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(bottomStart = 8.dp)
                            )
                            .padding(horizontal = 0.dp, vertical = 4.dp),
                        text = " Invalid ",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.labelLarge
                    )
            }
            Row(
                Modifier.width(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Salary: ",
                    color = Color.Black,
                    fontSize = 13.sp,
                    modifier = Modifier
                )
                Text(
                    text = "${contract?.salaire} ",
                    color = Color.Black,
                    fontSize = 20.sp,
//                    modifier = Modifier.padding(bottom = 10.dp),
                    style = MaterialTheme.typography.labelLarge

                )
            }
            Row(Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(topStart = 8.dp)
                        )
                        .padding(horizontal = 0.dp, vertical = 4.dp),
                    text = " Payments: $nbrPayments ",
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun AddContractDialog(
    value: String? = null,
    setShowDialog: (Boolean) -> Unit,
    sharedViewModel: SharedViewModel? = null
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
    if (value != null && sharedViewModel != null)
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
                            //clearing lists to avoid lazycol parsing error
                            sharedViewModel.contractList.clear()
                            sharedViewModel.paymentList.clear()
                            //api call
                            addContract(contract, sharedViewModel, true)
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

@Composable
fun EditContractDialog(
    value: String,
    setShowDialog: (Boolean) -> Unit,
    sharedViewModel: SharedViewModel,
    contract: Contract
) {
    val period = remember {
        mutableStateOf(TextFieldValue(contract.periode.toString()))
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

                    OutlinedTextField(value = period.value,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        onValueChange = { period.value = it },
                        singleLine = true,
                        label = {
                            Text(
                                text = "period",
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
                        updateContract(contract.id.toInt(), period.value.text)

                        //resets all fields
                        period.value = TextFieldValue("")

                        //close dialog
                        setShowDialog(false)
                    }) {
                    Text(text = "Update")
                }
            }
        }
    }
}

@Preview
@Composable
fun ContractUserPreview() {
    ContractUser()
//    SwipeItem()
}