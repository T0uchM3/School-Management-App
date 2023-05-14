package com.example.schoolmanagementsystem.script

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Block
import androidx.compose.material.icons.twotone.Delete
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("RememberReturnType")
@Composable
fun PaymentScreen(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    val showAddDialog = remember { mutableStateOf(false) }
    val payments = remember { SnapshotStateList<Payment>() }

    LaunchedEffect(key1 = sharedViewModel.paymentList.size) {
        payments.clear()
        for (payment in sharedViewModel.paymentList) {
            println(sharedViewModel.selectedContractId + " payment id " + payment.contrat_id)
            if (payment.contrat_id.toString() == sharedViewModel.selectedContractId) {
                payments.add(payment)
                println("found one")
            }
        }
    }
    println("test FRT " + payments.size)
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .background(Color.Red)
                .fillMaxWidth()
        ) {
            Button(onClick = {
                showAddDialog.value = true
            }) {
                Text(text = "Add a Payment")
            }
        }

        if (payments.isEmpty()) {
            Text(
                text = "No payment has been made for this contract!",
                color = Color.Black,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
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
//            Divider()
                    Spacer(Modifier.height(1.dp))

                }
            }
        }
    }
    if (showAddDialog.value)
        AddPaymentDialog(value = "", setShowDialog = {
            showAddDialog.value = it
        }, sharedViewModel)
}

@OptIn(ExperimentalFoundationApi::class)
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
//            deleteContract(contract.id.toInt())
        },

        isUndo = isSnoozed,
    )
    SwipeableActionsBox(
        modifier = modifier,
//        startActions = listOf(payments, editContract),
        endActions = listOf(remove),
        swipeThreshold = 40.dp,
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp),
    ) {
        SwipeItem(
            sharedViewModel, navCtr = navCtr, payment = payment
        )
    }
//    if (showEditDialog.value)
//        EditContractDialog(value = "", setShowDialog = {
//            showEditDialog.value = it
//        }, sharedViewModel, contract)

}

@OptIn(ExperimentalFoundationApi::class)
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
                    navCtr.navigate(Screen.Payment.route)

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
                text = "montant     ${payment.montant}",
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Text(
                text = "month       ${payment.mois}", color = Color.Black, fontSize = 20.sp
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
                text = "type ${payment.type} ",
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = "date ${payment.date}", color = Color.Black, fontSize = 20.sp
            )
        }
    }
//            Spacer(Modifier.height(2.dp))
}

@Composable
fun AddPaymentDialog(
    value: String, setShowDialog: (Boolean) -> Unit, sharedViewModel: SharedViewModel
) {
    val date = remember {
        mutableStateOf(TextFieldValue("2023-08-17"))
    }
    val mois = remember {
        mutableStateOf(TextFieldValue("1"))
    }
    val amount = remember {
        mutableStateOf(TextFieldValue("666"))
    }
    val type = remember {
        mutableStateOf(TextFieldValue("espece"))
    }
    val ref = remember {
        mutableStateOf(TextFieldValue(""))
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

                    OutlinedTextField(value = mois.value,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        onValueChange = { mois.value = it },
                        singleLine = true,
                        label = {
                            Text(
                                text = "Month",
                                fontSize = 15.sp,
                            )
                        })

                    OutlinedTextField(value = amount.value,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        onValueChange = { amount.value = it },
                        singleLine = true,
                        label = {
                            Text(
                                text = "amount",
                                fontSize = 15.sp,
                            )
                        })
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
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 6.dp)
                        .height(50.dp),
                    onClick = {
                        val payment = Payment()
                        payment.date = date.value.text
                        payment.mois = mois.value.text
                        payment.montant = amount.value.text
                        payment.type = type.value.text
                        payment.ref = ref.value.text
                        payment.contrat_id = sharedViewModel.selectedContractId

                        //clearing lists to avoid lazycol parsing error
                        sharedViewModel.contractList.clear()
                        sharedViewModel.paymentList.clear()
                        addPayment(payment, sharedViewModel = sharedViewModel, true)

                        //resets all fields
                        date.value = TextFieldValue("")
                        mois.value = TextFieldValue("")
                        amount.value = TextFieldValue("")
                        type.value = TextFieldValue("")
                        ref.value = TextFieldValue("")


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
fun Preview442() {
    PaymentScreen(TODO(), TODO())
}