// AidlTestInterface.aidl
package pjwin.com.aidltest;
import pjwin.com.aidltest.Person;
// Declare any non-default types here with import statements


interface AidlTestInterface {

    void addPerson(in Person person);

    List<Person> listPersons();
}
