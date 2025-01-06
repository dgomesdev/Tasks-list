package com.dgomesdev.taskslist.infra

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.dgomesdev.taskslist.domain.model.User
import org.json.JSONObject

class SecurePreferences(context: Context) {
    private var masterKey: MasterKey = try {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    } catch (e: Exception) {
        Log.e("SecurePreferences", "Error initializing MasterKey: ${e.message}")
        regenerateMasterKey(context)
    }

    private fun regenerateMasterKey(context: Context): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .setRequestStrongBoxBacked(false)
            .build()
    }

    private var sharedPreferences: SharedPreferences = try {
        EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        Log.e("SecurePreferences", "Error initializing EncryptedSharedPreferences: ${e.message}")
        context.getSharedPreferences("fallback_prefs", Context.MODE_PRIVATE)
    }

    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveToken(token: String) {
        editor.putString("token", token).apply()
    }

    fun getToken(): String? {
        return try {
            sharedPreferences.getString("token", null)
        } catch (e: Exception) {
            Log.e("SecurePreferences", "Error retrieving token: ${e.message}")
            null
        }
    }

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

    fun getUserFromToken(): User? {
        return try {
            val payload = getTokenPayload()
            val userId = JSONObject(payload).optString("userId")
            val username = JSONObject(payload).optString("username")
            User(userId = userId, username = username)
        } catch (e: Exception) {
            Log.e("SecurePreferences", "Error getting userId: ${e.message}")
            null
        }
    }

    fun deleteToken() {
        editor.remove("token").apply()
    }
}