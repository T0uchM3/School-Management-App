package com.example.schoolmanagementsystem.script.homeTab

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.twotone.ArrowBack
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.script.SharedViewModel
import com.example.schoolmanagementsystem.script.TextField
import com.example.schoolmanagementsystem.script.addGroup
import com.example.schoolmanagementsystem.script.addNiveau
import com.example.schoolmanagementsystem.script.deleteGroup
import com.example.schoolmanagementsystem.script.deleteNiveau
import com.example.schoolmanagementsystem.script.getGroups
import com.example.schoolmanagementsystem.script.getNiveau
import com.example.schoolmanagementsystem.script.model.Contract
import com.example.schoolmanagementsystem.script.model.Group
import com.example.schoolmanagementsystem.script.model.Niveau
import com.example.schoolmanagementsystem.script.sheetAction
import com.example.schoolmanagementsystem.script.updateGroup
import com.example.schoolmanagementsystem.script.updateNiveau
import com.example.schoolmanagementsystem.ui.theme.focusRequester
import com.example.schoolmanagementsystem.ui.theme.scope
import com.example.schoolmanagementsystem.ui.theme.sheetState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox


//var sheetAction = ""
//var contractHolder: Contract? = null
var selectedNiv = mutableStateOf(TextFieldValue(""))
var selectedNivValue = mutableStateOf("")
var niveauSelected = mutableStateOf(false)
var selectedNiveau = Niveau()
var selectedGroup = Group()

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun GroupScreen(
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
    if(sharedViewModel.user?.role == "admin" || sharedViewModel.user?.role == "teacher")
    // make sure the fab is visible in this screen
        sharedViewModel.defineFabVisible(true)
    val fabClicked = remember { mutableStateOf(false) }
    val groups = remember { SnapshotStateList<Group>() }
//    val userName = remember { mutableStateOf("") }

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
                ManageGroupSheet(value = "", scope = scope, state = sheetState, sharedViewModel)
            }
        )
    } else
        sharedViewModel.defineFABClicked(false)

    LaunchedEffect(key1 = sharedViewModel?.contractList?.size) {
        sharedViewModel.groupList.clear()
        getGroups(sharedViewModel)
        //show fab incase it's invisible
        sharedViewModel.defineFabVisible(true)
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

        sharedViewModel.groupList.clear()
        getGroups(sharedViewModel = sharedViewModel)
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
            // top row that contains back arrow
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
                    text = "Groups",
                    color = Color(0xCCFFFFFF),
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 10.dp)

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
                if (sharedViewModel.groupList.isEmpty()) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No groups!",
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
                            sharedViewModel.groupList.sortedByDescending { it.created_at },
                            key = { item -> item.id }) { group ->
                            Box(modifier = Modifier.animateItemPlacement()) {
                                if (sharedViewModel.user?.role == "admin" || sharedViewModel.user?.role == "teacher")
                                    SwipeableGroupBox(navCtr,
                                        Modifier.padding(),
                                        sharedViewModel,
                                        group,
                                        onRemoveClicked = {
                                            sharedViewModel.groupList.remove(group)
                                        })
                                else
                                    Card(
                                        Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)

                                    ) {
                                        SwipeItemGroup(
                                            sharedViewModel,
                                            navCtr = navCtr,
                                            group = group,
                                        )
                                    }
                            }
//                            contractHolder = contract
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableGroupBox(
    navCtr: NavHostController?,
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    group: Group,
    onRemoveClicked: (group: Group) -> Unit
) {
    var isValid by rememberSaveable { mutableStateOf(true) }
    val isArchived by rememberSaveable { mutableStateOf(false) }
    var showEditDialog = remember { mutableStateOf(false) }

    var isActive by rememberSaveable { mutableStateOf(false) }
    val editContract = SwipeAction(
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
            selectedGroup = group
            sheetAction = "edit"
            scope?.launch {
                sheetState?.show()
            }

        },
        isUndo = false,
    )

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
            deleteGroup(group.id.toInt())
            onRemoveClicked(group)

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
            endActions = listOf(remove),
            swipeThreshold = 40.dp,
            backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp),
        ) {
            SwipeItemGroup(
                sharedViewModel, navCtr = navCtr, group = group
            )
        }
    }
}


