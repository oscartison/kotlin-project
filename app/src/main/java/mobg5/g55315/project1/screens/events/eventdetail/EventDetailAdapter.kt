package mobg5.g55315.project1.screens.events.eventdetail


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mobg5.g55315.project1.databinding.ListItemParticipantBinding
import mobg5.g55315.project1.model.Event
import mobg5.g55315.project1.model.Person

/**
 * this adapter will populate a recyclerview with the users of a team of a certain event
 */
class EventDetailAdapter(val clickListener: PersonListener, val event: Event) :
    ListAdapter<Person, EventDetailAdapter.ViewHolder>(EventDetailDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, event, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemParticipantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Person, event: Event, clickListener: PersonListener) {
            binding.person = item
            binding.event = event
            binding.executePendingBindings()
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemParticipantBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class EventDetailDiffCallback : DiffUtil.ItemCallback<Person>() {

    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }
}

class PersonListener(val clickListener: (personKey: String) -> Unit) {
    fun onClick(person: Person) = clickListener(person.id!!)
}
