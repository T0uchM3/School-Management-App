package com.example.schoolmanagementsystem.script

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Block
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.example.schoolmanagementsystem.ui.theme.scope
import com.example.schoolmanagementsystem.ui.theme.sheetState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.util.Calendar


var sheetAction = ""
var contractHolder: Contract? = null

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun ContractUser(
    navCtr: NavHostController? = null,
    sharedViewModel: SharedViewModel,
) {
    // In case user came to Contract view from searching
    // fix bottom bar
    sharedViewModel.defineSearchBarFocused(false)
    // fix status bar
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = true
    )

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
            scrimColor = Color.Transparent,
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
    } else
        sharedViewModel.defineFABClicked(false)

    LaunchedEffect(key1 = sharedViewModel?.contractList?.size) {
        contracts.clear()
        //search through contract list, and save to new list any contract
        //that it's user id match the one we selected in the previous view.
        for (contract in sharedViewModel!!.contractList) {
            if (contract.user_id.toString() == sharedViewModel.selectedUserId) {
                contracts.add(contract)
            }
        }
        //show fab incase it's invisible
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
    val pullRefreshState = rememberPullRefreshState(sharedViewModel.isRefreshing, {
        sharedViewModel.userList.clear()
        sharedViewModel.contractList.clear()
        sharedViewModel.paymentList.clear()
        usersAPI(sharedViewModel = sharedViewModel)
        contracts.clear()
        for (contract in sharedViewModel!!.contractList) {
            if (contract.user_id.toString() == sharedViewModel.selectedUserId) {
                contracts.add(contract)
            }
        }
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
                IconButton(onClick = { navCtr?.popBackStack() }) {
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
                    text = userName.value.toString(),
                    color = Color(0xCCFFFFFF),
                    fontSize = 30.sp,
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
                        state = rememberLazyListState(),
                        modifier = Modifier
                            .padding(bottom = 51.dp, top = 15.dp)
                            .padding(horizontal = 7.dp)

                    ) {
                        items(
                            contracts.sortedByDescending { it.valide },
                            key = { item -> item.id }) { contract ->
                            Box(modifier = Modifier.animateItemPlacement()) {
                                if (sharedViewModel.user?.role == "admin")
                                    SwipeableBoxPreview(navCtr,
                                        Modifier.padding(),
                                        sharedViewModel,
                                        contract,
                                        onRemoveClicked = {
                                            contracts.remove(contract)
                                            sharedViewModel.contractList.remove(contract)
                                        })
                                else
                                    Card(
                                        Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)

                                    ) {
                                        SwipeItem(
                                            sharedViewModel,
                                            navCtr = navCtr,
                                            contract = contract,
                                            valid = contract.valide == "1"
                                        )
                                    }
                            }
                            contractHolder = contract
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }

            }
        }
        PullRefreshIndicator(refreshing = sharedViewModel.isRefreshing, state = pullRefreshState,Modifier.align(Alignment.TopCenter))
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
        background = MaterialTheme.colorScheme.onSurface,
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
    Card(
        Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)

    ) {
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
        if (payment.contrat_id == contract?.id.toString()) {
            nbrPayments++
            println("found one")
        }
    }
    /**
    this come above the card
     */
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clickable(
                onClick = {
                    println("clicked")
                    sharedViewModel.defineIsInPayment(true)
                    sharedViewModel.defineSelectedSalary(contract?.salaire.toString())
                    sharedViewModel.defineSelectedContractId(contract?.id.toString())
                    sharedViewModel.defineSelectedContract(contract!!)
                    navCtr?.navigate(Screen.Payment.route)

                })
            .background(Color.White)

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
//                    .clip(shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                        .height(20.dp)
                        .background(MaterialTheme.colorScheme.inverseSurface)
                )
            }
        /**
        setting up the left time image
         */
        Column(
            Modifier
                .width(IntrinsicSize.Min)
                .padding(vertical = 5.dp, horizontal = 5.dp)
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
        /**
        setting up the left text contents
         */
        Column(
            Modifier
                .fillMaxHeight()
                .background(Color.Transparent)
                .padding(vertical = 8.dp, horizontal = 5.dp)
                .padding(end = 5.dp, start = 2.dp)
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
        /**
        setting up the right text contents
         */
        Column(
            Modifier
                .fillMaxHeight()
                .padding(start = 10.dp)
                .animateContentSize()
                .weight(1f)

        ) {
            Row(
                Modifier
                    .width(IntrinsicSize.Min)
                    .padding(top = 5.dp), verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Salary: ",
                    color = Color.Black,
                    fontSize = 22.sp,
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
                    .padding(top = 4.dp),
//                .background(Color.Red),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .border(
                            BorderStroke(2.dp, Color.Gray),
                            RoundedCornerShape(8.dp)
                        ),
//                        .padding(horizontal = 5.dp, vertical = 3.dp),
                    text = " Payments: $nbrPayments ",
                    fontSize = 13.sp
                )
                //invalid indicator
                if (valid == true)
                    Text(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.inverseSurface,
                                RoundedCornerShape(8.dp)
                            ),
                        text = "  Valid  ",
                        color = Color.White,
                        fontSize = 13.sp

                    )
                else
                    Text(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            ),
                        text = "  Invalid  ",
                        fontSize = 13.sp,
                        color = Color.White

                    )
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContractSheet(
    value: String? = null,
    scope: CoroutineScope? = null,
    state: SheetState? = null,
    sharedViewModel: SharedViewModel? = null
) {
    val year = remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val month = remember { mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) }
    val day = remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }

    LaunchedEffect(key1 = Unit) {
        // Bring back the FAB
        sharedViewModel?.defineFABClicked(true)
    }

    val newPeriod: MutableState<Float>?
    newPeriod = remember { mutableStateOf(1f) }
    val ogPeriod: MutableState<Float>?
    ogPeriod = remember { mutableStateOf(1f) }
    val newSalary: MutableState<Float>?
    newSalary = remember { mutableStateOf(0f) }
    val ogSalary: MutableState<Float>?
    ogSalary = remember { mutableStateOf(0f) }


    if (value != null && sharedViewModel != null)
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
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = "Cancel", fontSize = 17.sp, color = Color.Red,
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "New Contract",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        color = Color.DarkGray

                    )
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {
                            val contract = Contract()
                            contract.date_debut = "${year.value}-${
                                String.format(
                                    "%02d",
                                    month.value + 1
                                )
                            }-${String.format("%02d", day.value)}"
