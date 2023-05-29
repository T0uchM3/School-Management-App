package com.example.schoolmanagementsystem.script

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.ui.theme.scope
import com.example.schoolmanagementsystem.ui.theme.sheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

var payments = SnapshotStateList<Payment>()
var amountLeft by mutableStateOf<Int>(0)
var paymentToEdit =Payment()

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun PaymentScreen(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    val showAddDialog = remember { mutableStateOf(false) }
    payments = remember { SnapshotStateList<Payment>() }
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
                    AddPaymentSheet(
                        value = "",
                        sharedViewModel = sharedViewModel,
                        scope = scope,
                        state = sheetState
                    )
                if (sheetAction == "edit")
                    EditPaymentSheet(
                        value = "",
                        scope = scope,
                        state = sheetState,
                        payment  = paymentToEdit,
                        sharedViewModel = sharedViewModel
                        )
            }
        )
    }

    LaunchedEffect(key1 = sharedViewModel.paymentList.size) {
        updateValues(sharedViewModel)
        userName.value =
            sharedViewModel.userList.find { it.id.toString() == sharedViewModel.selectedUserId }?.name.toString()
        //setting up fab
        sharedViewModel.defineFabClick {
            sheetAction = "add"
            scope?.launch {
                sheetState?.show()
            }

        }
    }
//    println("test FRT " + payments.size)
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
//                .shadow(1.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            IconButton(onClick = { navCtr?.popBackStack() }) {
                Icon(Icons.TwoTone.ArrowBack, "")
            }
            Text(
                text = userName.value + " > " + sharedViewModel.selectedSalary + " (${amountLeft} left)",
                fontSize = 20.sp,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

        }
        if (payments.isEmpty()) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No payment has been made for this contract!",
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
                items(payments, key = { item -> item.id }) { payment ->
                    Box(modifier = Modifier.animateItemPlacement()) {
                        SwipeableBoxPreview(navCtr,
                            Modifier.padding(),
                            sharedViewModel,
                            payment,
                            onRemoveClicked = {
                                payments.remove(payment)
                                sharedViewModel.paymentList.remove(payment)
                            })
                    }

                    Spacer(Modifier.height(1.dp))
                    Divider(Modifier.padding(horizontal = 20.dp))
                    Spacer(Modifier.height(1.dp))

                }
            }
        }
    }
}

/**
 * Updating the visibility of fab, the amount left to be paid
 * And initializing local list
 */
fun updateValues(sharedViewModel: SharedViewModel) {
    payments.clear()

    //filling local payment list
    for (payment in sharedViewModel.paymentList) {
        println(sharedViewModel.selectedContractId + " payment id " + payment.contrat_id)
        if (payment.contrat_id.toString() == sharedViewModel.selectedContractId) {
            payments.add(payment)
        }
    }
    //deciding if the fab should be visible or not
    //based on if the contract got paid or not
    var amountPaid = 0
    for (payment in payments) {
        amountPaid += payment.montant!!.toInt()
    }
    amountLeft = sharedViewModel.selectedSalary.toInt() - amountPaid
    if (amountPaid == sharedViewModel.selectedSalary.toInt())
        sharedViewModel.defineFabVisible(false)
    else
        sharedViewModel.defineFabVisible(true)


}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableBoxPreview(
    navCtr: NavHostController,
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    payment: Payment,
    onRemoveClicked: (payment: Payment) -> Unit
) {
    val isSnoozed by rememberSaveable { mutableStateOf(false) }
    val isArchived by rememberSaveable { mutableStateOf(false) }
    var showEditDialog = remember { mutableStateOf(false) }

    var isActive by rememberSaveable { mutableStateOf(false) }

    val remove = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Delete),
        background = MaterialTheme.colorScheme.error,
        onSwipe = {
//            deleteContract(contract.id.toInt())
            onRemoveClicked(payment)
            deletePayment(payment.id.toInt())
            updateValues(sharedViewModel = sharedViewModel)
//            deleteContract(contract.id.toInt())
        },

        isUndo = isSnoozed,
    )
    val editPayment = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Edit),
        background = MaterialTheme.colorScheme.outline,
        onSwipe = {
            if(sharedViewModel.selectedcontract!!.valide=="0")
                return@SwipeAction
            //this will trigger recomposition of the PaymentScreen
            //and a condition check will happen for sheetAction
            sheetAction = "edit"
            paymentToEdit = payment
            scope?.launch {
                sheetState?.show()
            }
        },
        isUndo = false,
    )
    SwipeableActionsBox(
        modifier = modifier,
        startActions = listOf(editPayment),
        endActions = listOf(remove),
        swipeThreshold = 40.dp,
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp),
    ) {
        SwipeItem(
            sharedViewModel, navCtr = navCtr, payment = payment
        )
    }

}

