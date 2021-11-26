package mobg5.g55315.project1.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import mobg5.g55315.project1.model.User

@Dao
interface EmailDatabaseDao {
    @Insert
    suspend fun insert(email: User)

    @Update
    suspend fun update(email: User)

    @Query("SELECT * from users_database WHERE userEmail = :key")
    suspend fun get(key: String): User?

    @Query("DELETE FROM users_database")
    suspend fun clear()

    @Query("SELECT * FROM users_database ORDER BY userEmail DESC")
    fun getAllEmails(): LiveData<List<User>>
}