//                            println("date format = " + contract.date_debut)
                            contract.periode = newPeriod.value.toInt().toString()
                            contract.salaire = newSalary.value.toInt().toString()
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

                            sharedViewModel.defineFABClicked(false)
                            scope?.launch { state?.hide() }


                        }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = "Add",
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    }
                }
                Text(
                    text = "Start Date",
                    modifier = Modifier.padding(
                        top = 5.dp,
                        start = 15.dp,
                    ),
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
                )
                DatePickerUI(sharedViewModel, year, month, day)



                ScrollableTextField(
                    sharedViewModel = sharedViewModel,
                    labelText = "Period",
                    tfValue = newPeriod,
                    min = ogPeriod
                )
                ScrollableTextField(
                    sharedViewModel = sharedViewModel,
                    labelText = "salary",
                    tfValue = newSalary,
                    min = ogSalary
                )
            }
//            Spacer(modifier = Modifier.height(100.dp))

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

    val newPeriod: MutableState<Float>?
    newPeriod = remember {
        mutableStateOf(0f)
    }
    val ogPeriod: MutableState<Float>?
    ogPeriod = remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(key1 = Unit) {
        sharedViewModel.defineFABClicked(true)
        for (contract in sharedViewModel.contractList) {
            if (contract.id.toString() == sharedViewModel.selectedContractId)
                ogPeriod.value = contract.periode!!.toFloat()
        }
        newPeriod.value = ogPeriod.value
    }

    Column() {
        Column(
            Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { scope?.launch { state?.hide() } },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Red
                    )
                ) {
                    Text(text = "Cancel", fontSize = 17.sp)
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Update Contract",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    color = Color.DarkGray
                )
                Spacer(Modifier.weight(1f))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Gray,
                        contentColor = MaterialTheme.colorScheme.inverseSurface
                    ),
