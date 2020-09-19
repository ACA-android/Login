package com.dm.login

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val login: String,
    val password: String
) : Parcelable