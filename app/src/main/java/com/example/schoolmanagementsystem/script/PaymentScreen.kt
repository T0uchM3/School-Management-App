package com.example.schoolmanagementsystem.script

import android.annotation.SuppressLint
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.example.schoolmanagementsystem.ui.theme.scope
import com.example.schoolmanagementsystem.ui.theme.sheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.util.Calendar

var payments = SnapshotStateList<Payment>()

var paymentToEdit = Payment()
var amountLeft = mutableStateOf(0)

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@SuppressLint("RememberReturnType")
@Composable
fun PaymentScreen(navCtr: NavHostController, sharedViewModel: SharedViewModel) {
    val isEdit = remember { mutableStateOf(false) }
    payments = remember { SnapshotStateList<Payment>() }
    val userName = remember { mutableStateOf("") }
    if (sharedViewModel.user?.role == "admin")
        sharedViewModel.defineFabVisible(true)
    else
        sharedViewModel.defineFabVisible(false)
    BackPressHandlerP(navCtr = navCtr, sharedViewModel = sharedViewModel)

    sheetState = remember {
        SheetState(
            skipHiddenState = false, skipPartiallyExpanded = true, initialValue = SheetValue.Hidden
        )
    }
    scope = rememberCoroutineScope()
    if (sheetState?.isVisible == true) {
        ModalBottomSheet(sheetState = sheetState!!, scrimColor = Color.Transparent,
            dragHandle = null, shape = RoundedCornerShape(
                bottomStart = 0.dp, bottomEnd = 0.dp, topStart = 12.dp, topEnd = 12.dp
            ), onDismissRequest = {
                scope?.launch {
                    sheetState?.hide()
                }
            }, content = {
                if (sheetAction == "add") {
                    isEdit.value = false
                    ManagePaymentSheet(
                        sharedViewModel = sharedViewModel,
                        scope = scope,
                        state = sheetState,
                        isEdit = isEdit
                    )
                }
                if (sheetAction == "edit") {
                    isEdit.value = true
                    ManagePaymentSheet(
                        sharedViewModel = sharedViewModel,
                        scope = scope,
                        state = sheetState,
                        isEdit = isEdit
                    )
                }
            })
    } else sharedViewModel.defineFABClicked(false)
    LaunchedEffect(key1 = sharedViewModel.paymentList.size) {
        // Filling the list
        updateValues(sharedViewModel)
        userName.value =
            sharedViewModel.userList.find { it.id.toString() == sharedViewModel.selectedUserId }?.name.toString()
        //setting up fab
        if (sharedViewModel.user?.role == "admin")
            sharedViewModel.defineFabClick {
                sheetAction = "add"
                scope?.launch {
                    sheetState?.show()
                }
            }
    }

    val pullRefreshState = rememberPullRefreshState(sharedViewModel.isRefreshing, {
        sharedViewModel.userList.clear()
        sharedViewModel.contractList.clear()
        sharedViewModel.paymentList.clear()
        usersAPI(sharedViewModel = sharedViewModel)
        updateValues(sharedViewModel)
    })
    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF3F7CC4), Color(0xFF7AB8FF))
                    )
                )
                .padding(top = 5.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                IconButton(onClick = {
                    sharedViewModel.defineSelectedContract(null)
                    navCtr.popBackStack()
                }) {
                    Icon(
                        Icons.TwoTone.ArrowBack,
                        "",
                        tint = Color(0xCCFFFFFF),
                        modifier = Modifier
                            .scale(1.3f)
                            .padding(end = 5.dp)
                    )
                }
                Text(
                    text = userName.value + " > " + sharedViewModel.selectedSalary + " (${amountLeft.value} left)",
                    color = Color(0xCCFFFFFF),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.weight(1f))

            }
            Column(
                Modifier
                    .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                /**
                 * setting up the list section
                 */
                if (payments.isEmpty()) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.no_payment_has_been_made_for_this_contract),
                            color = Color.Black,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {

                    LazyColumn(
                        state = rememberLazyListState(),
                        modifier = Modifier
                            .padding(bottom = 40.dp, top = 15.dp)
                            .padding(horizontal = 7.dp)

                    ) {
                        items(payments, key = { item -> item.id }) { payment ->
                            Box(modifier = Modifier.animateItemPlacement()) {
                                if (sharedViewModel.user?.role == "admin")
                                    SwipeableBoxPreview(navCtr,
                                        Modifier.padding(),
                                        sharedViewModel,
                                        payment,
                                        onRemoveClicked = {
                                            payments.remove(payment)
                                            sharedViewModel.paymentList.remove(payment)
                                        })
                                else
                                    Card(
                                        Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)

                                    ) {
                                        SwipeItem(
                                            sharedViewModel, navCtr = navCtr, payment = payment
                                        )
                                    }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = sharedViewModel.isRefreshing,
            state = pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

//handling the fab position when going back without the top arrow
@Composable
fun BackPressHandlerP(
    backPressedDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    navCtr: NavHostController? = null,
    sharedViewModel: SharedViewModel
) {

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sharedViewModel.defineSelectedContract(null)
                navCtr?.popBackStack()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
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
    amountLeft.value = sharedViewModel.selectedSalary.toInt() - amountPaid
    if (sharedViewModel.user?.role == "admin")
        if (amountPaid == sharedViewModel.selectedSalary.toInt()) sharedViewModel.defineFabVisible(
            false
        )
        else sharedViewModel.defineFabVisible(true)


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
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                tint = Color(0xCCFFFFFF),
                modifier = Modifier.scale(1f)
            )
        },
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
        icon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "",
                tint = Color(0xFFFFFFFF),
                modifier = Modifier.scale(1f)
            )

        },
        background = MaterialTheme.colorScheme.onSurface,
        onSwipe = {
            if (sharedViewModel.selectedcontract!!.valide == "0") return@SwipeAction
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
    Card(
        Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)

    ) {
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

}

