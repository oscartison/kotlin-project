package mobg5.g55315.project1.model


import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * represents an event in the online database
 */
@IgnoreExtraProperties
class Event {
    @DocumentId
    var id: String? = null
    var name: String? = null
    var time: String? = null
    var date: String? = null
    var teamId: String? = null
    var teamName: String? = null
    var creator: String? = null
    var photo: String? = null
    var participants: MutableList<String?> = mutableListOf()

    constructor()

    constructor(
        id: String?,
        name: String?,
        time: String?,
        date: String?,
        teamId: String?,
        teamName: String?,
        creator: String?,
        photo: String?,
        participants: MutableList<String?>
    ) {
        this.id = id
        this.name = name
        this.time = time
        this.date = date
        this.teamId = teamId
        this.teamName = teamName
        this.creator = creator
        this.photo = photo
        this.participants = participants
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (id != other.id) return false
        if (name != other.name) return false
        if (time != other.time) return false
        if (date != other.date) return false
        if (teamId != other.teamId) return false
        if (teamName != other.teamName) return false
        if (creator != other.creator) return false
        if (photo != other.photo) return false
        if (participants != other.participants) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (time?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + (teamId?.hashCode() ?: 0)
        result = 31 * result + (teamName?.hashCode() ?: 0)
        result = 31 * result + (creator?.hashCode() ?: 0)
        result = 31 * result + (photo?.hashCode() ?: 0)
        result = 31 * result + participants.hashCode()
        return result
    }
}