@Composable
private fun SwipeItemGroup(
    sharedViewModel: SharedViewModel? = null,
    navCtr: NavHostController? = null,
    group: Group? = null,
) {
    /**
    this come above the card
     */
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clickable(
                onClick = {

                })
            .background(Color.White), verticalAlignment = Alignment.CenterVertically

    ) {
        /**
        setting the left vertical blue marker
         */
        if (sharedViewModel?.user?.role == "admin")
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


        Row(
            Modifier
//                .fillMaxWidth()
//                .background(Color.Green)
                .weight(0.25f)
                .padding(top = 5.dp)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(Modifier.weight(0.4f)) {
                Text(
                    text = "Group: ",
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier
                )
                Text(
                    text = "${group?.name} ",
                    color = Color.Black,
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Row(Modifier.weight(0.4f), Arrangement.End) {
                Text(
                    text = "Niveau: ",
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier
                )
                var nivName = ""
                sharedViewModel?.niveauList?.forEach { niveau ->
                    if (niveau.id.toString() == group?.niveau_id) nivName = niveau.name!!
                }
                Text(
                    text = nivName,
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier
                )
            }
        }


        /**
        setting the right vertical red marker
         */
//        if (sharedViewModel?.user?.role == "admin")
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
fun ManageGroupSheet(
    value: String? = null,
    scope: CoroutineScope? = null,
    state: SheetState? = null,
    sharedViewModel: SharedViewModel? = null
) {
    val nameInput: MutableState<TextFieldValue>?
    nameInput = remember {
        mutableStateOf(TextFieldValue(""))
    }
    LaunchedEffect(key1 = Unit) {
        // Bring back the FAB
        sharedViewModel?.defineFABClicked(true)
        if (sheetAction == "edit") {
            niveauSelected.value = true
            nameInput.value = TextFieldValue(selectedGroup.name.toString())
            selectedNiveau.id = selectedGroup.niveau_id!!.toLong()
        }
    }


//    if (sheetAction == "edit") {
//        nameInput.value = TextFieldValue(selectedGroup.name.toString())
//    }


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
                            text =  stringResource(
                                R.string.cancel), fontSize = 17.sp, color = Color.Red,
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = if (sheetAction == "add") stringResource(R.string.new_group) else stringResource(
                                                    R.string.edit_group),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        color = Color.DarkGray
                    )

                    Spacer(Modifier.weight(1f))
                    Button(
                        enabled = niveauSelected.value && nameInput.value.text.isNotEmpty(),

                        onClick = {
                            val contract = Contract()
                            var group = Group()
                            if (sheetAction == "edit")
                                group = selectedGroup
                            group.name = nameInput.value.text
                            group.niveau_id = selectedNiveau.id.toString()

                            //api call
                            if (sheetAction == "edit")
                                updateGroup(group.id.toInt(), group, sharedViewModel)
                            else
                                addGroup(group, sharedViewModel, true)

                            sharedViewModel.defineFABClicked(false)
                            scope?.launch { state?.hide() }


                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Gray,
                            contentColor = MaterialTheme.colorScheme.inverseSurface
                        ),
                    ) {
                        Text(
                            text = if (sheetAction == "add")  stringResource(
                                R.string.create) else  stringResource(
                                R.string.update),
                            fontSize = 17.sp,
                        )
                    }
                }
                TextField(sharedViewModel = sharedViewModel, nameInput, stringResource(R.string.group_name))
                NiveauUI(sharedViewModel)
            }

        }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NiveauUI(sharedViewModel: SharedViewModel) {
    var selectedIndex by remember { mutableStateOf(0) }
    val onItemClick = { index: Int -> selectedIndex = index }

    LaunchedEffect(key1 = Unit) {
        sharedViewModel.niveauList.clear()
        getNiveau(sharedViewModel)
        if (sheetAction == "edit") {
            onItemClick.invoke(selectedGroup.niveau_id!!.toInt())
        }
    }
    var editIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxWidth()
            .height(200.dp)
            .background(shape = RoundedCornerShape(20.dp), color = Color(0xA4E9E9E9))

    ) {
        if (sharedViewModel.niveauList.isEmpty())
            Text(
                text = stringResource(R.string.select_a_niveau_to_add_a_group),
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )
        else
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
                    .padding(bottom = 8.dp, top = 2.dp)
                    .padding(horizontal = 7.dp)
            ) {
                item(key = "0") {
                    Spacer(Modifier.height(5.dp)) // To add some padding on top of the list
                }
                items(sharedViewModel.niveauList, key = { item -> item.id }) { niv ->
                    var rivTfValue = remember {
                        mutableStateOf(TextFieldValue(niv.name.toString()))
                    }

                    rivTfValue.value = TextFieldValue(niv.name.toString())
//                    nivv.value = niv
                    Spacer(Modifier.height(8.dp))
                    Box(modifier = Modifier.animateItemPlacement()) {
                        NiveauItem(
                            selected = selectedIndex == niv.id.toInt(),
                            onClick = onItemClick,
                            sharedViewModel = sharedViewModel,
                            tfvalue = rivTfValue,
                            niveau = niv,
                            enabled = editIndex == niv.id.toInt(),
                            onEdit = { index: Int ->
                                editIndex = index
                            },
                            onDelete = {
                                sharedViewModel.niveauList.remove(niv)
                            },
                        )

                    }
                    Spacer(Modifier.height(8.dp))
                    Divider()
                }
            }

    }
    Row(
        Modifier
            .fillMaxWidth()
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val focusManager = LocalFocusManager.current
        androidx.compose.material3.TextField(
            value = selectedNiv.value,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {
                selectedNiv.value = it
            },
            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painterResource(id = R.drawable.m),
                        contentDescription = "",
                        tint = if (selectedNiv.value.text.isNotEmpty()) MaterialTheme.colorScheme.inverseSurface else Color.LightGray,
                        modifier = Modifier
                            .size(40.dp)
                            .scale(1f)
                            .clickable(onClick = {
                                addNiveau(
                                    selectedNiv.value.text,
                                    sharedViewModel, true
                                )
                                selectedNiv.value = TextFieldValue("")
                                focusManager.clearFocus()
                            })
                    )
                }
            },
            label = { Text(stringResource(R.string.new_niveau_enter_name_here), fontSize = 14.sp) },
            interactionSource = interactionSource,
            modifier = Modifier
