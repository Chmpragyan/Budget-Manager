package com.example.budgetwise.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.budgetwise.data.local.model.Transfer

@Dao
interface TransferDao {

    @Insert
    fun insertTransfer(transfer: Transfer)

}