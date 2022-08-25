package com.example.calculatordemo.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CalculationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalc(calculation: CalcHistory)

    @Delete
    suspend fun deleteCalc(calculation: CalcHistory)

    @Query("SELECT * FROM calculations ORDER BY id DESC LIMIT 10")
    fun getAll(): LiveData<List<CalcHistory>>
}
