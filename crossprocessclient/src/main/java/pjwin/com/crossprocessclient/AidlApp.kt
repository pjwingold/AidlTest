package pjwin.com.crossprocessclient

import android.app.Application
import android.content.Context

class AidlApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
    }

    companion object {
        @SuppressWarnings("StaticFieldLeak")
        @JvmStatic
        lateinit var instance: AidlApp
            private set

        @SuppressWarnings("StaticFieldLeak")
        lateinit var context: Context
            private set
    }
}

val Context.aldiApp: AidlApp
    get() = applicationContext as AidlApp