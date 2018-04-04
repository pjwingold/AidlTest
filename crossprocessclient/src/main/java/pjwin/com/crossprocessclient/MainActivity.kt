package pjwin.com.crossprocessclient

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import pjwin.com.aidltest.AidlTestInterface
import pjwin.com.aidltest.Person


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.qualifiedName
    private var messengerBound = false
    private var aidlBound = false
    private lateinit var aidlBtn: Button
    private lateinit var messengerBtn: Button
    private lateinit var taskBtn: Button
    private var messenger: Messenger? = null
    private var aidlService: AidlTestInterface? = null
    private lateinit var resultTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultTxt = findViewById(R.id.result_txt)
        aidlBtn = findViewById(R.id.aidl_btn)
        aidlBtn.setOnClickListener {
            aidlAction()
        }
        messengerBtn = findViewById(R.id.messenger_btn)
        messengerBtn.setOnClickListener {
            messengerAction()
        }
        taskBtn = findViewById(R.id.task_btn)
        taskBtn.setOnClickListener {
            val intent = Intent(this, AffinityActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
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

    override fun onStart() {
        super.onStart()
        var intent = Intent("pjwin.com.aidltest.AidlService")
        intent.`package` = "pjwin.com.aidltest"
        bindService(intent, aidlConn, Context.BIND_AUTO_CREATE)

        intent = Intent("pjwin.com.aidltest.MessengerService")
        intent.`package` = "pjwin.com.aidltest"
        bindService(intent, messengerConn, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(messengerConn)
        messengerBound = false

        unbindService(aidlConn)
        aidlBound = false
    }

    private fun aidlAction() {
        if (aidlBound) {
            launch(UI) {
                Log.d(TAG, "loading")
                addPerson(1, "max").await()
                addPerson(2, "jun").await()
                addPerson(3, "ying").await()

                val personList = listPersons().await()
                showPerson(personList)
            }
        }
    }

    private fun addPerson(id: Int, name: String) = async {
        aidlService?.addPerson(Person(id, name))
    }

    private fun listPersons() = async {
        aidlService?.listPersons()
    }

    private fun showPerson(personList: List<Person>?) {
        Log.d(TAG, "loading finish")
        personList?.map {
            Log.d(TAG, "person id: " + it.id + " name: " + it.name)
            resultTxt.text = "person id: " + it.id + " name: " + it.name
        }
    }

    private fun messengerAction() {
        if (messengerBound) {
            val message = Message.obtain(null, 100)
            val bundle = Bundle()
            bundle.putString("TAG", "Bundle data from client")
            message.data = bundle
            messenger?.send(message)
        }
    }
}
