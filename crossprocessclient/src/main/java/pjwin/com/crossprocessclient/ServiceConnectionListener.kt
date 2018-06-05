package pjwin.com.crossprocessclient

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import pjwin.com.aidltest.AidlTestInterface
import pjwin.com.aidltest.Person

class ServiceConnectionListener(private var aidlCallback: ((List<Person>) -> Unit)?) : LifecycleObserver {

    private var messengerBound = false
    private var messenger: Messenger? = null
    private var aidlBound = false
    private var aidlService: AidlTestInterface? = null

    //inline fun callback(block: () -> Unit) = block()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        var intent = Intent("pjwin.com.aidltest.AidlService")
        intent.`package` = "pjwin.com.aidltest"
        AidlApp.context.bindService(intent, aidlConn, Context.BIND_AUTO_CREATE)

        intent = Intent("pjwin.com.aidltest.MessengerService")
        intent.`package` = "pjwin.com.aidltest"
        AidlApp.context.bindService(intent, messengerConn, Context.BIND_AUTO_CREATE)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        AidlApp.context.unbindService(messengerConn)
        messengerBound = false

        AidlApp.context.unbindService(aidlConn)
        aidlBound = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        aidlCallback = null
    }

    private val messengerConn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            messengerBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messenger = Messenger(service)
            messengerBound = true
        }
    }

    private val aidlConn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            aidlBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            aidlService = AidlTestInterface.Stub.asInterface(service)
            aidlBound = true
        }
    }

    fun messengerAction() {
        if (messengerBound) {
            val message = Message.obtain(null, 100)
            val bundle = Bundle()
            bundle.putString("TAG", "Bundle data from client")
            message.data = bundle
            messenger?.send(message)
        }
    }

    fun aidlAction() {
        if (aidlBound) {
            launch(UI) {
                addPerson(1, "max").await()
                addPerson(2, "jun").await()
                addPerson(3, "ying").await()

                val personList = listPersons().await()
                personList?.let { aidlCallback?.invoke(it) }
            }
        }
    }

    private fun addPerson(id: Int, name: String) = async {
        aidlService?.addPerson(Person(id, name))
    }

    private fun listPersons() = async {
        aidlService?.listPersons()
    }
}