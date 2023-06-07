package com.example.schoolmanagementsystem.script

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.schoolmanagementsystem.BuildConfig
import com.example.schoolmanagementsystem.R
import com.example.schoolmanagementsystem.ui.theme.clearSearch
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar


var selectedUser: User? = null
var nameInput: MutableState<TextFieldValue>? = null
var cinInput: MutableState<TextFieldValue>? = null
var dnInput: MutableState<TextFieldValue>? = null
var emailInput: MutableState<TextFieldValue>? = null
var passwordInput: MutableState<TextFieldValue>? = null
var positionInput: MutableState<TextFieldValue>? = null
var ribInput: MutableState<TextFieldValue>? = null
var phoneInput: MutableState<TextFieldValue>? = null
var addressInput: MutableState<TextFieldValue>? = null

// This for the photo that get selected from gallery
var selectedPhoto: MutableState<File>? = null

// This for the photo that get retrieved from server
var userPhoto: MutableState<String>? = null

var photoIsSelected = mutableStateOf(false)

var selectedSex: MutableState<Int>? = null
var selectedRole: MutableState<Int>? = null

var year = mutableStateOf(Calendar.getInstance().get(Calendar.YEAR))
var month = mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
var day = mutableStateOf(Calendar.getInstance().get(Calendar.MONTH))

var bank = mutableStateOf(8)
var weGotPermission = mutableStateOf(false)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
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
        if (user.id.toString() == sharedViewModel.selectedUserId) selectedUser = user
    }
