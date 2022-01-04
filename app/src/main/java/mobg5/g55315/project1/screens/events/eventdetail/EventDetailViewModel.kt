package mobg5.g55315.project1.screens.events.eventdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import mobg5.g55315.project1.model.Event
import mobg5.g55315.project1.model.Person
import mobg5.g55315.project1.model.Team
import mobg5.g55315.project1.util.FirebaseUtil

class EventDetailViewModel(
    private val eventKey: String,
    dataSource: FirebaseFirestore
) : ViewModel() {

    val database = dataSource

    var event: MutableLiveData<Event> = MutableLiveData()
    private var creator: String? = null

    var team: MutableLiveData<Team> = MutableLiveData()

    var persons: MutableLiveData<List<Person>> = MutableLiveData()

    var visible: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getEvent(eventKey)
    }

    private fun isVisible(): Boolean {
        val currentUser = FirebaseUtil.getAuth()?.uid
        val userCreator = event.value?.creator

        visible.value = currentUser == userCreator

        return currentUser == userCreator
    }


    private fun getEvent(key: String): MutableLiveData<Event> {
        database.collection("events").document(key)
            .addSnapshotListener(EventListener { value, e ->
                if (e != null) {
                    return@EventListener
                }
                val eventItem = value?.toObject(Event::class.java)

                event.value = eventItem
                if (eventItem != null) {
                    creator = eventItem.creator
                }
                isVisible()
            })
        return event
    }

    fun getPersonsDatabase(): LiveData<List<Person>> {
        event.value?.teamId?.let {
            database.collection("teams").document(it)
                .addSnapshotListener(EventListener { value, e ->
                    if (e != null) {
                        return@EventListener
                    }
                    val teamItem = value?.toObject(Team::class.java)
                    team.value = teamItem
                })
        }

        database.collection("users")
            .addSnapshotListener(EventListener { value, e ->
                if (e != null) {
                    return@EventListener
                }
                val personList: MutableList<Person> = mutableListOf()

                for (doc in value!!) {
                    val person = doc.toObject(Person::class.java)
                    if (person.id == event.value?.creator) {
                        personList.add(0, person)
                    } else if (team.value?.members?.contains(person.id) == true || team.value?.creator == person.id) {
                        personList.add(person)
                    }
                }
                persons.value = personList
            })
        return persons
    }


    fun addParticipantToEvent(personId: String) {
        if (personId != event.value?.creator && FirebaseUtil.getAuth()?.uid == event.value?.creator) {
            val newArray = event.value?.participants
            if (event.value?.participants?.contains(personId) == true) {
                newArray?.remove(personId)
            } else {
                newArray?.add(personId)
            }
            database.collection("events").document(eventKey).update(
                "participants", newArray
            )
        }
    }

    private val _navigateToEvent = MutableLiveData<Boolean?>()

    fun onDelete() {
        database.collection("events").document(eventKey).delete()
        val storage =
            FirebaseUtil.getFirestorage()!!.reference.storage.getReference("images/${eventKey}")
        storage.delete()
        onClose()
    }


    val navigateToEvent: LiveData<Boolean?>
        get() = _navigateToEvent


    fun doneNavigating() {
        _navigateToEvent.value = null
    }

    fun onClose() {
        _navigateToEvent.value = true
    }
}
