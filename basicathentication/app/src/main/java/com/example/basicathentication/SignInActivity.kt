package com.example.basicathentication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// ✅ Use the shared dataStore from DataStoreHelper.kt
import com.example.basicathentication.dataStore

class SignInActivity : AppCompatActivity() {

    private val PHONE = stringPreferencesKey("phone")
    private val PASSWORD = stringPreferencesKey("password")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signInLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val phoneInput = findViewById<EditText>(R.id.phoneSignInInput)
        val passwordInput = findViewById<EditText>(R.id.passwordSignInInput)
        val signInButton = findViewById<Button>(R.id.signInButton)
        val goToSignUp = findViewById<TextView>(R.id.goToSignUp)

        signInButton.setOnClickListener {
            val phone = phoneInput.text.toString()
            val password = passwordInput.text.toString()

            runBlocking {
                val storedPhone = getValue(PHONE)
                val storedPassword = getValue(PASSWORD)

                if (phone == storedPhone && password == storedPassword) {
                    Toast.makeText(this@SignInActivity, "Sign-in successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
                } else {
                    Toast.makeText(this@SignInActivity, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        goToSignUp.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private suspend fun getValue(key: androidx.datastore.preferences.core.Preferences.Key<String>): String? {
        val preferences = applicationContext.dataStore.data.first()
        return preferences[key]
    }
}
