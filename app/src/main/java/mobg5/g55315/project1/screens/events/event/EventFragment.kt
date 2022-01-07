package mobg5.g55315.project1.screens.events.event

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_event.*
import mobg5.g55315.project1.R
import mobg5.g55315.project1.databinding.FragmentEventBinding
import mobg5.g55315.project1.util.FirebaseUtil
import mobg5.g55315.project1.util.LiveDataInternetConnections

class EventFragment : Fragment() {
    private lateinit var cld : LiveDataInternetConnections

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentEventBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_event, container, false
        )
        val application = requireNotNull(this.activity).application
        cld =  LiveDataInternetConnections(application)
        cld.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                binding.imageView2.visibility = View.GONE
                binding.eventList.visibility = View.VISIBLE
                binding.floatingActionButton.visibility = View.VISIBLE

                showEvents(binding)
            } else {
                binding.imageView2.visibility = View.VISIBLE
                binding.eventList.visibility = View.GONE
                binding.floatingActionButton.visibility = View.GONE


            }
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showEvents(binding: FragmentEventBinding) {
        val dataSource = requireNotNull(FirebaseUtil.getFirestore())
        val viewModelFactory = EventViewModelFactory(dataSource)

        val eventViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(EventViewModel::class.java)


        binding.eventViewModel = eventViewModel

        val adapter = EventAdapter(EventListener { eventId ->
            eventViewModel.onEventClicked(eventId)
        })
        binding.eventList.adapter = adapter

        eventViewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tabLayout!!.selectedTabPosition == 0) {
                    eventViewModel.getEventsDatabaseUpcoming()
                } else {
                    eventViewModel.getEventsDatabasePast()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        binding.lifecycleOwner = this

        eventViewModel.navigateToEventDetail.observe(viewLifecycleOwner, Observer { event ->
            event?.let {

                this.findNavController().navigate(
                    EventFragmentDirections
                        .actionEventFragmentToEventDetailFragment(event)
                )
                eventViewModel.onEventDetailNavigated()
            }
        })

        eventViewModel.navigateToEventCreate.observe(viewLifecycleOwner, Observer {
            if (it == true) {

                this.findNavController().navigate(
                    EventFragmentDirections
                        .actionEventFragmentToEventCreateFragment()
                )
                eventViewModel.onEventCreateNavigated()
            }
        })


        val manager = LinearLayoutManager(context)
        binding.eventList.layoutManager = manager
    }

}