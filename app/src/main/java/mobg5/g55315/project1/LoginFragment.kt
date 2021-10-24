package mobg5.g55315.project1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import mobg5.g55315.project1.databinding.FragmentLoginBinding


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater, R.layout.fragment_login, container, false)

        binding.connectButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        {
            view ->
            val email = binding.editTextTextEmailAddress.text.toString()
            if (email.matches(emailPattern.toRegex())) {
                Toast.makeText(context, "Valid email address",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Invalid email address",
                    Toast.LENGTH_SHORT).show()
            }

        }
        // Inflate the layout for this fragment
        return binding.root

    }
}