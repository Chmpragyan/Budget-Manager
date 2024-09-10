package com.example.budgetwise.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetwise.data.local.model.IncomeCategory

@Dao
interface IncomeCategoryDao {

    @Insert
    suspend fun insertIncomeCategories(incomeCategories: List<IncomeCategory>)

    @Query("SELECT * FROM income_category")
    fun getAllIncomeCategories(): LiveData<List<IncomeCategory>>
}