//    println(sharedViewModel.isNewUser.toString() + "  USER ID!::: " + selectedUser?.id)

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
    passwordInput = remember {
        mutableStateOf(TextFieldValue(selectedUser?.password.toString()))
    }
    positionInput = remember {
        mutableStateOf(TextFieldValue(selectedUser?.poste.toString()))
    }
    ribInput = remember {
        mutableStateOf(TextFieldValue(selectedUser?.rib.toString()))
    }
    phoneInput = remember {
        mutableStateOf(TextFieldValue(selectedUser?.tel.toString()))
    }
    addressInput = remember {
        mutableStateOf(TextFieldValue(selectedUser?.adresse.toString()))
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
    if (selectedUser?.date_naiss != null) {
        // Progressive app?!
        selectedSex = remember {
            mutableStateOf(if (selectedUser?.sex == "man") 1 else if (selectedUser?.sex == "woman") 2 else -1)
        }
        selectedRole = remember {
            mutableStateOf(if (selectedUser?.role == "staff") 1 else if (selectedUser?.role == "teacher") 2 else 3)
        }
        //format 2023-05-27
        year = remember { mutableStateOf(selectedUser?.date_naiss!!.substring(0, 4).toInt()) }
        month = remember { mutableStateOf(selectedUser?.date_naiss!!.substring(5, 7).toInt() - 1) }
        day = remember { mutableStateOf(selectedUser?.date_naiss!!.substring(8, 10).toInt()) }

        bank = remember { mutableStateOf(bankNames.indexOf(selectedUser?.bank)) }


    }

    LaunchedEffect(key1 = Unit) {
        sharedViewModel.defineFABClicked(true)
        if (sharedViewModel.isNewUser) {
            //this will get recomposed, so better to trigger it once per fap pres
//            sharedViewModel.defineIsNewUser(false)
            resetAllFields()
        }
    }


    Column(
        modifier = Modifier
//            .height(200.dp)

            .padding(horizontal = 15.dp)

    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    sharedViewModel.defineFABClicked(false)
                    scope?.launch { state?.hide() }
                },
                border = BorderStroke(1.dp, Color.Transparent),
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 17.sp,
                    color = Color.Red,
//                    style = MaterialTheme.typography.titleSmall
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = "New User",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                color = Color.DarkGray
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    val user = User()

                    //TODO: prevent create button from being clicked of things aren't selected
                    user.name = nameInput!!.value.text
                    user.cin = cinInput!!.value.text
//                    user.date_naiss = dnInput!!.value.text
                    user.date_naiss = "${year.value}-${month.value + 1}-${day.value}"
//                    println("BBBBIRTTHHTHTHTHT   " + user.date_naiss)
                    user.email = emailInput!!.value.text
                    user.sex =
                        if (selectedSex!!.value == 1) "man" else if (selectedSex!!.value == 2) "woman" else null
                    user.bank = bankNames.elementAt(bank.value)
                    user.tel = phoneInput!!.value.text
                    user.rib = ribInput!!.value.text
                    user.poste = positionInput!!.value.text
                    user.adresse = addressInput!!.value.text
                    user.role =
                        if (selectedRole!!.value == 1) "staff" else if (selectedRole!!.value == 2) "teacher" else "admin"
                    //clearing lists to avoid lazycol parsing error
                    sharedViewModel.userList.clear()
                    sharedViewModel.contractList.clear()

                    // Adding new user
                    if (sharedViewModel.isNewUser) {
                        clearSearch(sharedViewModel, focusManager!!)
//                        addUserAPI(user, sharedViewModel, true)
                        addUserAPI(user, sharedViewModel, selectedPhoto, true)
                        sharedViewModel.defineFABClicked(false)
                        scope?.launch { state?.hide() }
                    }
                    // Updating user
                    else {
                        clearSearch(sharedViewModel, focusManager!!)
                        updateUser(
                            sharedViewModel.selectedUserId.toInt(),
                            user,
                            selectedPhoto,
                            sharedViewModel,
                            true
                        )
                        sharedViewModel.defineFABClicked(false)
                        scope?.launch { state?.hide() }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Gray,
                    contentColor = MaterialTheme.colorScheme.inverseSurface
                ),
                enabled = !(selectedRole!!.value == -1 || selectedSex!!.value == -1 || nameInput!!.value.text.isEmpty() ||
                        emailInput!!.value.text.isEmpty() || bankNames.elementAt(bank.value) == "Choose a bank")
            ) {
                if (sharedViewModel.isNewUser) Text(
                    text = "Create",
                    fontSize = 17.sp,
//                    color = Color(0xFFF1F1F1)
//                    color = MaterialTheme.colorScheme.inverseSurface
                )
                else Text(
                    text = "Update",
                    fontSize = 17.sp,
//                    color = MaterialTheme.colorScheme.inverseSurface
                )
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())

//                .padding(horizontal = 20.dp)
        ) {
            //user's photo
            ImageSelection(LocalContext.current, userPhoto!!)

//            RequestContentPermission()

            TextField(sharedViewModel = sharedViewModel, nameInput!!, "name")
            TextField(sharedViewModel = sharedViewModel, cinInput!!, "cin")
            TextField(sharedViewModel = sharedViewModel, emailInput!!, "email")
            TextField(sharedViewModel = sharedViewModel, passwordInput!!, "password")

            Text(
                text = "Role",
                modifier = Modifier.padding(
                    top = 5.dp,
                    start = 15.dp,
                ),
                style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
            )
            SegmentedButtonsRole(selectedRole!!)
            TextField(sharedViewModel = sharedViewModel, positionInput!!, "poste")

            Text(
                text = "Date of birth",
                modifier = Modifier.padding(
                    top = 5.dp,
                    start = 15.dp,
                ),
                style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
            )
            DatePickerUI(sharedViewModel)

            TextField(sharedViewModel = sharedViewModel, addressInput!!, "address")
//            TextField(sharedViewModel = sharedViewModel, emailInput!!, "bank")

            Text(
                text = "Gender",
                modifier = Modifier.padding(top = 5.dp, start = 15.dp),
                style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
            )
            SegmentedButtons(selectedSex!!)
            TextField(sharedViewModel = sharedViewModel, ribInput!!, "rib")
            TextField(sharedViewModel = sharedViewModel, phoneInput!!, "phone")
            Text(
                text = "Bank",
                modifier = Modifier.padding(top = 5.dp, start = 15.dp),
                style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
            )
            BanksScroll(
                onBankChosen = { bank.value = bankNames.indexOf(it) },
                isNewUser = sharedViewModel.isNewUser
            )

        }
    }
}

