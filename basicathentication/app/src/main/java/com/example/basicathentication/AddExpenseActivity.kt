package com.example.basicathentication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.runBlocking

class AddExpenseActivity : AppCompatActivity() {

    private val EXPENSE_AMOUNT = stringPreferencesKey("expense_amount")
    private val EXPENSE_REASON = stringPreferencesKey("expense_reason")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val amountInput = findViewById<EditText>(R.id.expenseAmountInput)
        val reasonInput = findViewById<EditText>(R.id.expenseReasonInput)
        val saveButton = findViewById<Button>(R.id.saveExpenseButton)
        val profileIcon = findViewById<ImageView>(R.id.ICprofile)
        val setting = findViewById<ImageView>(R.id.ICsetting)

        setting.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        saveButton.setOnClickListener {
            val amount = amountInput.text.toString()
            val reason = reasonInput.text.toString()

            if (amount.isNotEmpty() && reason.isNotEmpty()) {
                runBlocking {
                    applicationContext.dataStore.edit { prefs ->
                        prefs[EXPENSE_AMOUNT] = amount
                        prefs[EXPENSE_REASON] = reason
                    }
                    Toast.makeText(this@AddExpenseActivity, "Expense Saved", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_expense

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
                    // Already on this screen
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
