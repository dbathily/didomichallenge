package com.example.didomiconsent

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.*

interface ConsentStore {

    fun getDeviceId(): UUID
    fun setConsent(status: ConsentStatus): Unit
    fun getConsent(): ConsentStatus
    fun resetConsent(): Unit

}

class SharedPreferencesConsentStore(private val context: Context): ConsentStore {

    private val deviceIdKey = "deviceId"
    private val key = "status"
    private val preferences: SharedPreferences = context.getSharedPreferences("didomi-consent", 0)

    override fun getDeviceId(): UUID {
        return preferences.getString(deviceIdKey, null)?.let { UUID.fromString(it) }
            ?: UUID.randomUUID().also {
                preferences.edit { putString(deviceIdKey, it.toString()) }
            }
    }

    override fun setConsent(status: ConsentStatus): Unit {
        preferences.edit { putString(key, status.toString()) }
    }

    override fun getConsent(): ConsentStatus {
        return preferences.getString(key, null)
            ?.let {
                try {
                    ConsentStatus.valueOf(it)
                } catch (e: Throwable) {
                    ConsentStatus.UNDEFINED
                }
            } ?: ConsentStatus.UNDEFINED
    }

    override fun resetConsent(): Unit {
        return preferences.edit { remove(key) }
    }

}