fun resetAllFields() {
    //resets all fields
    nameInput?.value = TextFieldValue("suzume")
    cinInput?.value = TextFieldValue("010010110")
    dnInput?.value = TextFieldValue("")
    emailInput?.value = TextFieldValue("suzume@gmail.com")
    phoneInput?.value = TextFieldValue("21547856")
    passwordInput?.value = TextFieldValue("fuckthishit")
    addressInput?.value = TextFieldValue("sesemee street")
    ribInput?.value = TextFieldValue("kil")
    positionInput?.value = TextFieldValue("position")
//    selectedBank?
    selectedSex?.value = -1
    selectedRole?.value = -1
    userPhoto?.value = ""
}

@Composable
fun TextField(
    sharedViewModel: SharedViewModel,
    tfValue: MutableState<TextFieldValue>,
    labelText: String
) {
    val IndicatorUnfocusedWidth = 1.dp
    val IndicatorFocusedWidth = 2.dp
    val TextFieldPadding = 16.dp
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val indicatorColor = if (isFocused) Color(0xff4774a9) else Color.Gray
    val indicatorWidth = if (isFocused) IndicatorFocusedWidth else IndicatorUnfocusedWidth
    var text = remember { mutableStateOf("") }

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
    TextField(
        value = tfValue.value,
        visualTransformation = if (passwordVisible.value || labelText != "password") VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = {
            tfValue.value = it
        },
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
        modifier = mod.fillMaxWidth(),
        colors = sharedViewModel.tFColors2(),
        textStyle = MaterialTheme.typography.titleMedium,
        shape = RoundedCornerShape(20.dp),
    )
}

@Composable
fun SegmentedButtons(selectedButton: MutableState<Int>) {
//    var selectedButton = remember { mutableStateOf(-1) }
    Row(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 2.dp)
    ) {
        OutlinedButton(
            onClick = { selectedButton.value = 1 },
            shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp),
            border = if (selectedButton.value == 1) BorderStroke(1.dp, Color.Transparent)
            else BorderStroke(1.dp, Color.Gray),
            colors = if (selectedButton.value == 1) ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xCC4884C9), contentColor = Color.White
            )
            else ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent, contentColor = Color(0xFF727272)
            ),
            modifier = Modifier
                .background(Color.Transparent)
                .weight(1f)
        ) {
            Text(
                "Man",
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                fontSize = 16.sp,
            )
        }
        OutlinedButton(
            onClick = { selectedButton.value = 2 },
            shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
            border = if (selectedButton.value == 2) BorderStroke(1.dp, Color.Transparent)
            else BorderStroke(1.dp, Color.Gray),
            colors = if (selectedButton.value == 2) ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xCC4884C9), contentColor = Color.White
            )
            else ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent, contentColor = Color(0xFF727272)
            ),
            modifier = Modifier.weight(1f)

        ) {
            Text(
                "Woman",
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                fontSize = 16.sp
//                style = MaterialTheme.typography.titleMedium.copy(
////                    color = Color.DarkGray
//                )
            )
        }
    }
}

