package mobg5.g55315.project1.screens.teams.team

import androidx.lifecycle.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import mobg5.g55315.project1.model.Team
import mobg5.g55315.project1.util.FirebaseUtil

class TeamViewModel(
    dataSource: FirebaseFirestore
) : ViewModel() {

    val database = dataSource

    var teams: MutableLiveData<List<Team>> = MutableLiveData()

    private val _navigateToTeamDetail = MutableLiveData<String>()
    val navigateToEventDetail
        get() = _navigateToTeamDetail

    private val _navigateToTeamCreate = MutableLiveData<Boolean?>()
    val navigateToTeamtCreate: LiveData<Boolean?>
        get() = _navigateToTeamCreate


    init {
        initializeTeams()
    }

    private fun initializeTeams() {
        viewModelScope.launch {
            teams = getTeamsDatabase() as MutableLiveData<List<Team>>
        }
    }

    fun getTeamsDatabase(): LiveData<List<Team>> {
        database.collection("teams")
            .addSnapshotListener(EventListener { value, e ->
                if (e != null) {
                    return@EventListener
                }
                var teamList: MutableList<Team> = mutableListOf()
                for (doc in value!!) {
                    val currentUser = FirebaseUtil.getAuth()?.uid
                    var teamItem = doc.toObject(Team::class.java)
                    if (teamItem.members.contains(currentUser) || teamItem.creator == currentUser) {
                        teamList.add(teamItem)
                    }
                }
                teams.value = teamList

            })
        return teams
    }


    fun onTeamClicked(id: String) {
        _navigateToTeamDetail.value = id

    }

    fun onTeamDetailNavigated() {
        _navigateToTeamDetail.value = null
    }

    fun onTeamCreateClicked() {
        _navigateToTeamCreate.value = true

    }

    fun onTeamCreateNavigated() {
        _navigateToTeamCreate.value = null
    }

}
