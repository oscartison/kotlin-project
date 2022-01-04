package mobg5.g55315.project1.screens.teams.teamdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mobg5.g55315.project1.databinding.ListItemMembersBinding
import mobg5.g55315.project1.model.Person
import mobg5.g55315.project1.model.Team

class TeamDetailAdapter(val clickListener: PersonListener, val team: Team) :
    ListAdapter<Person, TeamDetailAdapter.ViewHolder>(TeamDetailDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, team, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemMembersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Person, team: Team, clickListener: PersonListener) {
            binding.person = item
            binding.team = team
            binding.executePendingBindings()
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMembersBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class TeamDetailDiffCallback : DiffUtil.ItemCallback<Person>() {
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