@Composable
private fun SwipeItem(
    sharedViewModel: SharedViewModel, navCtr: NavHostController, payment: Payment
) {
    Row(
        modifier = Modifier
//            .clip(shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clickable(onClick = {
                println("clicked")
            })
            .background(Color.White),

        Arrangement.SpaceEvenly

    ) {
        /**
        setting the left vertical blue marker
         */
        if (sharedViewModel.user?.role == "admin")
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .height(80.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(8.dp)
                        .height(20.dp)
                        .background(MaterialTheme.colorScheme.inverseSurface)
                )
            }
        Column(
            Modifier
                .fillMaxHeight()
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .animateContentSize()
                .weight(0.5f)
        ) {
            Text(
                text = "Date:      ${payment.date?.substring(0, 10)}",
                color = Color.Black,
                fontSize = 17.sp,
                modifier = Modifier.padding(bottom = 0.dp),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = "month:      ${payment.mois}",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black,
                fontSize = 17.sp
            )
            Text(
                text = "type:       ${payment.type}",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black,
                fontSize = 17.sp
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
                .background(Color.Transparent)
                .padding(start = 15.dp)
                .animateContentSize()
                .weight(0.25f)

        ) {
            Text(
                text = "Amount: ",
                color = Color.Black,
                fontSize = 13.sp,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 10.dp)
            )


            Text(
                text = payment.montant.toString(),
//                color = Color.Black,
                fontSize = 30.sp,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            )
        }
        /**
        setting the right vertical red marker
         */
        if (sharedViewModel.user?.role == "admin")
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .height(80.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(8.dp)
                        .clip(shape = RoundedCornerShape(topEnd = 25.dp, bottomEnd = 25.dp))
                        .height(20.dp)
                        .background(Color(0x80FFD7D7))
                )
            }
    }
}

