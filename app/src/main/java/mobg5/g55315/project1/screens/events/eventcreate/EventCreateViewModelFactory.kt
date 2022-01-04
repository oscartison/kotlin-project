package mobg5.g55315.project1.screens.events.eventcreate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore

class EventCreateViewModelFactory(
    private val dataSource: FirebaseFirestore
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventCreateViewModel::class.java)) {
            return EventCreateViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}