//                    enabled = !(ogPeriod!!.value < newPeriod || ogPeriod!!.value <= 0f),
                    enabled = !(newPeriod!!.value < ogPeriod.value || newPeriod!!.value <= 0f),
                    modifier = Modifier.alpha(if (newPeriod!!.value < ogPeriod.value || newPeriod!!.value <= 0f) 0.4f else 1f),
                    onClick = {
                        val ph = PeriodHolder()
                        ph.periode = newPeriod.value.toInt().toString()
                        sharedViewModel.userList.clear()
                        sharedViewModel.contractList.clear()
                        updateContract(
                            sharedViewModel.selectedContractId.toInt(),
                            ph, sharedViewModel
                        )
                        sharedViewModel.defineFABClicked(false)
                        //close dialog
                        scope?.launch { state?.hide() }
                    }) {
                    Text(
                        text = "Update", fontSize = 17.sp,
                    )
                }
            }
            println("ogPeriodD  " + ogPeriod)
            ScrollableTextField(
                sharedViewModel = sharedViewModel,
                labelText = "Period",
                tfValue = newPeriod,
                min = ogPeriod
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}


@Composable
fun ScrollableTextField(
    sharedViewModel: SharedViewModel,
    labelText: String,
    tfValue: MutableState<Float>,
    min: MutableState<Float>,
    max: MutableState<Int> = mutableStateOf(Int.MAX_VALUE)
) {
    val IndicatorUnfocusedWidth = 1.dp
    val IndicatorFocusedWidth = 2.dp
    val TextFieldPadding = 16.dp
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val indicatorColor = if (isFocused) Color(0xff4774a9) else Color.Gray
    val indicatorWidth = if (isFocused) IndicatorFocusedWidth else IndicatorUnfocusedWidth
    var text = remember { mutableStateOf("") }

    var oldValue = remember { mutableStateOf(tfValue.value) }

    var mod = Modifier.drawBehind {
        val strokeWidth = indicatorWidth.value * density
        val y = size.height - strokeWidth / 2
        drawLine(
            indicatorColor,
            Offset(TextFieldPadding.toPx(), y),
            Offset(size.width - TextFieldPadding.toPx(), y),
            strokeWidth
        )
    }
    val passwordVisible: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    androidx.compose.material3.TextField(
        value = tfValue.value.toInt().toString(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = { tfValue.value = (it.toIntOrNull() ?: 0).toFloat() },
        trailingIcon = {
            val image = if (passwordVisible.value)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description =
                if (passwordVisible.value) "Hide password" else "Show password"
            if (labelText == "password")
                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    Icon(imageVector = image, description, Modifier.scale(0.9f))
                }
            else
                null
        },
        label = { Text(labelText, fontSize = 14.sp) },
        interactionSource = interactionSource,
        modifier = mod
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        var y = dragAmount
                        oldValue.value -= dragAmount.times(0.02f)

                        // In case we got a defined max value
                        // prevent going over it
                        if (oldValue.value > max.value) {
                            oldValue.value = max.value.toFloat()
                            tfValue.value = max.value.toFloat()
                            return@detectVerticalDragGestures
                        }

                        // Making sure, we can only increase the period without
                        // decreasing it pass the default value
                        println("oldperiod " + oldValue.value + " checker " + min.value + " period " + tfValue.value)
                        if (oldValue.value <= min.value) {
                            println("1111111111111111")
                            oldValue.value = min.value
                            tfValue.value = min.value
                            return@detectVerticalDragGestures
                        }
                        println("222222222222222222222")
                        tfValue.value -= dragAmount.times(0.02f)
                    },
                )
            },
        colors = sharedViewModel.tFColors2(),
        textStyle = MaterialTheme.typography.titleMedium,
        shape = RoundedCornerShape(20.dp),
    )
}

@Preview
@Composable
fun ContractUserPreview() {
//    ContractUser(} )
    SwipeItem()
}