@Composable
private fun SwipeItem(
    sharedViewModel: SharedViewModel,
    navCtr: NavHostController,
    payment: Payment
) {
    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clickable(
                onClick = {
                    println("clicked")
                }),
        Arrangement.SpaceEvenly

    ) {

        Column(
            Modifier
                .fillMaxHeight()
                .background(Color.Transparent)
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .animateContentSize()
                .weight(0.5f)

        ) {
            Text(
                text = "Date:      ${payment.date?.substring(0, 10)}",
                color = Color.Black,
                fontSize = 17.sp,
                modifier = Modifier.padding(bottom = 0.dp)
            )
            Text(
                text = "month:      ${payment.mois}", color = Color.Black, fontSize = 17.sp
            )
            Text(
                text = "type:       ${payment.type}", color = Color.Black, fontSize = 17.sp
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
                .padding(start = 15.dp)
                .animateContentSize()
                .weight(0.4f)

        ) {
            Text(
                text = "Amount: ",
                color = Color.Black,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
//                        .background(Color.Red)
            )


            Text(
                text = payment.montant.toString(),
                color = Color.Black,
                fontSize = 35.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 55.dp)
//                    .background(Color.Red)

            )

//            Text(
//                text = "date ${payment.date}", color = Color.Black, fontSize = 17.sp
//            )
        }
    }
