package mobg5.g55315.project1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mobg5.g55315.project1.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class EmailDatabase : RoomDatabase() {

    abstract val emailDatabaseDao: EmailDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: EmailDatabase? = null

        fun getInstance(context: Context): EmailDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EmailDatabase::class.java,
                        "users_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}