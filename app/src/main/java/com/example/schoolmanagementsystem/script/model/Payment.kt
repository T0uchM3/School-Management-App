package com.example.schoolmanagementsystem.script.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Payment(
    var id: Long = -1,
    var montant: String? = "",
    var mois: String? = "",
    var date: String? = "",
    var type: String? = "",
    var ref: String? = "",
    var contrat_id: String? = "",
    var created_at: String? = "",
    var updated_at: String? = "",
    var deleted_at: String? = "",
    var contrat: Contract? = null,
) : Parcelable