//            Spacer(Modifier.height(2.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentSheet(
    value: String,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope? = null,
    state: SheetState? = null,
) {
    val date = remember {
        mutableStateOf(TextFieldValue("2023-08-17"))
    }
    var mois by remember { mutableStateOf(0f) }

    val type = remember {
        mutableStateOf(TextFieldValue("espece"))
    }
    val ref = remember {
        mutableStateOf(TextFieldValue(""))
    }
    var montant by remember { mutableStateOf(1f) }
    var oldMontant by remember { mutableStateOf(1f) }
    var oldMonth by remember { mutableStateOf(1f) }
    var checker by remember { mutableStateOf(0f) }
    checker = sharedViewModel.selectedSalary.toFloat()
    Column() {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { scope?.launch { state?.hide() } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Text(text = "Cancel", fontSize = 17.sp)
                }
                Spacer(Modifier.weight(1f))
                Text(text = "New Payment", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(Modifier.weight(1f))
                Button(
                    enabled = !(montant > checker || montant <= 0f),
                    modifier = Modifier.alpha(if (montant > checker || montant <= 0f) 0.4f else 1f),

                    onClick = {
                        val payment = Payment()
                        payment.date = date.value.text
                        payment.mois = mois.toInt().toString()
                        payment.montant = montant.toInt().toString()
                        payment.type = type.value.text
                        payment.ref = ref.value.text
                        payment.contrat_id = sharedViewModel.selectedContractId

                        //clearing lists to avoid lazycol parsing error
                        sharedViewModel.contractList.clear()
                        sharedViewModel.paymentList.clear()
                        addPayment(payment, sharedViewModel = sharedViewModel, true)

                        //hide fab
                        scope?.launch { state?.hide() }


                    }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(
                        text = "Create", fontSize = 17.sp,
                        color = if (montant > checker || montant <= 0f) Color.Gray else Color(
                            0xff386A1F
                        )
                    )
                }
            }
            OutlinedTextField(value = date.value,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { date.value = it },
                singleLine = true,
                label = {
                    Text(
                        text = "Date",
                        fontSize = 15.sp,
                    )
                })

            OutlinedTextField(
                value = mois.toInt().toString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { mois = (it.toIntOrNull() ?: 0).toFloat() },
                singleLine = true,
                label = {
                    Text(
                        text = "Month",
                        fontSize = 15.sp,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                var y = dragAmount
                                //making sure, the month is not negative
                                oldMonth -= dragAmount.times(0.05f)
                                if (oldMonth < 1) {
                                    mois = 1f
                                    oldMonth = 1f
                                    return@detectVerticalDragGestures
                                }
                                mois -= dragAmount.times(0.05f)
                            },
                        )
                    },
            )

            OutlinedTextField(
                value = montant.toInt().toString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { montant = (it.toIntOrNull() ?: 0).toFloat() },
                singleLine = true,
                label = {
                    Text(
                        text = "amount",
                        fontSize = 15.sp,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                var y = dragAmount
                                //making sure, the amount stay between 1 and what's left from default amount
                                oldMontant -= dragAmount.times(0.05f)
                                if (oldMontant > amountLeft) {
                                    oldMontant = amountLeft.toFloat()
                                    montant = amountLeft.toFloat()
                                    return@detectVerticalDragGestures
                                }
                                if (oldMontant < 1) {
                                    montant = 1f
                                    oldMontant = 1f
                                    return@detectVerticalDragGestures
                                }
                                montant -= dragAmount.times(0.05f)
                            },
                        )
                    },

                )
            OutlinedTextField(value = type.value,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { type.value = it },
                singleLine = true,
                label = {
                    Text(
                        text = "type",
                        fontSize = 15.sp,
                    )
                })
            OutlinedTextField(value = ref.value,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { ref.value = it },
                singleLine = true,
                label = {
                    Text(
                        text = "ref",
                        fontSize = 15.sp,
                    )
                })
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPaymentSheet(
    value: String,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope? = null,
    state: SheetState? = null,
    payment: Payment,
) {
    val date = remember {
        mutableStateOf(TextFieldValue(payment.date.toString()))
    }
    var mois by remember { mutableStateOf(payment.mois!!.toFloat()) }

    val type = remember {
        mutableStateOf(TextFieldValue(payment.type.toString()))
    }
    val ref = remember {
        mutableStateOf(TextFieldValue(""))
    }
    var montant by remember { mutableStateOf(payment.montant!!.toFloat()) }
    var oldMontant by remember { mutableStateOf(1f) }
    var oldMonth by remember { mutableStateOf(1f) }
    var checker by remember { mutableStateOf(0f) }
    checker = sharedViewModel.selectedSalary.toFloat()
    Column() {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { scope?.launch { state?.hide() } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Text(text = "Cancel", fontSize = 17.sp)
                }
                Spacer(Modifier.weight(1f))
                Text(text = "Update Payment", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(Modifier.weight(1f))
                Button(
                    enabled = !(montant > checker || montant <= 0f),
                    modifier = Modifier.alpha(if (montant > checker || montant!! <= 0f) 0.4f else 1f),

                    onClick = {
                        val paymentFinal = payment
                        paymentFinal.date = date.value.text
                        paymentFinal.mois = mois.toInt().toString()
                        paymentFinal.montant = montant.toInt().toString()
                        paymentFinal.type = type.value.text
                        paymentFinal.ref = ref.value.text

                        //clearing lists to avoid lazycol parsing error
                        sharedViewModel.contractList.clear()
                        sharedViewModel.paymentList.clear()
                        println("PAYMENT IDDD "+paymentFinal.id.toInt())

                        updatePayment(paymentFinal.id.toInt(), paymentFinal, sharedViewModel)

                        //hide fab
                        scope?.launch { state?.hide() }


                    }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(
                        text = "Update", fontSize = 17.sp,
                        color = if (montant > checker || montant <= 0f) Color.Gray else Color(
                            0xff386A1F
                        )
                    )
                }
            }
            OutlinedTextField(value = date.value,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { date.value = it },
                singleLine = true,
                label = {
                    Text(
                        text = "Date",
                        fontSize = 15.sp,
                    )
                })

            OutlinedTextField(
                value = mois?.toInt().toString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { mois = (it.toIntOrNull() ?: 0).toFloat() },
                singleLine = true,
                label = {
                    Text(
                        text = "Month",
                        fontSize = 15.sp,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                var y = dragAmount
                                //making sure, the month is not negative
                                oldMonth -= dragAmount.times(0.05f)
                                if (oldMonth < 1) {
                                    mois = 1f
                                    oldMonth = 1f
                                    return@detectVerticalDragGestures
                                }
                                mois -= dragAmount.times(0.05f)
                            },
                        )
                    },
            )

            OutlinedTextField(
                value = montant.toInt().toString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { montant = (it.toIntOrNull() ?: 0).toFloat() },
                singleLine = true,
                label = {
                    Text(
                        text = "amount",
                        fontSize = 15.sp,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                var y = dragAmount
                                //making sure, the amount stay between 1 and what's left from default amount
                                oldMontant -= dragAmount.times(0.05f)
                                if (oldMontant > amountLeft) {
                                    oldMontant = amountLeft.toFloat()
                                    montant = amountLeft.toFloat()
                                    return@detectVerticalDragGestures
                                }
                                if (oldMontant < 1) {
                                    montant = 1f
                                    oldMontant = 1f
                                    return@detectVerticalDragGestures
                                }
                                montant -= dragAmount.times(0.05f)
                            },
                        )
                    },

                )
            OutlinedTextField(value = type.value,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { type.value = it },
                singleLine = true,
                label = {
                    Text(
                        text = "type",
                        fontSize = 15.sp,
                    )
                })
            OutlinedTextField(value = ref.value,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { ref.value = it },
                singleLine = true,
                label = {
                    Text(
                        text = "ref",
                        fontSize = 15.sp,
                    )
                })
        }

    }
}

@Preview
@Composable
fun Preview442() {
    PaymentScreen(TODO(), TODO())
}