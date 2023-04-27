package com.example.schoolmanagementsystem.script

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    val id: Long? = null,
    val name: String? = "",
    val email: String? = "",
    val email_verified_at: String? = "",
    val cin: String? = "",
    val sex: String? = "",
    val date_naiss: String? = "",
    val adresse: String? = "",
    val tel: String? = "",
    val role: String? = "",
    val photo: String? = "",
    val poste: String? = "",
    val bank: String? = "",
    val rib: String? = "",
    val created_at: String? = "",
    val updated_at: String? = "",
) : Parcelable


@Parcelize
data class UserResults(
    @Json(name = "data")
    val results: List<User>
) : Parcelable