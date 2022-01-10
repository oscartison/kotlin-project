package mobg5.g55315.project1.screens.teams.teamcreate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import mobg5.g55315.project1.model.Category
import mobg5.g55315.project1.model.Team
import mobg5.g55315.project1.util.FirebaseUtil
import java.util.*


class TeamCreateViewModel(
    dataSource: FirebaseFirestore) : ViewModel() {

    val database = dataSource

    var teamName = MutableLiveData<String?>()
    var categories = MutableLiveData<List<String>>()
    var teamCreator : String? = null
    var teamCategory = MutableLiveData<String?>()

    private val _statusToast = MutableLiveData<Boolean?>()
    val statusToast: MutableLiveData<Boolean?>
        get() = _statusToast

    private val _navigateBack = MutableLiveData<Boolean?>()

    init {
        teamCreator = FirebaseUtil.getAuth()?.uid.toString()
        categories = getCategories() as MutableLiveData<List<String>>
    }

    fun getCategories(): LiveData<List<String>>  {
        database.collection("categories")
            .addSnapshotListener(EventListener { value, e ->
                if (e != null) {
                    return@EventListener
                }
                var categoryList: MutableList<String> = mutableListOf()
                for (doc in value!!) {
                    var category = doc.toObject(Category::class.java)

                    category.category?.let { categoryList.add(it)  }
                }
                categories.value = categoryList
            })
        return categories
    }

    val navigateBack: LiveData<Boolean?>
        get() = _navigateBack


    fun doneNavigating() {
        _navigateBack.value = null
    }

    fun onClose() {
        _navigateBack.value = true
    }

    fun onAdd() {
        if(teamName.value!=null && teamCategory.value != null) {
            var teamToAdd = Team(teamName.value, teamCategory.value, mutableListOf(), teamCreator)
            database.collection("teams").add(teamToAdd).addOnSuccessListener {
                statusToast.value = true
                onClose()
            }
        } else {
            statusToast.value = false
        }
    }
}