@Composable
fun SegmentedButtonsRole(selectedRoleIndex: MutableState<Int>) {
//    var selectedButton = remember { mutableStateOf(-1) }
    Row(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 2.dp)
    ) {
        OutlinedButton(
            onClick = { selectedRoleIndex.value = 1 },
            shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp),
            border = if (selectedRoleIndex.value == 1) BorderStroke(1.dp, Color.Transparent)
            else BorderStroke(1.dp, Color.Gray),
            colors = if (selectedRoleIndex.value == 1) ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xCC4884C9), contentColor = Color.White
            )
            else ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent, contentColor = Color(0xFF727272)
            ),
            modifier = Modifier
                .background(Color.Transparent)
                .weight(1f)
        ) {
            Text(
                "Staff",
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                fontSize = 15.sp,
            )
        }
        OutlinedButton(
            onClick = { selectedRoleIndex.value = 2 },
            shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp),
            border = if (selectedRoleIndex.value == 2) BorderStroke(1.dp, Color.Transparent)
            else BorderStroke(1.dp, Color.Gray),
            colors = if (selectedRoleIndex.value == 2) ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xCC4884C9), contentColor = Color.White
            )
            else ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent, contentColor = Color(0xFF727272)
            ),
            modifier = Modifier
                .background(Color.Transparent)
                .weight(1f)
        ) {
            Text(
                "Teacher",
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                fontSize = 15.sp,
            )
        }
        OutlinedButton(
            onClick = { selectedRoleIndex.value = 3 },
            shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
            border = if (selectedRoleIndex.value == 3) BorderStroke(1.dp, Color.Transparent)
            else BorderStroke(1.dp, Color.Gray),
            colors = if (selectedRoleIndex.value == 3) ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xCC4884C9), contentColor = Color.White
            )
            else ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent, contentColor = Color(0xFF727272)
            ),
            modifier = Modifier.weight(1f)

        ) {
            Text(
                "Admin",
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                fontSize = 15.sp
//                style = MaterialTheme.typography.titleMedium.copy(
////                    color = Color.DarkGray
//                )
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Preview55() {
//    ManageUser(TODO(), TODO(), focusManger = focusManager)
//    DatePickerUI("zaaa")
//    SegmentedButtons(selectedSex)
}


@Composable
fun DatePickerUI(
    sharedViewModel: SharedViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 5.dp)
            .background(Color.Transparent)
    ) {

        DateSelectionSection(
            onYearChosen = { year.value = it.toInt() },
            onMonthChosen = { month.value = monthsNames.indexOf(it) },
            onDayChosen = { day.value = it.toInt() },
            isNewUser = sharedViewModel.isNewUser
        )
    }
}

@Composable
fun DateSelectionSection(
    onYearChosen: (String) -> Unit,
    onMonthChosen: (String) -> Unit,
    onDayChosen: (String) -> Unit,
    isNewUser: (Boolean)
) {
    Box(
        modifier = Modifier, contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(35.dp)
                .padding(horizontal = 10.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(Color(0x8CBDBDBD))
        ) {

        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier

                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            InfiniteItemsPicker(
                modifier = Modifier,
                items = days,
                firstIndex = if (!isNewUser) day.value - 2
                else Int.MAX_VALUE / 2 + (Calendar.getInstance()
                    .get(Calendar.DAY_OF_MONTH) - 2),
                onItemSelected = onDayChosen,
                isForBank = false

            )

            InfiniteItemsPicker(
                items = monthsNames,
                firstIndex = if (!isNewUser) Int.MAX_VALUE / 2 - 4 + month.value
                else Int.MAX_VALUE / 2 - 4 + Calendar.getInstance().get(Calendar.MONTH),
                onItemSelected = onMonthChosen,
                isForBank = false
            )
            InfiniteItemsPicker(
                items = years,
                firstIndex = if (!isNewUser) Int.MAX_VALUE / 2 + (year.value - 1967)
                else Int.MAX_VALUE / 2 + (Calendar.getInstance().get(Calendar.YEAR) - 1967),
                onItemSelected = onYearChosen,
                isForBank = false
            )
        }
    }
}

