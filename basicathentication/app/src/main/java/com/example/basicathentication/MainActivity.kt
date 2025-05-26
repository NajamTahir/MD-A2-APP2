package com.example.basicathentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.runBlocking

val Context.dataStore by preferencesDataStore(name = "User_Data")

class MainActivity : AppCompatActivity() {

    private val FIRST_NAME = stringPreferencesKey("first_name")
    private val LAST_NAME = stringPreferencesKey("last_name")
    private val PHONE = stringPreferencesKey("phone")
    private val PASSWORD = stringPreferencesKey("password")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val firstNameInput = findViewById<EditText>(R.id.firstNameInput)
        val lastNameInput = findViewById<EditText>(R.id.lastNameInput)
        val phoneInput = findViewById<EditText>(R.id.phoneInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val loginText = findViewById<TextView>(R.id.loginText)

        signUpButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            when {
                firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || password.isEmpty() -> {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
                phone.length < 8 -> {
                    Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    runBlocking {
                        saveUserData(firstName, lastName, phone, password)
                        Toast.makeText(this@MainActivity, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                        finish()
                    }
                }
            }
        }

        loginText.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    private suspend fun saveUserData(firstName: String, lastName: String, phone: String, password: String) {
        applicationContext.dataStore.edit { preferences ->
            preferences[FIRST_NAME] = firstName
            preferences[LAST_NAME] = lastName
            preferences[PHONE] = phone
            preferences[PASSWORD] = password
        }
    }
}
