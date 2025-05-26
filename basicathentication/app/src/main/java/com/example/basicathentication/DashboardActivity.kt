package com.example.basicathentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.example.basicathentication.dataStore

class DashboardActivity : AppCompatActivity() {

    private val FIRST_NAME = stringPreferencesKey("first_name")
    private val BUDGET_AMOUNT = stringPreferencesKey("budget_amount")
    private val INCOME_AMOUNT = stringPreferencesKey("income_amount")
    private val EXPENSE_AMOUNT = stringPreferencesKey("expense_amount")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val summaryText = findViewById<TextView>(R.id.summaryText)
        val reviewButton = findViewById<Button>(R.id.reviewDataButton)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val setting = findViewById<ImageView>(R.id.settingicon)

        runBlocking {
            val prefs = applicationContext.dataStore.data.first()

            val name = prefs[FIRST_NAME] ?: "User"
            val budget = prefs[BUDGET_AMOUNT]?.toIntOrNull() ?: 0
            val income = prefs[INCOME_AMOUNT]?.toIntOrNull() ?: 0
            val expense = prefs[EXPENSE_AMOUNT]?.toIntOrNull() ?: 0
            val totalFunds = budget + income
            val remaining = totalFunds - expense

            welcomeText.text = "Welcome, $name 👋"

            val summary = """
                Budget: $$budget
                Income: $$income
                Expense: $$expense
                -------------------------
                Remaining Balance: $$remaining
            """.trimIndent()

            summaryText.text = summary

            val message = when {
                remaining > (totalFunds * 0.4) -> "🎯 Great job! You're managing your budget well."
                remaining in 1..(totalFunds * 0.4).toInt() -> "⚠️ You're doing okay, but watch your spending."
                else -> "🚨 You're over budget! Consider reducing expenses."
            }

            Toast.makeText(this@DashboardActivity, message, Toast.LENGTH_LONG).show()
        }

        reviewButton.setOnClickListener {
            startActivity(Intent(this, ReviewDataActivity::class.java))
        }
        setting.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Handle Bottom Navigation
        bottomNav.selectedItemId = R.id.nav_dashboard // highlight dashboard
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> true // already here
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
                    startActivity(Intent(this, AnalyticsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
