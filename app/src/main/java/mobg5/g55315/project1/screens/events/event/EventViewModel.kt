package mobg5.g55315.project1.screens.events.event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import mobg5.g55315.project1.model.Event
import mobg5.g55315.project1.model.Person
import mobg5.g55315.project1.util.FirebaseUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class EventViewModel(
    dataSource: FirebaseFirestore,
) : ViewModel() {

    val database = dataSource

    var events: MutableLiveData<List<Event>> = MutableLiveData()

    private val _navigateToEventDetail = MutableLiveData<String>()
    val navigateToEventDetail
        get() = _navigateToEventDetail

    private val _navigateToEventCreate = MutableLiveData<Boolean?>()
    val navigateToEventCreate: LiveData<Boolean?>
        get() = _navigateToEventCreate

    init {
        initializeEvents()
        addUser()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeEvents() {
        viewModelScope.launch {
            events = getEventsDatabaseUpcoming() as MutableLiveData<List<Event>>
        }
    }

    fun addUser() {
        var authenticaed_user = FirebaseAuth.getInstance().currentUser
        var person = Person(
            authenticaed_user?.uid,
            authenticaed_user?.displayName, authenticaed_user?.email
        )
        database.collection("users").whereEqualTo("id", authenticaed_user?.uid)
            .get().addOnSuccessListener { documents ->
                if (documents.isEmpty && person.name != null) {
                    database.collection("users").add(person)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEventsDatabaseUpcoming(): LiveData<List<Event>> {
        database.collection("events")
            .addSnapshotListener(EventListener { value, e ->
                if (e != null) {
                    return@EventListener
                }
                var eventList: MutableList<Event> = mutableListOf()
                for (doc in value!!) {
                    var eventItem = doc.toObject(Event::class.java)
                    val currentUser = FirebaseUtil.getAuth()?.uid
                    if (eventItem.participants.contains(currentUser) || eventItem.creator == currentUser) {
                        eventList.add(eventItem)
                    }
                }
                val dateTimeFormatter: DateTimeFormatter =
                    DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")

                eventList.sortBy { event ->
                    LocalDateTime.parse(event.time + " " + event.date, dateTimeFormatter)
                }
                eventList.removeAll { event ->
                    LocalDateTime.parse(
                        event.time + " " + event.date,
                        dateTimeFormatter
                    ) <= LocalDateTime.now()
                }

                events.value = eventList
            })
        return events
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEventsDatabasePast(): LiveData<List<Event>> {
        database.collection("events")
            .addSnapshotListener(EventListener { value, e ->
                if (e != null) {
                    return@EventListener
                }
                var eventList: MutableList<Event> = mutableListOf()
                for (doc in value!!) {
                    var eventItem = doc.toObject(Event::class.java)
                    val currentUser = FirebaseUtil.getAuth()?.uid
                    if (eventItem.participants.contains(currentUser) || eventItem.creator == currentUser) {
                        eventList.add(eventItem)
                    }
                }
                val dateTimeFormatter: DateTimeFormatter =
                    DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")

                eventList.sortByDescending { event ->
                    LocalDateTime.parse(event.time + " " + event.date, dateTimeFormatter)
                }
                eventList.removeAll { event ->
                    LocalDateTime.parse(
                        event.time + " " + event.date,
                        dateTimeFormatter
                    ) >= LocalDateTime.now()
                }

                events.value = eventList
            })
        return events
    }


    fun onEventClicked(id: String) {
        _navigateToEventDetail.value = id
    }

    fun onEventDetailNavigated() {
        _navigateToEventDetail.value = null
    }

    fun onEventCreateClicked() {
        _navigateToEventCreate.value = true

    }

    fun onEventCreateNavigated() {
        _navigateToEventCreate.value = null
    }
}
