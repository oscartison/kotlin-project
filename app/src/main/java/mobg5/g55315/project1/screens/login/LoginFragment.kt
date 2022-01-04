package mobg5.g55315.project1.screens.login

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import mobg5.g55315.project1.R
import mobg5.g55315.project1.database.EmailDatabase
import mobg5.g55315.project1.databinding.FragmentLoginBinding


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var binding: FragmentLoginBinding


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = EmailDatabase.getInstance(application).emailDatabaseDao

        val viewModelFactory = LoginViewModelFactory(dataSource, application)

        viewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(LoginViewModel::class.java)

        binding.loginViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.statusToast.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                if (viewModel.statusToast.value == true) {
                    showToastMail()
                } else {
                    showFailedEmail()
                }

                viewModel.statusToast.value = null
            }
        })

        viewModel.emails.observe(viewLifecycleOwner, Observer { emails ->
            binding.editTextEmailAddress.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    emails
                )
            )
            binding.editTextEmailAddress.showDropDown()

        })

        // Inflate the layout for this fragment
        return binding.root

    }


    private fun showToastMail() {
        Toast.makeText(
            context, "Valid email address",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showFailedEmail() {
        Toast.makeText(
            context, "Invalid email address",
            Toast.LENGTH_SHORT
        ).show()
    }
}