package pjwin.com.crossprocessclient

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import pjwin.com.aidltest.Person


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.qualifiedName
    private lateinit var connectionListener: ServiceConnectionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aidl_btn.setOnClickListener {
            aidlAction()
        }

        messenger_btn.setOnClickListener {
            messengerAction()
        }

        task_btn.setOnClickListener {
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
            result_txt.text = "person id: " + it.id + " name: " + it.name
        }
    }

    private fun messengerAction() {
        connectionListener.messengerAction()
    }
}