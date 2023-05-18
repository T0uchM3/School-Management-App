package com.example.schoolmanagementsystem.script

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var user by mutableStateOf<User?>(null)
        private set

    fun defineUser(newUser: User) {
        user = newUser
    }

    //    var userList by mutableStateListOf<List<User>?>(null)
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

    var contract by mutableStateOf<Contract?>(null)
        private set

    fun defineContract(newContract: Contract) {
        contract = newContract
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


//    var textFieldColor: TextFieldColors = OutlinedTextFieldDefaults.colors(
//        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
//        focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
//        unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
//        cursorColor = MaterialTheme.colorScheme.onPrimary,
//    )
}