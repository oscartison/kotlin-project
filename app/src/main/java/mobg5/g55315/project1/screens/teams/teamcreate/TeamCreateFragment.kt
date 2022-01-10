package mobg5.g55315.project1.screens.teams.teamcreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_event_creation.*
import kotlinx.android.synthetic.main.fragment_team_creation.*
import mobg5.g55315.project1.R
import mobg5.g55315.project1.databinding.FragmentTeamCreationBinding
import mobg5.g55315.project1.util.FirebaseUtil
import java.util.*

/**
 * in this fragment a user will be able to create a team
 */
class TeamCreateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTeamCreationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_team_creation, container, false
        )


        val dataSource = requireNotNull(FirebaseUtil.getFirestore())
        val viewModelFactory = TeamCreateViewModelFactory(dataSource)


        val teamCreateViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(TeamCreateViewModel::class.java)

        binding.teamCreateViewModel = teamCreateViewModel


        teamCreateViewModel.categories.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { categories ->
                binding.categoriesTeam.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        categories
                    )
                )
            })


        binding.lifecycleOwner = this

        teamCreateViewModel.navigateBack.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                    TeamCreateFragmentDirections.actionTeamCreateFragmentToTeamFragment()
                )
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                teamCreateViewModel.doneNavigating()
            }
        })

        teamCreateViewModel.statusToast.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { status ->
                status?.let {
                    if (teamCreateViewModel.statusToast.value == true) {
                        Toast.makeText(
                            context, "Team was created succesfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context, "Please fill every field",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    teamCreateViewModel.statusToast.value = null
                }
            })

        return binding.root

    }
}