//var year2 = mutableStateOf(Calendar.getInstance().get(Calendar.YEAR))
//var month2 = mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
//var day2 = mutableStateOf(Calendar.getInstance().get(Calendar.MONTH))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePaymentSheet(
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope? = null,
    state: SheetState? = null,
    isEdit: MutableState<Boolean>,
) {


// Changing the values in ManageUser cause the
// date picker uses those values
    if (isEdit.value) {
        stringToDate(paymentToEdit.date!!)
    }

    val newMontant: MutableState<Float>?
    newMontant =
        remember { mutableStateOf(if (isEdit.value) paymentToEdit.montant!!.toFloat() else 0f) }
    val ogMontant: MutableState<Float>?
    ogMontant =
        remember { mutableStateOf(0f) }
    val newMonth: MutableState<Float>?
    newMonth = remember { mutableStateOf(if (isEdit.value) paymentToEdit.mois!!.toFloat() else 1f) }
    val ogMonth: MutableState<Float>?
    ogMonth = remember { mutableStateOf(if (isEdit.value) paymentToEdit.mois!!.toFloat() else 1f) }
    val editedAmountLeft: MutableState<Int>?
    editedAmountLeft =
        remember { mutableStateOf(0) }

    val selectedType: MutableState<Int>?
    selectedType = remember {
        mutableStateOf(
            if (isEdit.value) when (paymentToEdit.type) {
                "Espèce" -> 1
                "Virement" -> 2
                "Chéque" -> 3
                else -> -1
            } else -1
        )
    }

    LaunchedEffect(key1 = Unit) {
        // For status bar shadow simulation
        sharedViewModel.defineFABClicked(true)

        // Trying to avoid changing amountLeft directly since that can mess things up
        editedAmountLeft.value = amountLeft.value + newMontant.value.toInt()
    }


    var refInput: MutableState<TextFieldValue>? = null
    refInput = remember {
        mutableStateOf(TextFieldValue(if (isEdit.value) paymentToEdit.ref.toString() else ""))
    }
    if (paymentToEdit.ref.toString() == "null")
        refInput.value = TextFieldValue("")
    Column() {
        Column(
            Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {
                        sharedViewModel.defineFABClicked(false)
                        scope?.launch { state?.hide() }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Text(text = "Cancel", fontSize = 17.sp, color = Color.Red)
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = if (isEdit.value) "Edit Payment" else "New Payment",
                    fontSize = 20.sp,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.weight(1f))
                Button(
                    enabled = if (newMontant.value > editedAmountLeft.value.toFloat() ||
                        newMontant.value < 0f || selectedType.value == -1 || newMonth.value < 1
                    ) false else true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Gray,
                        contentColor = MaterialTheme.colorScheme.inverseSurface
                    ),
                    onClick = {
                        val payment = Payment()
                        if (isEdit.value)
                            payment.id = paymentToEdit.id
                        payment.date = "${year.value}-${
                            String.format(
                                "%02d", month.value + 1
                            )
                        }-${String.format("%02d", day.value)}"
                        payment.mois = newMonth.value.toInt().toString()
                        payment.montant = newMontant.value.toInt().toString()
//                        payment.type = type.value.text
                        payment.ref = refInput.value.text
                        payment.contrat_id = sharedViewModel.selectedContractId
                        payment.type =
                            if (selectedType!!.value == 1) "Espèce" else if (selectedType!!.value == 2) "Virement" else "Chéque"

                        sharedViewModel.contractList.clear()
                        sharedViewModel.paymentList.clear()

                        if (isEdit.value)
                            updatePayment(paymentToEdit.id.toInt(), payment, sharedViewModel)
                        else
                            addPayment(payment, sharedViewModel = sharedViewModel, true)

                        sharedViewModel.defineFABClicked(false)
                        //hide fab
                        scope?.launch { state?.hide() }


                    },
                ) {
                    Text(
                        text = if (isEdit.value) "Update" else "Create", fontSize = 17.sp,
                    )
                }
            }
            Text(
                text = "Start Date", modifier = Modifier.padding(
                    top = 5.dp,
                    start = 15.dp,
                ), style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
            )
            sharedViewModel.defineIsNewUser(!isEdit.value)
            DatePickerUI(sharedViewModel, year, month, day)

            ScrollableTextField(
                sharedViewModel = sharedViewModel,
                labelText = "Month",
                tfValue = newMonth,
                min = ogMonth
            )
            ScrollableTextField(
                sharedViewModel = sharedViewModel,
                labelText = "Amount",
                tfValue = newMontant,
                min = ogMontant,
                max = if (isEdit.value) editedAmountLeft else amountLeft
            )

            Text(
                text = "Type", modifier = Modifier.padding(
                    top = 5.dp,
                    start = 15.dp,
                ), style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
            )
            SegmentedButtonsRole(selectedType, "Espèce", "Virement", "Chéque")



            TextField(sharedViewModel = sharedViewModel, refInput!!, "ref")
            Spacer(modifier = Modifier.height(20.dp))

        }

    }
}


@Preview
@Composable
fun Preview442() {
    PaymentScreen(TODO(), TODO())
}