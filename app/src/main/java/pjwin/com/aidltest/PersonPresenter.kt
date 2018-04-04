package pjwin.com.aidltest

import android.os.SystemClock
import kotlin.properties.Delegates

/**
 * Created by hans on 23-Jan-18.
 */
object PersonPresenter {

    private var personList: ArrayList<Person> by Delegates.notNull()

    init {
        personList = arrayListOf()
    }

    fun addPerson(person: Person) {
        SystemClock.sleep(1000)
        personList.add(person)
    }

    fun personList(): ArrayList<Person> {
        SystemClock.sleep(1000)
        return personList
    }

}