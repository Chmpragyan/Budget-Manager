package com.example.budgetwise.data.local.model

open class BaseTransaction(
    open val id: Int? = null,
    open val date: Long,
    open val amount: Double,
    open val account: AccountType,
    open val note: String
)