@Composable
fun BanksScroll(
    onBankChosen: (String) -> Unit, isNewUser: (Boolean)
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        println("BBBBZAAAAAABBBBKKKKKKKKKKK  " + bank.value)
        InfiniteItemsPicker(
            items = bankNames,
            firstIndex = if (!isNewUser) Int.MAX_VALUE / 2 + bank.value + 8
            else Int.MAX_VALUE / 2 + 8,
            onItemSelected = onBankChosen,
            isForBank = true
        )
    }
    Spacer(modifier = Modifier.height(100.dp))
}

@Composable
fun InfiniteItemsPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    firstIndex: Int,
    onItemSelected: (String) -> Unit,
    isForBank: Boolean,
) {

    val listState = rememberLazyListState(firstIndex)
    val currentValue = remember { mutableStateOf("") }

    LaunchedEffect(key1 = !listState.isScrollInProgress) {
//        onItemSelected(currentValue.value)
        onItemSelected(currentValue.value)
        listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
    }
    var mod = if (isForBank) Modifier.fillMaxWidth()
    else Modifier.width(100.dp)

    Box(
        modifier = Modifier.height(90.dp)
    ) {
        LazyColumn(mod
//            Modifier
//                .width(if (isForBank) IntrinsicSize.Max else 100.dp)

            .offset(y = -5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            content = {
                items(count = Int.MAX_VALUE, itemContent = {
                    val index = it % items.size
                    if (it == listState.firstVisibleItemIndex + 1) {
                        currentValue.value = items[index]

//                        println("CURRRREEECNT VALUEEEUEUE   "+currentValue.value)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = items[index],
                        modifier = Modifier.alpha(if (it == listState.firstVisibleItemIndex + 1) 1f else 0.2f),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.inverseSurface,
                            fontSize = if (it == listState.firstVisibleItemIndex + if (isForBank) 1 else 1) 19.sp else 17.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                })
            })
    }
}


val years = (1950..2050).map { it.toString() }
val monthsNumber = (1..12).map { it.toString() }
val days = (1..31).map { it.toString() }
val monthsNames = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)
val banks = (0..27).map { it.toString() }
val bankNames = listOf(
    "Choose a bank",
    "Société Tunisienne de Banque",
    "Banque Nationale Agricole",
    "Banque de lHabitat",
    "Banque de Financement des Petites et Moyennes entreprises",
    "Banque Tunisienne de Solidarité",
    "Banque de Tunisie et des Emirats",
    "Banque Tuniso-Libyenne",
    "Tunisian Saudi Bank",
    "Banque Zitouna",
    "Al Baraka Bank",
    "Al Wifak International Bank",
    "Amenk Bank",
    "Attijari Bank",
    "Arab Tunisian Bank",
    "Arab Banking Corporation",
    "Banque Internationale Arabe de Tunisie",
    "Banque Tuniso Koweitienne",
    "Banque de Tunisie",
    "Banque Tuniso Koweitienne",
    "Banque Franco Tunisienne",
    "Citi Bank",
    "Qatar National Bank- Tunis",
    "Union Bancaire de Commerce et d’Industrie",
    "La Poste Tunisienne",
    "Union Internationale de Banque",
    "Autre",
)


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImageSelection(context: Context, userImage: MutableState<String>) {
    val (galleryImage, setGalleryImage) = remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = setGalleryImage,
        )

    val storagePermissionState = rememberPermissionState(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
    )
    var host = ""
    LaunchedEffect(key1 = Unit) {
        host =
            if (BuildConfig.DEV.toBoolean()) {
                if (Build.HARDWARE == "ranchu") "http://10.0.2.2:8000/" else "http://192.168.1.4:8000"
            } else
                BuildConfig.Host
    }
    // A check for permission to change button border color based on this value
    weGotPermission.value = storagePermissionState.status.isGranted
    val imageSelectionClicked = remember { mutableStateOf(false) }

    if (galleryImage == null) {
        photoIsSelected.value = false
        Row(
            Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent, shape = CircleShape),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {

            OutlinedButton(
                onClick = {
                    imageSelectionClicked.value = true
                },
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                border = BorderStroke(
                    1.4.dp,
                    if (weGotPermission.value) MaterialTheme.colorScheme.inverseSurface else Color.LightGray
                ),
                contentPadding = PaddingValues(0.dp),
            ) {
                if (userImage.value.isNotEmpty()) {
                    AsyncImage(
                        model =
                        // Building image from url
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = host + userImage.value)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                            }).build(),
                        contentDescription = "Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(1.4.dp, MaterialTheme.colorScheme.inverseSurface, CircleShape)
//                            .clickable(onClick = { galleryLauncher.launch("image/*") })
                    )
                } else
                    Icon(
                        painterResource(id = R.drawable.y),
                        contentDescription = "",
                        tint = if (weGotPermission.value) MaterialTheme.colorScheme.inverseSurface else Color.LightGray,
                        modifier = Modifier.padding(15.dp)
                    )
            }
        }
    } else {
        photoIsSelected.value = true

        println("image path 1 " + getFileFromUri(context, galleryImage))
        LaunchedEffect(key1 = Unit) {
            println("image path 2 " + getFileFromUri(context, galleryImage))
            selectedPhoto?.value = getFileFromUri(context, galleryImage)!!
        }


        Row(
            Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent, shape = CircleShape),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {

            AsyncImage(
                model = galleryImage,
                contentDescription = "Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(1.4.dp, MaterialTheme.colorScheme.inverseSurface, CircleShape)
                    .clickable(onClick = { galleryLauncher.launch("image/*") })
            )
        }
    }
    if (imageSelectionClicked.value) {
        imageSelectionClicked.value = false
        PermissionScreen(galleryLauncher)
    }
}