//                    .background(Color.Red)
                .fillMaxWidth()
                .focusRequester(focusRequester),
            colors = sharedViewModel.tFColors2(),
            textStyle = MaterialTheme.typography.titleMedium,
            shape = RoundedCornerShape(20.dp),
        )
    }
}

@Composable
fun NiveauItem(
    sharedViewModel: SharedViewModel,
    niveau: Niveau,
    tfvalue: MutableState<TextFieldValue>,
    selected: Boolean,
    enabled: Boolean,
    onEdit: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    onClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                if (selected) {     // Deselect
                    onClick.invoke(-1)
                    niveauSelected.value = false
                    selectedNivValue.value = ""
                } else {        // Select
                    onClick.invoke(niveau.id.toInt())
                    selectedNiveau = niveau
                    niveauSelected.value = true
                    selectedNivValue.value = niveau.name.toString()
                }

            }
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
//            .background(Color.Green)
            .animateContentSize(),
        Arrangement.Center,
        Alignment.CenterVertically

    ) {
        // Setting up the middle texts
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp)
                .weight(0.8f)
//                .background(Color.Blue)
                .fillMaxHeight(),
            Arrangement.Center

        ) {
            BasicTextField(
                value = tfvalue.value,
                onValueChange = { tfvalue.value = it },
                enabled = enabled
            )

        }
        Row(
            Modifier
                .padding(start = 10.dp)
                .weight(0.4f)
                .fillMaxHeight(), Arrangement.Center,
            Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = R.drawable.j),
                contentDescription = stringResource(R.string.edit),
                tint = if (!enabled) MaterialTheme.colorScheme.inverseSurface else Color(0xFF0DB900),
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(30.dp)
                    .scale(1f)

                    .clickable {
                        if (enabled) {     // Deselect
                            onEdit.invoke(-1)
                            niveau.name = tfvalue.value.text
                            updateNiveau(niveau.id.toInt(), niveau, sharedViewModel)
                        } else {        // Select
                            onEdit.invoke(niveau.id.toInt())
                        }
                    }
            )
            Icon(
                painterResource(id = R.drawable.f),
                contentDescription = "delete",
                tint = Color(0xFFFF6565),
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(30.dp)
                    .scale(1f)
                    .clickable {
                        onDelete(niveau.id.toInt())
                        deleteNiveau(niveau.id.toInt())
                    }
            )
            // Checkbox image for indicating of user is selected or not
            Icon(
                painterResource(id = R.drawable.x),
                contentDescription = "selected",
                tint = if (selected) MaterialTheme.colorScheme.inverseSurface else Color.LightGray,
                modifier = Modifier
                    .size(30.dp)
                    .scale(1f)
            )
        }
    }
}