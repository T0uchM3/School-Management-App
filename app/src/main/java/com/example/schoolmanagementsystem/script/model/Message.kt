package com.example.schoolmanagementsystem.script.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Message(
    var id: Long = -1,
    var to_user_id: String? = "",
    var from_user_id: String?="",
    var vu: String? = "",
    var sujet: String? = "",
    var message: String? = "",
    var created_at: String? ="",
    var from_user: User?=null,
    var to_user: User?=null
) : Parcelable


@Parcelize
class MessageToSend(
    var id: Long = -1,
    var to_user_id: List<Int>? = null,
    var from_user_id: String?="",
    var vu: String? = "",
    var sujet: String? = "",
    var message: String? = "",
    var created_at: String? ="",
    var from_user: User?=null,
    var to_user: User?=null
) : Parcelable


@Parcelize
class MessagesIds(
    var ids: List<Int>
):Parcelable
