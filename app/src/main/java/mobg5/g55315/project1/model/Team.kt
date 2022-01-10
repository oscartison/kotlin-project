package mobg5.g55315.project1.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * This class represents a team in the app
 */
@IgnoreExtraProperties
class Team {
    @DocumentId
    var id: String? = null
    var name: String? = null
    var sport: String? = null
    var members: MutableList<String?> = mutableListOf()
    var creator: String? = null

    constructor()

    constructor(name: String?, sport: String?, members: MutableList<String?>, creator: String?) {
        this.name = name
        this.sport = sport
        this.members = members
        this.creator = creator
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Team

        if (id != other.id) return false
        if (name != other.name) return false
        if (sport != other.sport) return false
        if (members != other.members) return false
        if (creator != other.creator) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (sport?.hashCode() ?: 0)
        result = 31 * result + members.hashCode()
        result = 31 * result + (creator?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "$name"
    }
}