package mobg5.g55315.project1.screens.login

import android.app.Application
import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import mobg5.g55315.project1.database.EmailDatabaseDao
import mobg5.g55315.project1.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LoginViewModel(
    val database: EmailDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private val _statusToast = MutableLiveData<Boolean?>()
    val statusToast: MutableLiveData<Boolean?>
        get() = _statusToast

    val email = MutableLiveData<String>()

    private val users = database.getAllEmails()

    val emails = Transformations.map(users) { users ->
        users.map { item ->
            item.userEmail
        }
    }

    fun isFormValid(): Boolean {
        return email.value != null && Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addToDB() {
        viewModelScope.launch {
            val current = LocalDateTime.now()
            val formatDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val formatHour = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
            val formatted = current.format(formatDate)
            val formattedHour = current.format(formatHour)
            val existingUser = email.value?.let { get(it) }

            if(existingUser!=null) {
                val user = email.value?.let { User(it, formatted, formattedHour) }
                if (user != null) {
                    update(user)
                }
            } else {
                val user = email.value?.let { User(it, formatted, formattedHour) }
                if (user != null) {
                    insert(user)
                }
            }
        }
    }

    private suspend fun update(user: User) {
        database.update(user)
    }

    private suspend fun insert(user: User) {
        database.insert(user)
    }

    private suspend fun get(mail: String): User? {
        return database.get(mail)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClick() {
        if(isFormValid()) {
            statusToast.value = true
            addToDB()
        } else {
            statusToast.value = false
        }
    }

}