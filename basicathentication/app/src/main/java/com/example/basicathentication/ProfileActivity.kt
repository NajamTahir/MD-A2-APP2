package com.example.basicathentication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ProfileActivity : AppCompatActivity() {

    private val FIRST_NAME = stringPreferencesKey("first_name")
    private val LAST_NAME = stringPreferencesKey("last_name")
    private val PHONE = stringPreferencesKey("phone")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val nameText = findViewById<TextView>(R.id.nameText)
        val emailText = findViewById<TextView>(R.id.emailText)
        val phoneText = findViewById<TextView>(R.id.phoneText)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        runBlocking {
            val prefs = applicationContext.dataStore.data.first()
            val firstName = prefs[FIRST_NAME] ?: "N/A"
            val lastName = prefs[LAST_NAME] ?: "N/A"
            val phone = prefs[PHONE] ?: "N/A"

            nameText.text = "$firstName $lastName"
            emailText.text = "$firstName.$lastName@example.com"
            phoneText.text = phone
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
