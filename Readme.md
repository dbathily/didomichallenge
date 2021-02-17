My attempt to take didomi's mobile challenge [https://github.com/didomi/challenges/tree/master/mobile](https://github.com/didomi/challenges/tree/master/mobile)

The library is implemented in `didomi-consent` module and the sample in `app`

## How to use

You need to initialize the library with the application context before using it. Somewhere in your main

```kotlin
import com.example.didomiconsent.Consent

Consent.initialize(applicationContext)
```

After that, you will be able to:

- get the current consent status `Consent.getConsentStatus()`
- collect the consent in an activity using the provided dialog
    ```kotlin
    Consent.collectConsent(activty) { status ->
        //...
    }
    ```
- observe any consent status changes by subscribing a listener
    ```kotlin
    Consent.setStatusListener { status -> 
        //... 
    }
    ```
- reset the stored status `Consent.resetConsentStatus()`