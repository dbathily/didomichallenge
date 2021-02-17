package com.example.didomiconsent

import java.util.*

data class ConsentReport(val status: ConsentStatus,
                         val deviceId: UUID,
                         val dateTime: Date)
