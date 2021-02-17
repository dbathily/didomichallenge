package com.example.didomiconsent

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConsentInitializedTest {
    @get:Rule
    val exceptionRule = ExpectedException.none()

    @Test
    fun accessingConsentShouldFailWithoutInitialization() {
        exceptionRule.expect(IllegalStateException::class.java)
        exceptionRule.expectMessage(Consent.errorMessage)
        Consent.getConsentStatus()
    }

    @Test
    fun settingConsentShouldFailWithoutInitialization() {
        exceptionRule.expect(IllegalStateException::class.java)
        exceptionRule.expectMessage(Consent.errorMessage)
        Consent.setConsentStatus(ConsentStatus.ACCEPTED)
    }
}