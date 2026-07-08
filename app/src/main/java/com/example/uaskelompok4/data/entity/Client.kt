package com.example.uaskelompok4.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val picName: String = "",
    val phone: String = "",
    val email: String = ""
)
