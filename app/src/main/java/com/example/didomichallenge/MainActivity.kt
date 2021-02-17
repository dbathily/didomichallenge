package com.example.didomichallenge

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.didomiconsent.Consent
import com.example.didomiconsent.ConsentStatus

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(Consent) {
            initialize(applicationContext)
            setStatusListener { status -> findViewById<TextView>(R.id.currentConsent).text = status?.name }
            val consentStatus = getConsentStatus()
            if(consentStatus == ConsentStatus.UNDEFINED)
                collectConsent(this@MainActivity) { status ->
                    println("User consent result: $status")
                }
            else
                findViewById<TextView>(R.id.currentConsent).text = consentStatus.name

            findViewById<Button>(R.id.resetConsent).setOnClickListener {
                resetConsentStatus()
            }
        }
    }
}