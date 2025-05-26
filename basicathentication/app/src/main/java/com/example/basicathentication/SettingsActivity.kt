package com.example.basicathentication

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.example.basicathentication.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private val EMAIL = stringPreferencesKey("email")
    private val PHONE = stringPreferencesKey("phone")
    private val PASSWORD = stringPreferencesKey("password")
    private val THEME = stringPreferencesKey("app_theme")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val changeEmail = findViewById<Button>(R.id.changeEmailButton)
        val changePhone = findViewById<Button>(R.id.changePhoneButton)
        val changePassword = findViewById<Button>(R.id.changePasswordButton)
        val themeSwitch = findViewById<Switch>(R.id.themeSwitch)

        // Load saved theme preference and set switch state
        lifecycleScope.launch {
            val prefs = applicationContext.dataStore.data.first()
            val themePref = prefs[THEME]
            themeSwitch.isChecked = themePref == "dark"
        }

        // Change Email
        changeEmail.setOnClickListener {
            showInputDialog("Change Email") { newEmail ->
                lifecycleScope.launch {
                    applicationContext.dataStore.edit { prefs ->
                        prefs[EMAIL] = newEmail
                    }
                    Toast.makeText(this@SettingsActivity, "Email updated", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Change Phone
        changePhone.setOnClickListener {
            showInputDialog("Change Phone Number") { newPhone ->
                lifecycleScope.launch {
                    applicationContext.dataStore.edit { prefs ->
                        prefs[PHONE] = newPhone
                    }
                    Toast.makeText(this@SettingsActivity, "Phone updated", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Change Password
        changePassword.setOnClickListener {
            showInputDialog("Change Password") { newPass ->
                if (newPass.length < 6) {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                } else {
                    lifecycleScope.launch {
                        applicationContext.dataStore.edit { prefs ->
                            prefs[PASSWORD] = newPass
                        }
                        Toast.makeText(this@SettingsActivity, "Password updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Theme toggle and persist
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            val themeValue = if (isChecked) "dark" else "light"

            AppCompatDelegate.setDefaultNightMode(mode)

            lifecycleScope.launch {
                applicationContext.dataStore.edit { prefs ->
                    prefs[THEME] = themeValue
                }
            }
        }
    }

    private fun showInputDialog(title: String, onSubmit: (String) -> Unit) {
        val input = EditText(this)
        input.hint = "Enter new value"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val text = input.text.toString()
                if (text.isNotEmpty()) {
                    onSubmit(text)
                } else {
                    Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
