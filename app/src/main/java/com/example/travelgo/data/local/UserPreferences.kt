package com.example.travelgo.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// DataStore de preferencias de usuario
private val Context.userPrefsDataStore by preferencesDataStore("user_preferences")

class UserPreferences(private val context: Context) {

    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        private val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")
    }

    // ========== GUARDAR DATOS ==========

    suspend fun saveUsername(username: String) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[USERNAME_KEY] = username
        }
    }

    suspend fun saveEmail(email: String) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
        }
    }

    suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    suspend fun saveRememberMe(remember: Boolean) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[REMEMBER_ME_KEY] = remember
        }
    }

    // ========== OBTENER DATOS ==========

    fun getUsername(): Flow<String?> {
        return context.userPrefsDataStore.data.map { prefs ->
            prefs[USERNAME_KEY]
        }
    }

    suspend fun getUsernameOnce(): String? {
        return context.userPrefsDataStore.data.map { it[USERNAME_KEY] }.first()
    }

    fun getEmail(): Flow<String?> {
        return context.userPrefsDataStore.data.map { prefs ->
            prefs[EMAIL_KEY]
        }
    }

    suspend fun getEmailOnce(): String? {
        return context.userPrefsDataStore.data.map { it[EMAIL_KEY] }.first()
    }

    fun isLoggedIn(): Flow<Boolean> {
        return context.userPrefsDataStore.data.map { prefs ->
            prefs[IS_LOGGED_IN_KEY] ?: false
        }
    }

    suspend fun isLoggedInOnce(): Boolean {
        return context.userPrefsDataStore.data.map {
            it[IS_LOGGED_IN_KEY] ?: false
        }.first()
    }

    fun rememberMeEnabled(): Flow<Boolean> {
        return context.userPrefsDataStore.data.map { prefs ->
            prefs[REMEMBER_ME_KEY] ?: false
        }
    }

    // ========== LIMPIAR DATOS ==========

    suspend fun logout() {
        context.userPrefsDataStore.edit { it.clear() }
    }

    suspend fun clearUserData() {
        context.userPrefsDataStore.edit { prefs ->
            prefs.remove(USERNAME_KEY)
            prefs.remove(EMAIL_KEY)
        }
    }
}