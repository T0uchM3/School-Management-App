package com.example.schoolmanagementsystem.script

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    var id: Long = 0,
    var name: String? = "",
    var email: String? = "",
    var email_verified_at: String? = "",
    var cin: String? = "",
    var sex: String? = "",
    var date_naiss: String? = "",
    var adresse: String? = "",
    var tel: String? = "",
    var role: String? = "",
    var photo: String? = "",
    var poste: String? = "",
    var bank: String? = "",
    var rib: String? = "",
    var password: String? ="",
//    val created_at: String? = "",
//    val updated_at: String? = "",
) : Parcelable
