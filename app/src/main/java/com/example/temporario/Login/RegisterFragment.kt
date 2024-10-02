package com.example.temporario.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.temporario.R
import com.example.temporario.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.goToLogin.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, LoginFragment::class.java, null)
                .commit()
        }

        binding.registerButton.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val retypePassword = binding.retypePassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && retypePassword.isNotEmpty()) {
                if (password == retypePassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if (it.isSuccessful) {
                            val user = firebaseAuth.currentUser
//                            val profileUpdates = UserProfileChangeRequest.Builder()
//                                .setDisplayName(name)
//                                .build()
//                            user?.updateProfile(profileUpdates)

                            requireActivity().supportFragmentManager.beginTransaction()
                                .add(R.id.fragment_container, LoginFragment::class.java, null)
                                .commit()
                        } else {
                            Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}