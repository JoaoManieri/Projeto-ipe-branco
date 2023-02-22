package br.com.manieri.ipe_branco.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "userName") val userName : String?
)