package mobg5.g55315.project1.util


import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import mobg5.g55315.project1.model.Event
import mobg5.g55315.project1.model.Person
import mobg5.g55315.project1.model.Team

@BindingAdapter("eventTeam")
fun TextView.setCategory(item: Event?) {
    item?.let {
        text = item.teamName
    }
}

@BindingAdapter("eventName")
fun TextView.setNameString(item: Event?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("eventDateTime")
fun TextView.setDateTimeString(item: Event?) {
    item?.let {
        var textToDisplay = item.date + " - " + item.time
        text = textToDisplay
    }
}

@BindingAdapter("eventDate")
fun TextView.setDateString(item: Event?) {
    item?.let {
        text = item.date.toString()
    }
}

@BindingAdapter("eventTime")
fun TextView.setTimeString(item: Event?) {
    item?.let {
        text = item.time.toString()
    }
}

@BindingAdapter("eventImage")
fun ImageView.setDateString(item: Event?) {
    item?.let {
        var filename = item.id.toString()

        val storage = FirebaseStorage.getInstance()

        var gsReference =
            storage.getReferenceFromUrl("gs://teammaker-9e041.appspot.com/images/$filename")

        val ONE_MEGABYTE: Long = 1024 * 1024

        gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { data ->
            Glide.with(this).load(data).into(this)
        }.addOnFailureListener {
            gsReference =
                storage.getReferenceFromUrl("gs://teammaker-9e041.appspot.com/images/teammaker.png")
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { data ->
                Glide.with(this).load(data).into(this)
            }
        }
    }
}

@BindingAdapter("participantName")
fun TextView.setName(item: Person?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("participantEmail")
fun TextView.setEmail(item: Person?) {
    item?.let {
        text = item.email
    }
}

@BindingAdapter("eventColor", "personColor")
fun CardView.setColor(event: Event?, item: Person?) {
    item?.let {
        if (event != null) {
            if (event.participants.contains(item.id)) {
                setBackgroundColor(Color.GREEN)
            } else if (event.creator == item.id) {
                setBackgroundColor(Color.MAGENTA)
            } else {
                setBackgroundColor(Color.RED)
            }
        }
    }
}

@BindingAdapter("teamName")
fun TextView.setTeamName(item: Team?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("teamCategory")
fun TextView.setTeamCategory(item: Team?) {
    item?.let {
        text = item.sport
    }
}

@BindingAdapter("teamCapitain")
fun TextView.setTeamCatain(item: Team?) {
    item?.let {
        text = item.creator
    }
}

@BindingAdapter("teamColor", "personTeamColor")
fun CardView.setColorTeam(team: Team?, item: Person?) {
    item?.let {
        if (team != null) {
            if (team.creator == item.id) {
                setBackgroundColor(Color.MAGENTA)
            } else if (team.members.contains(item.id)) {
                setBackgroundColor(Color.GREEN)
            } else {
                setBackgroundColor(Color.RED)
            }
        }
    }
}





