package com.example.schoolmanagementsystem.script

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var user by mutableStateOf<User?>(null)
        private set

    fun defineUser(newUser: User) {
        user = newUser
    }
    var userList by mutableStateOf<List<User>?>(null)
        private set
    fun defineUserList(newList: List<User>?){
        userList = newList
    }
    var usersFocus by mutableStateOf<Boolean>(false)
        private set

    fun defineUsersFocus(newState: Boolean) {
        usersFocus = newState
    }
}