package mobg5.g55315.project1.screens.events.eventdetail

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_event_creation.*
import kotlinx.android.synthetic.main.fragment_event_detail.*
import mobg5.g55315.project1.R
import mobg5.g55315.project1.databinding.FragmentEventDetailBinding
import mobg5.g55315.project1.util.FirebaseUtil
import java.io.File

private const val FILE_NAME = "event_photo"
private lateinit var photoFile: File

class EventDetailFragment : Fragment() {

    private lateinit var meventDetailViewModel: EventDetailViewModel

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
        this.meventDetailViewModel = eventDetailViewModel

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

        binding.eventImage.setOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)
            val fileProvider = FileProvider.getUriForFile(
                requireContext(),
                "mobg5.g55315.project1.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (this.requireActivity().packageManager != null) {
                getResult.launch(takePictureIntent)
            } else {
                Toast.makeText(requireContext(), "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }


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
    private fun getPhotoFile(fileName: String): File {
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
        event_image.setImageBitmap(takenImage)
        meventDetailViewModel.uploadImage(photoFile.absolutePath)
    }

}