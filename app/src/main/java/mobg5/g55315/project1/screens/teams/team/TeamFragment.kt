package mobg5.g55315.project1.screens.teams.team

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
import kotlinx.android.synthetic.main.fragment_event.*
import mobg5.g55315.project1.R
import mobg5.g55315.project1.databinding.FragmentTeamBinding
import mobg5.g55315.project1.util.FirebaseUtil
import mobg5.g55315.project1.util.LiveDataInternetConnections

class TeamFragment : Fragment() {
    private lateinit var cld : LiveDataInternetConnections


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTeamBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_team, container, false
        )
        val application = requireNotNull(this.activity).application
        cld =  LiveDataInternetConnections(application)
        cld.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                binding.imageView2.visibility = View.GONE
                binding.teamList.visibility = View.VISIBLE
                binding.floatingActionButton.visibility = View.VISIBLE

                val dataSource = requireNotNull(FirebaseUtil.getFirestore())
                val viewModelFactory = TeamViewModelFactory(dataSource)

                val teamViewModel =
                    ViewModelProvider(
                        this, viewModelFactory
                    ).get(TeamViewModel::class.java)


                binding.teamViewModel = teamViewModel

                val adapter = TeamAdapter(TeamListener { team ->
                    teamViewModel.onTeamClicked(team)
                })
                binding.teamList.adapter = adapter


                teamViewModel.teams.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        adapter.submitList(it)
                    }
                })


                binding.lifecycleOwner = this

                teamViewModel.navigateToEventDetail.observe(this, Observer { team ->
                    team?.let {

                        this.findNavController().navigate(
                            TeamFragmentDirections
                                .actionTeamFragmentToTeamDetailFragment(team)
                        )
                        teamViewModel.onTeamDetailNavigated()
                    }
                })

                teamViewModel.navigateToTeamtCreate.observe(viewLifecycleOwner, Observer {
                    if (it == true) {
                        this.findNavController().navigate(
                            TeamFragmentDirections
                                .actionTeamFragmentToTeamCreateFragment()
                        )
                        teamViewModel.onTeamCreateNavigated()
                    }
                })

                val manager = LinearLayoutManager(context)
                binding.teamList.layoutManager = manager
            } else {
                binding.imageView2.visibility = View.VISIBLE
                binding.teamList.visibility = View.GONE
                binding.floatingActionButton.visibility = View.GONE

            }}

        return binding.root
    }
}