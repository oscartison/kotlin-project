package mobg5.g55315.project1

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import mobg5.g55315.project1.database.EmailDatabase
import mobg5.g55315.project1.database.EmailDatabaseDao
import mobg5.g55315.project1.model.User
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class EmailDatabaseTest {
    private lateinit var emailDao: EmailDatabaseDao
    private lateinit var db: EmailDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, EmailDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        emailDao = db.emailDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetUser() {
        runBlocking {
            val user = User("test@email.com", "12-10-2010", "10:20:20")
            emailDao.insert(user)
            val userget = emailDao.get("test@email.com")
            assertEquals(userget?.userEmail, "test@email.com")
        }
    }


    @Test
    @Throws(Exception::class)
    fun updateAndGetUser() {
        runBlocking {
            val user = User("test@email.com", "12-10-2010", "10:20:20")
            emailDao.insert(user)
            user.dateCreation = "22-10-2021"
            emailDao.update(user)
            val userget = emailDao.get("test@email.com")
            assertEquals(userget?.dateCreation, "22-10-2021")
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteUser() {
        runBlocking {
            val user = User("test@email.com", "12-10-2010", "10:20:20")
            emailDao.insert(user)
            user.dateCreation = "22-10-2021"
            emailDao.clear()
            val userget = emailDao.get("test@email.com")
            assertNull(userget)
        }
    }
}