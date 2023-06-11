package com.example.schoolmanagementsystem.script

import android.os.Build
import android.view.Window
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var user by mutableStateOf<User?>(null)
        private set

    fun defineUser(newUser: User) {
        user = newUser
    }

    var userList = SnapshotStateList<User>()
        private set

    fun defineUserList(newList: List<User>?) {
        if (newList != null) {
            for (user in newList)
                userList.add(user)
        }
    }

    fun clearUserList() {
        userList.clear()
    }

    /**
    userFocus is used as an state holder that indicate we still in the "User" tab
    when we navigate to other inner views.
     */
    var usersFocus by mutableStateOf<Boolean>(false)
        private set

    fun defineUsersFocus(newState: Boolean) {
        usersFocus = newState
    }

    var selectedcontract by mutableStateOf<Contract?>(null)
        private set

    fun defineSelectedContract(newContract: Contract?) {
        selectedcontract = newContract
    }

    var contractList = SnapshotStateList<Contract>()
        private set

    fun defineContractList(newList: List<Contract>?) {
        if (newList != null) {
            for (contract in newList)
                contractList.add(contract)
        }
    }

    var paymentList = SnapshotStateList<Payment>()
        private set

    fun definePaymentList(newList: List<Payment>?) {
        if (newList != null) {
            for (payment in newList)
                paymentList.add(payment)
        }
    }

    var selectedUserId by mutableStateOf<String>("")
        private set

    fun defineSelectedUserId(id: String) {
        selectedUserId = id
    }

    var selectedContractId by mutableStateOf<String>("")
        private set

    fun defineSelectedContractId(id: String) {
        selectedContractId = id
    }

    /**
    isNewUser is used as an state holder that indicate we selected the add new user button,
    we use this value to make use of one view for both updating the user and add new one.
     */
    var isNewUser by mutableStateOf<Boolean>(false)
        private set

    fun defineIsNewUser(newState: Boolean) {
        isNewUser = newState
    }

    var fabVisible by mutableStateOf<Boolean>(false)
        private set

    fun defineFabVisible(newState: Boolean) {
        fabVisible = newState
    }

    var fabOnClick by mutableStateOf<() -> Unit>({})
        private set

    fun defineFabClick(newState: () -> Unit) {
        fabOnClick = newState
    }

    @Composable
    fun tFColors() =
        OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xff4774a9),
            focusedLabelColor = Color(0xff4774a9),
            unfocusedLabelColor = Color.DarkGray,
            unfocusedBorderColor = Color.DarkGray,
            cursorColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary
        )

    @Composable
    fun tFColors2() =
        OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            focusedLabelColor = Color(0xff4774a9),
            unfocusedLabelColor = Color.Gray,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary
        )


    var selectedSalary by mutableStateOf<String>("")
        private set

    fun defineSelectedSalary(salary: String) {
        selectedSalary = salary
    }

    var inLogin by mutableStateOf<Boolean>(true)
        private set

    fun defineInLogin(newState: Boolean) {
        inLogin = newState
    }

    var window by mutableStateOf<Window?>(null)
    fun setWindow2(window: Window?) {
        this.window = window
    }

    var fabClicked by mutableStateOf<Boolean?>(false)

    /**
     *The price for wanting to draw behind the system bars.
     *this will keep track whenever the fab is clicked
     *mainly for changing the "status bar" color
     *to darker tone to simulate the color change that happen
     *in the background when a dialog or bottomsheet appear.
     */
    fun defineFABClicked(value: Boolean?) {
        fabClicked = value

    }
}