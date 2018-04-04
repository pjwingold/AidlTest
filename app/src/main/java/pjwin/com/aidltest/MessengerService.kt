package pjwin.com.aidltest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast

/**
 * Created by hans on 23-Jan-18.
 */
class MessengerService : Service() {
    companion object {
        const val MESSENGER_MSG = 100
        const val DATA_TAG = "TAG"
    }

    private lateinit var messenger: Messenger

    override fun onCreate() {
        super.onCreate()
        messenger = Messenger(IncomingHandler(applicationContext))
    }

    override fun onBind(intent: Intent?): IBinder {
        Toast.makeText(this, "Binding success", Toast.LENGTH_LONG).show()
        return messenger.binder
    }

    class IncomingHandler(private var context: Context) : Handler() {

        override fun handleMessage(msg: Message?) {
            msg?.let {
                when (it.what) {
                    MESSENGER_MSG -> {
                        val bundle = msg.data
                        Toast.makeText(context, "handle message " + bundle.getString(DATA_TAG), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

