package mobg5.g55315.project1.screens.events.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mobg5.g55315.project1.databinding.ListItemEventBinding
import mobg5.g55315.project1.model.Event

/**
 * This adapter will populate a recyclerview with a list of events
 */
class EventAdapter(val clickListener: EventListener) :
    ListAdapter<Event, EventAdapter.ViewHolder>(EventDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: ListItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Event, clickListener: EventListener) {
            binding.event = item
            binding.executePendingBindings()
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemEventBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class EventDiffCallback : DiffUtil.ItemCallback<Event>() {

    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}

class EventListener(val clickListener: (eventKey: String) -> Unit) {
    fun onClick(event: Event) = clickListener(event.id!!)

}
