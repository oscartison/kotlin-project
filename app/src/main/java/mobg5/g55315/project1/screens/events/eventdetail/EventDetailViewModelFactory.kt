package mobg5.g55315.project1.screens.events.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore

class EventDetailViewModelFactory(
    private val eventKey: String,
    private val dataSource: FirebaseFirestore
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel(eventKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}