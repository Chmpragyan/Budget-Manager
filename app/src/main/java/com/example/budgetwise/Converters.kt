package com.example.budgetwise.data.local.converter

import androidx.room.TypeConverter
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.ExpenseCategory
import com.example.budgetwise.data.local.model.IncomeCategory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromIncomeCategory(value: IncomeCategory?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toIncomeCategory(value: String?): IncomeCategory? {
        val type = object : TypeToken<IncomeCategory>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromExpenseCategory(value: ExpenseCategory?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toExpenseCategory(value: String?): ExpenseCategory? {
        val type = object : TypeToken<ExpenseCategory>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromAccountType(value: AccountType?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toAccountType(value: String?): AccountType? {
        val type = object : TypeToken<AccountType>() {}.type
        return gson.fromJson(value, type)
    }
}
