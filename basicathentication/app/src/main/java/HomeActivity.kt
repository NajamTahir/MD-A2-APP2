package com.example.basicathentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.basicathentication.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Load saved theme before setting content view
        runBlocking {
            val prefs = applicationContext.dataStore.data.first()
            val theme = prefs[stringPreferencesKey("app_theme")]
            if (theme == "dark") {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Buttons and Icons
        val dashboardBtn = findViewById<Button>(R.id.btnDashboard)
        val incomeBtn = findViewById<Button>(R.id.btnAddIncome)
        val expenseBtn = findViewById<Button>(R.id.btnAddExpense)
        val budgetBtn = findViewById<Button>(R.id.btnBudget)
        val analyticsBtn = findViewById<Button>(R.id.btnAnalytics)
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val setting = findViewById<ImageView>(R.id.setting)

        // Settings icon click
        setting.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Profile icon click
        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Navigation buttons
        dashboardBtn.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        incomeBtn.setOnClickListener {
            startActivity(Intent(this, AddIncomeActivity::class.java))
        }

        expenseBtn.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        budgetBtn.setOnClickListener {
            startActivity(Intent(this, BudgetActivity::class.java))
        }

        analyticsBtn.setOnClickListener {
            startActivity(Intent(this, AnalyticsActivity::class.java))
        }
    }
}
