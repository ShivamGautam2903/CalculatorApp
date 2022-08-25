package com.example.calculatordemo.Database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CalcHistory::class], version = 1)
abstract class CalculationDatabase: RoomDatabase() {
    abstract fun calculationDao() : CalculationDAO
}