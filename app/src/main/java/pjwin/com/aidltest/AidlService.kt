package pjwin.com.aidltest

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Parcel
import android.os.SystemClock
import android.util.Log


class AidlService : Service() {
    private val TAG = "AidlService"
    lateinit var presenter: PersonPresenter

    override fun onBind(intent: Intent?): IBinder = object : AidlTestInterface.Stub() {
        override fun addPerson(person: Person) {
            synchronized(this) {
                longOp()
                addPersonBg(person)
            }
        }

        override fun listPersons(): MutableList<Person> = presenter.personList()

        override fun onTransact(code: Int, data: Parcel?, reply: Parcel?, flags: Int): Boolean {
            return super.onTransact(code, data, reply, flags)
        }
    }

    override fun onCreate() {
        synchronized(this) {
            presenter = PersonPresenter
        }
    }

    private fun addPersonBg(person: Person) {
        presenter.addPerson(person)
    }

    private fun longOp() {
        Log.d(TAG, "longOp")
        SystemClock.sleep(2000)
    }
}