package com.example.schoolmanagementsystem.script

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class Student(
    var id: Long = -1,
    var parent: String? = "",
    var groupe_id: String? = "",
    var user_id: String? = "",
    var user: User? = null,
    var groupe: Group? = null,
    var remarque: String? = null,

    var name: String? = "",
    var email: String? = "",
    var cin: String? = "",
    var sex: String? = "",
    var date_naiss: String? = "",
    var adresse: String? = "",
    var tel: String? = "",
    var photo: String? = "",
    var password: String? = "",


    var created_at: String? = "",
    var updated_at: String? = "",
    var deleted_at: String? = "",
) : Parcelable

@Parcelize
class Group(
    var id: Long = -1,
    var name: String? = "",
    var niveau_id: String? = "",
    var created_at: String? = "",
    var updated_at: String? = "",
    var deleted_at: String? = "",
) : Parcelable

@Parcelize
class Niveau(
    var id: Long = -1,
    var name: String? = "",
    var created_at: String? = "",
    var updated_at: String? = "",
    var deleted_at: String? = "",
) : Parcelable
