package com.example.schoolmanagementsystem.script

import android.view.Window
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.schoolmanagementsystem.script.model.Contract
import com.example.schoolmanagementsystem.script.model.Group
import com.example.schoolmanagementsystem.script.model.Message
import com.example.schoolmanagementsystem.script.model.MessagesIds
import com.example.schoolmanagementsystem.script.model.Niveau
import com.example.schoolmanagementsystem.script.model.Payment
import com.example.schoolmanagementsystem.script.model.Student
import com.example.schoolmanagementsystem.script.model.User

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


    var window by mutableStateOf<Window?>(null)
    fun setWindow2(window: Window?) {
        this.window = window
    }

    var fabClicked by mutableStateOf<Boolean?>(null)

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

    var messageList = SnapshotStateList<Message>()
        private set

    fun defineMessageList(newList: List<Message>?) {
        messageList.addAll(newList!!)
    }

    var targetedMessageList = SnapshotStateList<Message>()
        private set

    fun defineTargetedMessageList(newList: List<Message>?) {
        targetedMessageList.addAll(newList!!)
    }

    var userToMessage by mutableStateOf<User?>(null)
        private set

    fun defineUserToMsg(userToMsg: User) {
        userToMessage = userToMsg
    }

    var isOnInbox by mutableStateOf<Boolean>(false)
        private set

    fun defineIsOnBox(newState: Boolean) {
        isOnInbox = newState
    }

    var isInPayment by mutableStateOf<Boolean>(false)
        private set

    fun defineIsInPayment(newState: Boolean) {
        isInPayment = newState
    }

    var getMessageWorked by mutableStateOf<Boolean>(false)
        private set

    fun defineGetMessageWorked(newState: Boolean) {
        getMessageWorked = newState
    }
    var gettingMsg by mutableStateOf<Boolean>(false)
        private set

    fun defineGettingMsg(newState: Boolean) {
        gettingMsg = newState
    }

    // Using this to know if search bar is focused
    // To fix a bottom bar problem in MainActivity
    var searchBarFocused by mutableStateOf<Boolean>(false)
        private set

    fun defineSearchBarFocused(newState: Boolean) {
        searchBarFocused = newState
    }

//    var unseenMessageList = SnapshotStateList<Int>()
//        private set
//
//    fun defineUnseenMessageList(newList: List<Int>?) {
//        unseenMessageList.addAll(newList!!)
//    }
    var unseenMessages by mutableStateOf<MessagesIds?>(null)
        private set

    fun defineUnseenMessages(msgToUpdate: MessagesIds) {
        unseenMessages = msgToUpdate
    }


    var spinSendBtn by mutableStateOf<Boolean>(false)
        private set

    fun defineSpinSendBtn(newState: Boolean) {
        spinSendBtn = newState
    }
    var unseenMsgExist by mutableStateOf<Boolean>(false)
        private set

    fun defineUnseenMsgExist(newState: Boolean) {
        unseenMsgExist = newState
    }
    // This will be used as a refresher for the messaging api
    var waiting by mutableStateOf<Boolean>(true)
        private set

    fun defineWaiting(newState: Boolean) {
        waiting = newState
    }

    var tempMsgSearchList = SnapshotStateList<Message>()
        private set

    fun defineTempMsgSearchList(newList: List<Message>?) {
        tempMsgSearchList.addAll(newList!!)
    }

    var isRefreshing by mutableStateOf<Boolean>(false)
        private set

    fun defineIsRefreshing(newState: Boolean) {
        isRefreshing = newState
    }

    var wrongCred by mutableStateOf<Boolean>(false)
        private set
    fun defineWrongCred(newState: Boolean) {
        wrongCred = newState
    }

    var studentList = SnapshotStateList<Student>()
        private set

    fun defineStudentList(newList: List<Student>?) {
        studentList.addAll(newList!!)
    }

    var selectedStudentId by mutableStateOf<String>("")
        private set

    fun defineSelectedStudentId(id: String) {
        selectedStudentId = id
    }
    var groupList = SnapshotStateList<Group>()
        private set

    fun defineGroupList(newList: List<Group>?) {
        groupList.addAll(newList!!)
    }

    var niveauList = SnapshotStateList<Niveau>()
        private set

    fun defineNiveauList(newList: List<Niveau>?) {
        niveauList.addAll(newList!!)
    }

    var studentConnected by mutableStateOf<Student?>(null)
        private set

    fun defineStudentConnected(student: Student) {
        studentConnected = student
    }
    var notifEnabled by mutableStateOf<Boolean>(true)
        private set
    fun defineNotifEnabled(newState: Boolean) {
        notifEnabled = newState
    }

}