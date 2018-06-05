package pjwin.com.crossprocessclient

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import pjwin.com.aidltest.Person


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.qualifiedName
    private lateinit var aidlBtn: Button
    private lateinit var messengerBtn: Button
    private lateinit var taskBtn: Button
    private lateinit var resultTxt: TextView
    private lateinit var connectionListener: ServiceConnectionListener

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

        connectionListener = ServiceConnectionListener { list -> showPerson(list) }
        lifecycle.addObserver(connectionListener)

    }


    private fun aidlAction() {
        Log.d(TAG, "loading")
        connectionListener.aidlAction()
    }

    private fun showPerson(personList: List<Person>?) {
        Log.d(TAG, "loading finish")
        personList?.forEach {
            Log.d(TAG, "person id: " + it.id + " name: " + it.name)
            resultTxt.text = "person id: " + it.id + " name: " + it.name
        }
    }

    private fun messengerAction() {
        connectionListener.messengerAction()
    }
}