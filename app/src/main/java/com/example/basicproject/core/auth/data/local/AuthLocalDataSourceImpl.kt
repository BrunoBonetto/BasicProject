package com.example.basicproject.core.auth.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

class AuthLocalDataSourceImpl @Inject constructor (
    @ApplicationContext private val context: Context
): AuthLocalDataSource {

    private val prefs by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            AuthStorageKeys.PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override suspend fun saveToken(token: String) {
        prefs.edit() { putString(AuthStorageKeys.AUTH_TOKEN, token) }
    }

    override suspend fun getToken(): String? {
        return prefs.getString(AuthStorageKeys.AUTH_TOKEN, null)
    }

    override suspend fun clearToken() {
        prefs.edit() { remove(AuthStorageKeys.AUTH_TOKEN) }
    }

}