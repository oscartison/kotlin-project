package mobg5.g55315.project1.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * represents the category of a team in the database
 */
@IgnoreExtraProperties
class Category {
    @DocumentId
    var id: String? = null
    var category: String? = null

    constructor()

    constructor(
        category: String?
    ) {
        this.category = category
    }

}