package com.example.temporario.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.temporario.Home.HomeActivity
import com.example.temporario.R
import com.example.temporario.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignIn: SignInButton
    private lateinit var gSignInOptions: GoogleSignInOptions
    private lateinit var gSignInClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser
        Log.d("LoginFragment", "User: $user")
        Toast.makeText(context, "Hello ${user!!.displayName}", Toast.LENGTH_SHORT).show()
        if (user != null) {
            goToHome(user)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        googleSignIn = binding.signInButton
        gSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.SERVER_CLIENT_WEB))
            .requestEmail()
            .build()

        gSignInClient = GoogleSignIn.getClient(requireContext(), gSignInOptions)

        binding.clickToRegister.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, RegisterFragment::class.java, null)
                .commit()
        }

        googleSignIn.setOnClickListener {
            goToSignIn()
        }
    }

    private fun goToSignIn() {
        val signInIntent = gSignInClient.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

       if (requestCode == 1000) {
           val task = GoogleSignIn.getSignedInAccountFromIntent(data)

           try {
               val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)

               val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
               firebaseAuth.signInWithCredential(credential)
                   .addOnCompleteListener {
                       if (it.isSuccessful) {
                           val user: FirebaseUser = firebaseAuth.currentUser!!
                           goToHome(user)
                       } else {
                           // no
                       }
                   }

           } catch (e: Exception) {
               Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
           }
       }
    }

    private fun goToHome(user: FirebaseUser) {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.putExtra("FirebaseUser", user)
        // TODO: sa mut signoutul in Home
        //gSignInClient.signOut()
        startActivity(intent)
    }



}