package com.example.challenge_five.presentation.ui.login.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserLoginPreferences(
    private val context: Context
) {
    fun getEmail(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[EMAIL_KEY] ?: ""
        }
    }

    fun login(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { preferences ->
                preferences[EMAIL_KEY] = email
            }
        }
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { preferences ->
                preferences[EMAIL_KEY] = ""
            }
        }
    }

    companion object {
        private val DATASTORE_NAME = "user_login"
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val STATE_KEY = booleanPreferencesKey("state")

        private val Context.dataStore by preferencesDataStore(
            DATASTORE_NAME
        )
    }
}