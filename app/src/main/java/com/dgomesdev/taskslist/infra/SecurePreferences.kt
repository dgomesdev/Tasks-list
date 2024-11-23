package com.dgomesdev.taskslist.infra

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.json.JSONObject
import java.util.Date

class SecurePreferences(context: Context){
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

    fun isTokenValid(): Boolean {
        val token = getToken()
        return try {
            val parts = token?.split(".")
            if (parts?.size == 3) {
                val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
                val expiry = JSONObject(payload).optLong("exp") * 1000 // JWT `exp` is in seconds
                val currentTime = Date().time
                currentTime < expiry
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun deleteToken() {
        editor.remove("token").apply()
    }
}