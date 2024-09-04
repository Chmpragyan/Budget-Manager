package com.example.budgetwise.data.local.model

open class BaseModel(
    open val id: Int? = null,
    open val date: Long,
    open val amount: Double,
    open var accountId: Int,
    open val note: String
)
