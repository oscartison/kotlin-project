package mobg5.g55315.project1.screens.teams.teamdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import mobg5.g55315.project1.R
import mobg5.g55315.project1.databinding.FragmentTeamDetailBinding
import mobg5.g55315.project1.util.FirebaseUtil

/**
 * this fragment will show the details of a certain team. In this fragment the creator of a team
 * will be able to add users to his team
 */
class TeamDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentTeamDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_team_detail, container, false
        )

        val arguments = TeamDetailFragmentArgs.fromBundle(arguments)

        // Create an instance of the ViewModel Factory.
        val dataSource = requireNotNull(FirebaseUtil.getFirestore())
        val viewModelFactory = TeamDetailViewModelFactory(arguments.teamKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val teamDetailViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(TeamDetailViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.teamDetailViewModel = teamDetailViewModel

        teamDetailViewModel.team.observe(viewLifecycleOwner, Observer { team ->
            val adapter =
                TeamDetailAdapter(PersonListener { personId ->
                    teamDetailViewModel.addParticipantToEvent(personId)
                }, team)

            binding.participantList.adapter = adapter

            teamDetailViewModel.persons.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })
        })



        binding.lifecycleOwner = this

        teamDetailViewModel.navigateToTeam.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                    TeamDetailFragmentDirections.actionTeamDetailFragmentToTeamFragment()
                )
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                teamDetailViewModel.doneNavigating()
            }
        })

        val manager = LinearLayoutManager(context)
        binding.participantList.layoutManager = manager

        return binding.root
    }


}