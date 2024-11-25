package com.dgomesdev.taskslist.infra

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.dgomesdev.taskslist.domain.model.User
import org.json.JSONObject
import java.util.Date

class SecurePreferences(context: Context) {
    private var masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private var sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveToken(token: String) {
        editor.putString("token", token).apply()
    }

    fun getToken(): String? = sharedPreferences.getString("token", null)

    private fun getTokenPayload(): String {
        return try {
            val token = getToken()
            val parts = token?.split(".")
            String(android.util.Base64.decode(parts?.get(1), android.util.Base64.URL_SAFE))
        } catch (e: Exception) {
            Log.e("SecurePreferences", "Error getting token payload: ${e.message}")
            ""
        }
    }

    fun isTokenValid(): Boolean {
        return try {
            val payload = getTokenPayload()
            val expiry = JSONObject(payload).optLong("exp") * 1000 // JWT `exp` is in seconds
            val currentTime = Date().time
            currentTime < expiry
        } catch (e: Exception) {
            Log.e("SecurePreferences", "Error validating token: ${e.message}")
            false
        }
    }

    fun getUserFromToken(): User? {
        return try {
            val payload = getTokenPayload()
            val userId = JSONObject(payload).optString("userId")
            val username = JSONObject(payload).optString("username")
            User(userId = userId, username = username)
        } catch (e: Exception) {
            Log.e("SecurePreferences", "Error getting user ID: ${e.message}")
            null
        }
    }

    fun deleteToken() {
        editor.remove("token").apply()
    }
}