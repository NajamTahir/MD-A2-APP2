package com.example.basicathentication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.*
import com.example.basicathentication.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class BudgetActivity : AppCompatActivity() {

    private val BUDGET_AMOUNT = stringPreferencesKey("budget_amount")
    private val INCOME_AMOUNT = stringPreferencesKey("income_amount")
    private val EXPENSE_AMOUNT = stringPreferencesKey("expense_amount")
    private val BUDGET_CATEGORY = stringPreferencesKey("budget_category")
    private val BUDGET_DURATION = stringPreferencesKey("budget_duration")
    private val BUDGET_NOTES = stringPreferencesKey("budget_notes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        val budgetInput = findViewById<EditText>(R.id.budgetInput)
        val categoryInput = findViewById<EditText>(R.id.budgetCategoryInput)
        val durationInput = findViewById<EditText>(R.id.budgetDurationInput)
        val notesInput = findViewById<EditText>(R.id.budgetNotesInput)
        val setBudgetButton = findViewById<Button>(R.id.setBudgetButton)
        val resultText = findViewById<TextView>(R.id.budgetResultText)
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val setting = findViewById<ImageView>(R.id.setting)

        setting.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        setBudgetButton.setOnClickListener {
            val budget = budgetInput.text.toString()
            val category = categoryInput.text.toString()
            val duration = durationInput.text.toString()
            val notes = notesInput.text.toString()

            if (budget.isNotEmpty() && category.isNotEmpty() && duration.isNotEmpty()) {
                runBlocking {
                    applicationContext.dataStore.edit { prefs ->
                        prefs[BUDGET_AMOUNT] = budget
                        prefs[BUDGET_CATEGORY] = category
                        prefs[BUDGET_DURATION] = duration
                        prefs[BUDGET_NOTES] = notes
                    }
                    Toast.makeText(this@BudgetActivity, "Budget Details Saved", Toast.LENGTH_SHORT).show()
                    showBudgetSummary(resultText)
                }
            } else {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            }
        }

        runBlocking {
            showBudgetSummary(resultText)
        }

        setupBottomNavigation()
    }

    private suspend fun showBudgetSummary(resultText: TextView) {
        val prefs = applicationContext.dataStore.data.first()
        val budget = prefs[BUDGET_AMOUNT]?.toIntOrNull() ?: 0
        val income = prefs[INCOME_AMOUNT]?.toIntOrNull() ?: 0
        val expense = prefs[EXPENSE_AMOUNT]?.toIntOrNull() ?: 0
        val remaining = budget + income - expense

        val category = prefs[BUDGET_CATEGORY] ?: "N/A"
        val duration = prefs[BUDGET_DURATION] ?: "N/A"
        val notes = prefs[BUDGET_NOTES] ?: ""

        val summary = """
            📊 Budget Summary
            --------------------------
            Category: $category
            Duration: $duration
            
            Budget: $budget
            Income: $income
            Expense: $expense
            --------------------------
            Remaining: $remaining
            ${if (notes.isNotBlank()) "\n📝 Notes: $notes" else ""}
        """.trimIndent()

        resultText.text = summary
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_budget

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
                    // Already on BudgetActivity
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
