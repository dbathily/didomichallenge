package com.example.didomiconsent

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConsentTest {

    @Test
    fun settingConsentShouldSucceed() {
        Consent.initialize(InstrumentationRegistry.getInstrumentation().targetContext)
        Consent.setConsentStatus(ConsentStatus.ACCEPTED)
        assert(Consent.getConsentStatus() == ConsentStatus.ACCEPTED)
    }

    //TODO Test that the dialog is being shown

    //TODO Test using a mock server that a http call is made

}