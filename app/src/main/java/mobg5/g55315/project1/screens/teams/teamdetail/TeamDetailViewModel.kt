package mobg5.g55315.project1.screens.teams.teamdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import mobg5.g55315.project1.model.Person
import mobg5.g55315.project1.model.Team
import mobg5.g55315.project1.util.FirebaseUtil

class TeamDetailViewModel(
    private val teamKey: String,
    dataSource: FirebaseFirestore
) : ViewModel() {

    val database = dataSource

    var team: MutableLiveData<Team> = MutableLiveData()
    var creator: String? = null

    var persons: MutableLiveData<List<Person>> = MutableLiveData()

    var visible: MutableLiveData<Boolean> = MutableLiveData()

    private fun initializePersons() {
        persons = getPersonsDatabase() as MutableLiveData<List<Person>>
    }

    fun getPersonsDatabase(): LiveData<List<Person>> {
        database.collection("users")
            .addSnapshotListener(EventListener { value, e ->
                if (e != null) {
                    return@EventListener
                }
                val personList: MutableList<Person> = mutableListOf()
                val currentUser = FirebaseUtil.getAuth()?.uid

                for (doc in value!!) {
                    val person = doc.toObject(Person::class.java)
                    if (person.id == team.value?.creator) {
                        personList.add(0, person)
                    } else if ((currentUser != team.value?.creator && team.value?.members?.contains(
                            person.id
                        ) == true) || currentUser == team.value?.creator
                    ) {
                        personList.add(person)
                    }
                }
                persons.value = personList

            })
        return persons
    }

    init {
        getLiveData(teamKey)
        initializePersons()
    }

    fun isVisible(): Boolean {
        val currentUser = FirebaseUtil.getAuth()?.uid
        val userCreator = team.value?.creator

        visible.value = currentUser == userCreator

        return currentUser == userCreator
    }

    fun getLiveData(key: String) {
        database.collection("teams").document(key)
            .addSnapshotListener(EventListener { value, e ->
                if (e != null) {
                    return@EventListener
                }
                val teamItem = value?.toObject(Team::class.java)

                team.value = teamItem
                if (teamItem != null) {
                    creator = teamItem.creator
                }
                isVisible()
            })
    }

    private val _navigateToTeam = MutableLiveData<Boolean?>()

    fun onDelete() {
        database.collection("teams").document(teamKey).delete()
        onClose()
    }

    val navigateToTeam: LiveData<Boolean?>
        get() = _navigateToTeam


    fun doneNavigating() {
        _navigateToTeam.value = null
    }

    fun onClose() {
        _navigateToTeam.value = true
    }

    fun addParticipantToEvent(personId: String) {
        if (personId != team.value?.creator && FirebaseUtil.getAuth()?.uid == team.value?.creator) {
            val newArray = team.value?.members
            if (team.value?.members?.contains(personId) == true) {
                newArray?.remove(personId)
            } else {
                newArray?.add(personId)
            }
            database.collection("teams").document(teamKey).update(
                "members", newArray
            )
        }

    }
}