/**
 * Permission request for file access
 */
@Composable
fun PermissionScreen(galleryLauncher: ManagedActivityResultLauncher<String, Uri?>) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted
            weGotPermission.value = true

        } else {
            // Permission Denied
            weGotPermission.value = false
            return@rememberLauncherForActivityResult
        }
    }
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        // Check permission
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                weGotPermission.value = true
                // Some works that require permission
                galleryLauncher.launch("image/*")
            }

            else -> {
                // Asking for permission
                launcher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

}

/**
 * Getting the real path of the image file
 */
fun getFileFromUri(context: Context, uri: Uri?): File? {
    uri ?: return null
    uri.path ?: return null

    var newUriString = uri.toString()
    newUriString = newUriString.replace(
        "content://com.android.providers.downloads.documents/",
        "content://com.android.providers.media.documents/"
    )
    newUriString = newUriString.replace(
        "/msf%3A", "/image%3A"
    )
    val newUri = Uri.parse(newUriString)

    var realPath = String()
    val databaseUri: Uri
    val selection: String?
    val selectionArgs: Array<String>?
    if (newUri.path?.contains("/document/image:") == true) {
        databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        selection = "_id=?"
        selectionArgs = arrayOf(DocumentsContract.getDocumentId(newUri).split(":")[1])
    } else {
        databaseUri = newUri
        selection = null
        selectionArgs = null
    }
    try {
        val column = "_data"
        val projection = arrayOf(column)
        val cursor = context.contentResolver.query(
            databaseUri,
            projection,
            selection,
            selectionArgs,
            null
        )
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                realPath = cursor.getString(columnIndex)
            }
            cursor.close()
        }
    } catch (e: Exception) {
        Log.i("GetFileUri Exception:", e.message ?: "")
    }
    val path = realPath.ifEmpty {
        when {
            newUri.path?.contains("/document/raw:") == true -> newUri.path?.replace(
                "/document/raw:",
                ""
            )

            newUri.path?.contains("/document/primary:") == true -> newUri.path?.replace(
                "/document/primary:",
                "/storage/emulated/0/"
            )

            else -> return null
        }
    }
    return if (path.isNullOrEmpty()) null else File(path)
}