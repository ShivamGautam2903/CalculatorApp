package com.example.calculatordemo.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class CalcHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val calc: String
)
