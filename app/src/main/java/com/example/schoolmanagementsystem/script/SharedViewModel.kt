package com.example.schoolmanagementsystem.script

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
    var userList =  SnapshotStateList<User>()
        private set

    fun defineUserList(newList: List<User>?) {
        if (newList != null) {
            for(user in newList)
                userList.add(user)
        }
    }

    var usersFocus by mutableStateOf<Boolean>(false)
        private set

    fun defineUsersFocus(newState: Boolean) {
        usersFocus = newState
    }
}