package mobg5.g55315.project1.screens.teams.teamdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore

class TeamDetailViewModelFactory(
    private val teamKey: String,
    private val dataSource: FirebaseFirestore
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamDetailViewModel::class.java)) {
            return TeamDetailViewModel(teamKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}