package com.oratakashi.catatanuang

import android.app.Application
import com.oratakashi.catatanuang.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Application entry point. Initializes Koin dependency injection.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
class CatatanUangApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@CatatanUangApp)
            modules(AppModule)
        }
    }
}

