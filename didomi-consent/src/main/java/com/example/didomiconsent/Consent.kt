package com.example.didomiconsent

import android.app.Activity
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.gson.FieldNamingPolicy
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.*

interface ConsentInterface {
    fun getDeviceId(): UUID
    fun getConsentStatus(): ConsentStatus
    fun setConsentStatus(status: ConsentStatus): Unit
    fun resetConsentStatus(): Unit
    fun collectConsent(activity: Activity, callback: (ConsentStatus) -> Unit):Dialog
    //This is a temporary simple listener, we can do way better
    fun setStatusListener(callback: (ConsentStatus?) -> Unit)
}

class Consent(private val context: WeakReference<Context>, private val conf: ConsentConf): ConsentInterface {
    private val store = SharedPreferencesConsentStore(context.get()!!)
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer {
                setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            }
        }
    }
    private var listener: ((ConsentStatus?) -> Unit)? = null

    override fun setStatusListener(callback: (ConsentStatus?) -> Unit) {
        listener = callback
    }

    override fun getDeviceId(): UUID =
        store.getDeviceId()

    override fun getConsentStatus(): ConsentStatus {
        return store.getConsent()
    }

    override fun setConsentStatus(status: ConsentStatus): Unit {
        GlobalScope.launch(Dispatchers.IO) {
            client.post<Unit>(conf.api) {
                contentType(ContentType.Application.Json)
                body = ConsentReport(status, getDeviceId(), Date())
            }
        }
        store.setConsent(status)
        listener?.invoke(status)
    }

    override fun resetConsentStatus(): Unit {
        store.resetConsent()
        listener?.invoke(null)
    }

    override fun collectConsent(activity: Activity, callback: (ConsentStatus) -> Unit):Dialog {
        return AlertDialog.Builder(activity).apply {
            setTitle(R.string.dialog_title)
            val appName = activity.applicationInfo.loadLabel(activity.packageManager).toString()
            setMessage(activity.getString(R.string.dialog_message, appName))
            setNegativeButton(R.string.dialog_negative_button_title) { dialog, id ->
                setConsentStatus(ConsentStatus.DENIED)
                callback(getConsentStatus())
            }
            setPositiveButton(R.string.dialog_positive_button_title) { dialog, id ->
                setConsentStatus(ConsentStatus.ACCEPTED)
                callback(getConsentStatus())
            }
        }.create().also { it.show() }
    }

    //Would be great if kotlin has a delegate for companion object
    companion object: ConsentInterface /*by this::instance*/ {
        val errorMessage = "Initialize Consent before. Ex: In your activity, Consent.initatize(this)"

        //I use this over lateinit because I want to provide a better error message
        private var instance: Consent? = null

        //We can take default conf from environment
        fun initialize(context: Context, conf: ConsentConf = ConsentConf("http://www.mocky.io/v2/5e14e8122d00002b00167430")) {
            instance = Consent(WeakReference(context), conf)
        }

        private fun instanceOrError(): Consent =
            checkNotNull(instance, { errorMessage })

        override fun getDeviceId(): UUID =
            instanceOrError().getDeviceId()

        override fun getConsentStatus(): ConsentStatus =
            instanceOrError().getConsentStatus()

        override fun resetConsentStatus() =
            instanceOrError().resetConsentStatus()

        override fun setConsentStatus(status: ConsentStatus): Unit =
            instanceOrError().setConsentStatus(status)

        override fun collectConsent(activity: Activity, callback: (ConsentStatus) -> Unit): Dialog =
            instanceOrError().collectConsent(activity, callback)

        override fun setStatusListener(callback: (ConsentStatus?) -> Unit) =
            instanceOrError().setStatusListener(callback)
    }
}