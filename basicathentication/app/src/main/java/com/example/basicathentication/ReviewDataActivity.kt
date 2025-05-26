package com.example.basicathentication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// ✅ Reuse the shared dataStore extension
import com.example.basicathentication.dataStore

class ReviewDataActivity : AppCompatActivity() {

    private val FIRST_NAME = stringPreferencesKey("first_name")
    private val LAST_NAME = stringPreferencesKey("last_name")
    private val PHONE = stringPreferencesKey("phone")
    private val PASSWORD = stringPreferencesKey("password")
    private val INCOME_AMOUNT = stringPreferencesKey("income_amount")
    private val INCOME_SOURCE = stringPreferencesKey("income_source")
    private val EXPENSE_AMOUNT = stringPreferencesKey("expense_amount")
    private val EXPENSE_REASON = stringPreferencesKey("expense_reason")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_data)

        val reviewTextView = findViewById<TextView>(R.id.reviewTextView)

        runBlocking {
            val preferences = applicationContext.dataStore.data.first()

            val firstName = preferences[FIRST_NAME] ?: "N/A"
            val lastName = preferences[LAST_NAME] ?: "N/A"
            val phone = preferences[PHONE] ?: "N/A"
            val password = preferences[PASSWORD] ?: "N/A"
            val incomeAmount = preferences[INCOME_AMOUNT] ?: "N/A"
            val incomeSource = preferences[INCOME_SOURCE] ?: "N/A"
            val expenseAmount = preferences[EXPENSE_AMOUNT] ?: "N/A"
            val expenseReason = preferences[EXPENSE_REASON] ?: "N/A"

            val reviewText = """
                --- User Info ---
                First Name: $firstName
                Last Name: $lastName
                Phone: $phone
                Password: $password

                --- Income Info ---
                Amount: $incomeAmount
                Source: $incomeSource

                --- Expense Info ---
                Amount: $expenseAmount
                Reason: $expenseReason
            """.trimIndent()

            reviewTextView.text = reviewText
        }
    }
}
