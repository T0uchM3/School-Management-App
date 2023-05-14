package com.example.schoolmanagementsystem.script

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class Contract(
    var id: Long = -1,
    var date_debut: String? = "",
    var periode: String? = "",
    var date_fin: String? = "",
    var salaire: String? = "",
    var valide: String? = "",
    var user_id: String? = "",
    var created_at: String? = "",
    var updated_at: String? = "",
    var deleted_at: String? = "",
    var enseignant: String? = "",
    var employe: String? = "",
) : Parcelable


@Parcelize
data class ContractResults(
    @Json(name = "contracts")
    val resultsC: List<Contract>,
    @Json(name = "versements")
    val resultsP: List<Payment>
) : Parcelable