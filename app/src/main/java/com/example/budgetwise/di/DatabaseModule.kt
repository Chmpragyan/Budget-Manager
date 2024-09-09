package com.example.budgetwise.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.budgetwise.dao.AccountTypeDao
import com.example.budgetwise.dao.ExpenseCategoryDao
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

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Step 1: Create a new table with the correct schema, ensuring the 'id' column is NOT NULL
            db.execSQL(
                """
            CREATE TABLE expense_table_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                date INTEGER NOT NULL,
                amount REAL NOT NULL,
                expCategoryId INTEGER NOT NULL,
                accountId INTEGER NOT NULL,
                note TEXT NOT NULL,
                FOREIGN KEY (expCategoryId) REFERENCES expense_category(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                FOREIGN KEY (accountId) REFERENCES account_table(id) ON DELETE NO ACTION ON UPDATE NO ACTION
            )
            """.trimIndent()
            )

            // Step 2: Copy data from the old table to the new table
            db.execSQL(
                """
            INSERT INTO expense_table_new (id, date, amount, expCategoryId, accountId, note)
            SELECT id, date, amount, expCategoryId, accountId, note
            FROM expense_table
            """.trimIndent()
            )

            // Step 3: Drop the old table
            db.execSQL("DROP TABLE expense_table")

            // Step 4: Rename the new table to the original table name
            db.execSQL("ALTER TABLE expense_table_new RENAME TO expense_table")
        }
    }


    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context
    ): BudgetDb {
        return Room.databaseBuilder(
            app,
            BudgetDb::class.java, "budgetwise-db"
        ).addMigrations(MIGRATION_3_4)
            .build()
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
    fun provideExpenseCategoryDao(database: BudgetDb): ExpenseCategoryDao {
        return database.getExpenseCategory()
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
