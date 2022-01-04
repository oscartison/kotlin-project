package mobg5.g55315.project1.screens.teams.team

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mobg5.g55315.project1.databinding.ListItemTeamBinding
import mobg5.g55315.project1.model.Team

class TeamAdapter(val clickListener: TeamListener) :
    ListAdapter<Team, TeamAdapter.ViewHolder>(TeamDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: ListItemTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Team, clickListener: TeamListener) {
            binding.team = item
            binding.executePendingBindings()
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTeamBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class TeamDiffCallback : DiffUtil.ItemCallback<Team>() {

    override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
        return oldItem == newItem
    }
}

class TeamListener(val clickListener: (teamKey: String) -> Unit) {
    fun onClick(team: Team) = clickListener(team.id!!)
}
