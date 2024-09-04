package com.example.budgetwise.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.budgetwise.dao.AccountTypeDao
import com.example.budgetwise.dao.ExpenseDao
import com.example.budgetwise.dao.IncomeCategoryDao
import com.example.budgetwise.dao.IncomeDao
import com.example.budgetwise.dao.TransferDao
import com.example.budgetwise.database.BudgetDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL("ALTER TABLE income_table ADD COLUMN newColumn INTEGER DEFAULT 0 NOT NULL")


            db.execSQL("ALTER TABLE income_table ALTER COLUMN amount INTEGER")

        }
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context
    ): BudgetDb {
        return Room.databaseBuilder(
            app,
            BudgetDb::class.java,
            "budget_db"
        ).addMigrations(MIGRATION_2_3).build()
    }

    @Provides
    @Singleton
    fun provideIncomeDao(database: BudgetDb): IncomeDao {
        return database.getIncome()
    }

    @Provides
    @Singleton
    fun provideIncomeCategoryDao(database: BudgetDb): IncomeCategoryDao {
        return database.getIncomeCategory()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(database: BudgetDb): ExpenseDao {
        return database.getExpense()
    }

    @Provides
    @Singleton
    fun provideTransferDao(database: BudgetDb): TransferDao {
        return database.getTransfer()
    }

    @Provides
    @Singleton
    fun provideAccountTypeDao(database: BudgetDb): AccountTypeDao {
        return database.getAccountType()
    }
}
