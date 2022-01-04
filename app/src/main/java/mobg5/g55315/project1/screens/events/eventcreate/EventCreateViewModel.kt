package mobg5.g55315.project1.screens.events.eventcreate

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import mobg5.g55315.project1.model.Event
import mobg5.g55315.project1.model.Team
import mobg5.g55315.project1.util.FirebaseUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SimpleDateFormat", "UseCompatLoadingForDrawables")
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class EventCreateViewModel(
    dataSource: FirebaseFirestore
) : ViewModel() {

    val database = dataSource
    var eventName = MutableLiveData<String?>()
    var eventDate = MutableLiveData<String?>()
    var eventTime = MutableLiveData<String?>()
    var eventTeam = MutableLiveData<Team?>()
    var eventImage: String? = null
    var eventCreator: String
    var teams = MutableLiveData<List<Team>>()

    private val _statusToast = MutableLiveData<Boolean?>()
    val statusToast: MutableLiveData<Boolean?>
        get() = _statusToast


    private val _navigateBack = MutableLiveData<Boolean?>()

    private val _progress = MutableLiveData<Boolean?>()

    init {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        eventDate.value = currentDate
        val stf = SimpleDateFormat("hh:mm")
        val currentTime = stf.format(Date())
        eventTime.value = currentTime
        getTeams()
        eventCreator = FirebaseUtil.getAuth()?.uid.toString()
    }

    fun getTeams(): LiveData<List<Team>> {
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

    val progress: LiveData<Boolean?>
        get() = _progress


    fun doneProgress() {
        _progress.value = null
    }

    fun setProgress() {
        _progress.value = true
    }


    val navigateBack: LiveData<Boolean?>
        get() = _navigateBack


    fun doneNavigating() {
        _navigateBack.value = null
    }

    fun onClose() {
        if (eventImage != null && File(eventImage!!).exists()) {
            File(eventImage!!).delete()
        }
        _navigateBack.value = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAdd() {
        if (eventName.value != null) {
            val eventToAdd = Event(
                null, eventName.value, eventTime.value, eventDate.value,
                eventTeam.value?.id, eventTeam.value?.name, eventCreator, null, ArrayList()
            )
            database.collection("events").add(eventToAdd)
                .addOnSuccessListener { documentReference ->
                    if (eventImage != null) {
                        val file = Uri.fromFile(File(eventImage!!))
                        val storage =
                            FirebaseUtil.getFirestorage()!!.reference.storage.getReference("images/${documentReference.id}")
                        storage.putFile(file).addOnProgressListener {
                            setProgress()
                        }.addOnCompleteListener {
                            doneProgress()
                            statusToast.value = true
                            onClose()
                        }
                    } else {
                        onClose()
                    }
                }
        } else {
            statusToast.value = false
        }
    }
}
