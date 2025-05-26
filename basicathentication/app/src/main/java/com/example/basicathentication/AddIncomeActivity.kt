package com.example.basicathentication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.runBlocking

class AddIncomeActivity : AppCompatActivity() {

    private val INCOME_AMOUNT = stringPreferencesKey("income_amount")
    private val INCOME_SOURCE = stringPreferencesKey("income_source")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)

        val amountInput = findViewById<EditText>(R.id.incomeAmountInput)
        val sourceInput = findViewById<EditText>(R.id.incomeSourceInput)
        val saveButton = findViewById<Button>(R.id.saveIncomeButton)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val setting = findViewById<ImageView>(R.id.setting)

        // Set selected item as current
        bottomNav.selectedItemId = R.id.nav_income

        // Handle nav item clicks
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    true
                }
                R.id.nav_income -> true  // already here
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
        setting.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Save income data
        saveButton.setOnClickListener {
            val amount = amountInput.text.toString()
            val source = sourceInput.text.toString()

            if (amount.isNotEmpty() && source.isNotEmpty()) {
                runBlocking {
                    applicationContext.dataStore.edit { prefs ->
                        prefs[INCOME_AMOUNT] = amount
                        prefs[INCOME_SOURCE] = source
                    }
                    Toast.makeText(this@AddIncomeActivity, "Income Saved", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
