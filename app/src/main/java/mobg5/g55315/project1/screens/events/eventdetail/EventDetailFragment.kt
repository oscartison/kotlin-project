package mobg5.g55315.project1.screens.events.eventdetail

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
import mobg5.g55315.project1.databinding.FragmentEventDetailBinding
import mobg5.g55315.project1.util.FirebaseUtil


class EventDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentEventDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_event_detail, container, false
        )

        val arguments = EventDetailFragmentArgs.fromBundle(arguments)

        // Create an instance of the ViewModel Factory.
        val dataSource = requireNotNull(FirebaseUtil.getFirestore())
        val viewModelFactory = EventDetailViewModelFactory(arguments.eventKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.

        val eventDetailViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(EventDetailViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.eventDetailViewModel = eventDetailViewModel

        eventDetailViewModel.event.observe(viewLifecycleOwner, Observer { event ->
            val adapter =
                EventDetailAdapter(PersonListener { personId ->
                    eventDetailViewModel.addParticipantToEvent(personId)
                }, event)

            binding.participantList.adapter = adapter

            eventDetailViewModel.getPersonsDatabase()

            eventDetailViewModel.persons.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })
        })

        binding.lifecycleOwner = this

        eventDetailViewModel.navigateToEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                    EventDetailFragmentDirections.actionEventDetailFragmentToEventFragment()
                )
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                eventDetailViewModel.doneNavigating()
            }
        })

        val manager = LinearLayoutManager(context)
        binding.participantList.layoutManager = manager

        return binding.root
    }


}