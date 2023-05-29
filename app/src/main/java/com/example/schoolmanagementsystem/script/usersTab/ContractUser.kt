package com.example.schoolmanagementsystem.script

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Block
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.example.schoolmanagementsystem.ui.theme.scope
import com.example.schoolmanagementsystem.ui.theme.sheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import kotlin.math.abs


var sheetAction = ""
var contractHolder: Contract? = null

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ContractUser(
    navCtr: NavHostController? = null,
    sharedViewModel: SharedViewModel,
) {
    val fabClicked = remember { mutableStateOf(false) }
    val contracts = remember { SnapshotStateList<Contract>() }
    val userName = remember { mutableStateOf("") }

    sheetState =
        remember {
            SheetState(
                skipHiddenState = false,
                skipPartiallyExpanded = false,
                initialValue = SheetValue.Hidden
            )
        }
    scope = rememberCoroutineScope()
    if (sheetState?.isVisible == true) {
        ModalBottomSheet(
            sheetState = sheetState!!,
            dragHandle = null,
            shape = RoundedCornerShape(
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
                topStart = 12.dp,
                topEnd = 12.dp
            ),
            onDismissRequest = {
                scope?.launch {
                    sheetState?.hide()
                }
            },
            content = {
                if (sheetAction == "add")
                    AddContractSheet(value = "", scope = scope, state = sheetState, sharedViewModel)
                if (sheetAction == "edit")
                    EditContractSheet(
                        value = "",
                        scope = scope,
                        state = sheetState,
                        sharedViewModel,
                    )
            }
        )
    }

    LaunchedEffect(key1 = sharedViewModel?.contractList?.size) {
        contracts.clear()
        //search through contract list, and save to new list any contract
        //that it's user id match the one we selected in the previous view.
        for (contract in sharedViewModel!!.contractList) {
            if (contract.user_id.toString() == sharedViewModel.selectedUserId) {
                contracts.add(contract)
            }
        }
        //show faf incase it's invisible
        sharedViewModel.defineFabVisible(true)
        userName.value =
            sharedViewModel.userList.find { it.id.toString() == sharedViewModel.selectedUserId }?.name.toString()
        //setting up fab
        sharedViewModel.defineFabClick {
//            fabClicked.value = true
            sheetAction = "add"
            sharedViewModel.defineIsNewUser(true)
            scope?.launch {
                sheetState?.show()
            }

        }

    }
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        Row(
            Modifier
                .fillMaxWidth(),
//                .shadow(1.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            IconButton(onClick = { navCtr?.popBackStack() }) {
                Icon(Icons.TwoTone.ArrowBack, "")
            }
            Text(
                text = userName.value.toString(), fontSize = 20.sp,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

        }
//        return
        if (contracts.isEmpty()) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "This employee has no contracts!",
                    color = Color.Black,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {

            LazyColumn(
                state = rememberLazyListState(), modifier = Modifier.padding(bottom = 40.dp)
            ) {
                items(
                    contracts.sortedByDescending { it.valide },
                    key = { item -> item.id }) { contract ->
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
                    contractHolder = contract
                    Spacer(Modifier.height(1.dp))
                    Divider(Modifier.padding(horizontal = 20.dp))
                    Spacer(Modifier.height(1.dp))

                }
            }
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
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
        background = MaterialTheme.colorScheme.outline,
        onSwipe = {
            if (contract.valide == "0")
                return@SwipeAction
//            showEditDialog.value = true
            sharedViewModel.defineSelectedContractId(contract.id.toString())
            sheetAction = "edit"
            scope?.launch {
                sheetState?.show()
            }
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
        background =
        if (contract.valide == "0") MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.outline,
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

}


@Composable
private fun SwipeItem(
    sharedViewModel: SharedViewModel? = null,
    navCtr: NavHostController? = null,
    contract: Contract? = null,
    valid: Boolean? = null
) {
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
                    sharedViewModel.defineSelectedSalary(contract?.salaire.toString())
                    sharedViewModel.defineSelectedContractId(contract?.id.toString())
                    sharedViewModel.defineSelectedContract(contract!!)
                    navCtr?.navigate(Screen.Payment.route)

                })

//            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 10.dp),

//        Arrangement.SpaceEvenly

    ) {
        Column(
            Modifier
                .width(IntrinsicSize.Min)
                .padding(vertical = 12.dp)
                .alpha(0.6f)
        ) {
            Image(
                imageVector = Icons.Outlined.Circle,
                contentDescription = "",
                Modifier
                    .fillMaxWidth()
                    .size(13.dp)
                    .offset(y = 4.dp)
                    .scale(1f)
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
                    .scale(1f)
                    .zIndex(1f)
            )
        }

        Column(
            Modifier
                .fillMaxHeight()
                .background(Color.Transparent)
                .padding(vertical = 12.dp, horizontal = 5.dp)
                .padding(end = 5.dp, start = 5.dp)
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
                .padding(vertical = 18.dp)
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
//            Row(Modifier.fillMaxWidth()) {
//                Spacer(modifier = Modifier.weight(1f))
//
//            }
            Row(
                Modifier
                    .width(IntrinsicSize.Min)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Salary: ",
                    color = Color.Black,
                    fontSize = 15.sp,
                    modifier = Modifier
                )
                Text(
                    text = "${contract?.salaire} ",
                    color = Color.Black,
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.labelLarge

                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
//                    .background(Color.Red)
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .border(
                            BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 3.dp),
                    text = " Payments: $nbrPayments ",
                    fontSize = 13.sp
                )
                //invalid indicator
                if (valid == true)
                    Text(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.tertiaryContainer,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 5.dp, vertical = 3.dp),
                        text = " Valid ",
                        fontSize = 13.sp

                    )
                else
                    Text(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 5.dp, vertical = 3.dp),
                        text = " Invalid ",
                        fontSize = 13.sp,
                        color = Color.White

                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContractSheet(
    value: String? = null,
    scope: CoroutineScope? = null,
    state: SheetState? = null,
    sharedViewModel: SharedViewModel? = null
) {

    val sDate = remember {
        mutableStateOf(TextFieldValue("2023-08-17"))
    }
//    val periode = remember {
//        mutableStateOf(TextFieldValue(500f))
//    }
    var periode by remember { mutableStateOf(1f) }
    var oldPeriod by remember { mutableStateOf(0f) }
    var salary by remember { mutableStateOf(500f) }
    var oldSalary by remember { mutableStateOf(0f) }
    if (value != null && sharedViewModel != null)
        Column() {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { scope?.launch { state?.hide() } },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(text = "Cancel", fontSize = 17.sp)
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "New Contract",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {
                            val contract = Contract()
                            contract.date_debut = sDate.value.text
                            contract.periode = periode.toInt().toString()
                            contract.salaire = salary.toInt().toString()
                            contract.user_id = sharedViewModel.selectedUserId
                            //clearing lists to avoid lazycol parsing error
                            sharedViewModel.contractList.clear()
                            sharedViewModel.paymentList.clear()
                            //api call
                            addContract(contract, sharedViewModel, true)
                            //resets all fields
//                            sDate.value = TextFieldValue("")
//                            periode = 0f
//                            salary.value = TextFieldValue("")

                            scope?.launch { state?.hide() }


                        }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(text = "Add", fontSize = 17.sp, color = Color(0xff386A1F))
                    }
                }
                OutlinedTextField(
                    value = sDate.value,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { sDate.value = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = "Start Date",
                            fontSize = 15.sp,
                        )
                    },
                    colors = sharedViewModel.tFColors(),
                )

                OutlinedTextField(
                    value = periode.toInt().toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { periode = (it.toIntOrNull() ?: 0).toFloat() },
                    singleLine = true,
                    label = { Text("Period") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onVerticalDrag = { change, dragAmount ->
                                    change.consume()
                                    var y = dragAmount
                                    //making sure period is above 1
                                    oldPeriod -= dragAmount.times(0.02f)
                                    if (oldPeriod < 1) {
                                        oldPeriod = 1f
                                        return@detectVerticalDragGestures
                                    }
                                    periode -= dragAmount.times(0.02f)
                                },
                            )
                        },
                    colors = sharedViewModel.tFColors(),
                )

                OutlinedTextField(
                    value = salary.toInt().toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { salary = (it.toIntOrNull() ?: 0).toFloat() },
                    singleLine = true,
                    label = { Text("salary") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onVerticalDrag = { change, dragAmount ->
                                    change.consume()
                                    var y = dragAmount
                                    //making sure period is above 1
                                    oldSalary -= dragAmount.times(0.02f)
                                    if (oldSalary < 1) {
                                        oldSalary = 1f
                                        return@detectVerticalDragGestures
                                    }
                                    salary -= dragAmount.times(0.02f)
                                },
                            )
                        },
                    colors = sharedViewModel.tFColors(),
                )
            }

        }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun EditContractSheet(
    value: String,
    scope: CoroutineScope? = null,
    state: SheetState? = null,
    sharedViewModel: SharedViewModel,
) {
    var period by remember { mutableStateOf(0f) }
    var oldPeriod by remember { mutableStateOf(0f) }
    var checker by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = Unit) {
        for (contract in sharedViewModel.contractList) {
            if (contract.id.toString() == sharedViewModel.selectedContractId)
                period = contract.periode!!.toFloat()
        }
        oldPeriod = period
        checker = period
    }



    Column() {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { scope?.launch { state?.hide() } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(text = "Cancel", fontSize = 17.sp)
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Update Contract",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Spacer(Modifier.weight(1f))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    enabled = !(period < oldPeriod || period <= 0f),
                    modifier = Modifier.alpha(if (period < oldPeriod || period == 0f) 0.4f else 1f),
                    onClick = {
                        val ph = PeriodHolder()
                        ph.periode = period.toInt().toString()
                        sharedViewModel.userList.clear()
                        sharedViewModel.contractList.clear()
                        updateContract(
                            sharedViewModel.selectedContractId.toInt(),
                            ph, sharedViewModel
                        )

                        //close dialog
                        scope?.launch { state?.hide() }
                    }) {
                    Text(
                        text = "Update", fontSize = 17.sp,
                        color = if (period < oldPeriod || period == 0f) Color.Gray else Color(
                            0xff386A1F
                        )
                    )
                }
            }

            OutlinedTextField(
                value = period.toInt().toString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { period = (it.toIntOrNull() ?: 0).toFloat() },
                singleLine = true,
                label = { Text("Period") },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                var y = dragAmount
                                //making sure, we can only increase the period without
                                // decreasing it pass the default value
                                oldPeriod -= dragAmount.times(0.02f)
                                if (oldPeriod < checker) {
                                    oldPeriod = checker
                                    return@detectVerticalDragGestures
                                }
                                period -= dragAmount.times(0.02f)
                            },
                        )
                    },
                colors = sharedViewModel.tFColors(),
            )
        }

    }
}

@Preview
@Composable
fun ContractUserPreview() {
//    ContractUser(} )
    SwipeItem()
}