package com.example.basicathentication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.basicathentication.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AnalyticsActivity : AppCompatActivity() {

    private val INCOME_AMOUNT = stringPreferencesKey("income_amount")
    private val EXPENSE_AMOUNT = stringPreferencesKey("expense_amount")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        val incomeText = findViewById<TextView>(R.id.incomeText)
        val expenseText = findViewById<TextView>(R.id.expenseText)
        val balanceText = findViewById<TextView>(R.id.balanceText)
        val reviewButton = findViewById<Button>(R.id.reviewDataButton)
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val setting = findViewById<ImageView>(R.id.setting)

        runBlocking {
            val prefs = applicationContext.dataStore.data.first()
            val income = prefs[INCOME_AMOUNT]?.toDoubleOrNull() ?: 0.0
            val expense = prefs[EXPENSE_AMOUNT]?.toDoubleOrNull() ?: 0.0
            val balance = income - expense

            incomeText.text = "Total Income: $income"
            expenseText.text = "Total Expense: $expense"
            balanceText.text = "Balance: $balance"
        }

        setting.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        reviewButton.setOnClickListener {
            startActivity(Intent(this, ReviewDataActivity::class.java))
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_analytics

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    true
                }
                R.id.nav_income -> {
                    startActivity(Intent(this, AddIncomeActivity::class.java))
                    true
                }
                R.id.nav_expense -> {
                    startActivity(Intent(this, AddExpenseActivity::class.java))
                    true
                }
                R.id.nav_budget -> {
                    startActivity(Intent(this, BudgetActivity::class.java))
                    true
                }
                R.id.nav_analytics -> {
                    // Already on AnalyticsActivity
                    true
                }
                else -> false
            }
        }
    }
}
