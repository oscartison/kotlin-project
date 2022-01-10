package mobg5.g55315.project1.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 */
@Entity(tableName = "users_database")
data class User(
    @PrimaryKey
    val userEmail: String,

    @ColumnInfo(name = "date_creation")
    var dateCreation: String,

    @ColumnInfo(name = "time_creation")
    var timeCreation: String,
)
