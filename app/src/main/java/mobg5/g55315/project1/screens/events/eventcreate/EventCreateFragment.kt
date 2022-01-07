package mobg5.g55315.project1.screens.events.eventcreate

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_event_creation.*
import mobg5.g55315.project1.R
import mobg5.g55315.project1.databinding.FragmentEventCreationBinding
import mobg5.g55315.project1.model.Team
import mobg5.g55315.project1.util.FirebaseUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val FILE_NAME = "event_photo.jpg"
private lateinit var photoFile: File

class EventCreateFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEventCreationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_event_creation, container, false
        )

        val dataSource = requireNotNull(FirebaseUtil.getFirestore())
        val viewModelFactory = EventCreateViewModelFactory(dataSource)

        var cal = Calendar.getInstance()

        val eventCreateViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(EventCreateViewModel::class.java)

        binding.eventCreateViewModel = eventCreateViewModel



        eventCreateViewModel.progress.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) { // Observed state is true.
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE

            }
        }
        )

        eventCreateViewModel.teams.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { teams ->
                binding.teamText.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        teams
                    )
                )
            })

        binding.teamText.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as Team
            eventCreateViewModel.eventTeam.value = selectedItem
        }


        val dateListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val format = "dd/MM/yyyy"
                var sdf = SimpleDateFormat(format, Locale.FRANCE)
                editTextDate.text = sdf.format(cal.time)
            }
        }

        binding.editTextDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    context!!, dateListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        })

        val timeListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                val format = "HH:mm"
                var stf = SimpleDateFormat(format, Locale.FRANCE)
                editTextTime.text = stf.format(cal.time)


            }
        }

        binding.editTextTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                TimePickerDialog(
                    context!!, timeListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            }
        })

        binding.imageView.setOnClickListener {

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
                eventCreateViewModel.eventImage = photoFile.absolutePath
            } else {
                Toast.makeText(requireContext(), "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }

        binding.lifecycleOwner = this

        eventCreateViewModel.navigateBack.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                    EventCreateFragmentDirections.actionEventCreateFragmentToEventFragment()
                )
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                eventCreateViewModel.doneNavigating()
            }
        })

        eventCreateViewModel.statusToast.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { status ->
                status?.let {
                    if (eventCreateViewModel.statusToast.value == true) {
                        Toast.makeText(
                            context, "Game was created succesfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context, "Please fill every field",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    eventCreateViewModel.statusToast.value = null
                }
            })

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
        imageView.setImageBitmap(takenImage)
    }
}