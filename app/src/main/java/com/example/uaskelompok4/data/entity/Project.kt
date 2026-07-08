package com.example.uaskelompok4.data.entity

import androidx.room.*

@Entity(
    tableName = "projects",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["clientId"])]
)
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val status: String,
    val startDate: String,
    val endDate: String?,
    val clientId: Int?
)