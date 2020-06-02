package ch.reinhold.ifolor

import android.app.Application
import ch.reinhold.ifolor.core.di.provideIfolorModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class IfolorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@IfolorApplication)
            modules(listOf(provideIfolorModule()))
        }
    }

}
