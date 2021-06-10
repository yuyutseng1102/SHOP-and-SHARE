package com.chloe.shopshare.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentLoginBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.util.UserManager
import com.chloe.shopshare.util.Util
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    val RC_SIGN_IN = 1
    private val viewModel by viewModels<LoginViewModel> { getVmFactory() }
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentLoginBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        googleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Util.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn
            .getClient(requireContext(), googleSignInOptions)

        binding.signInGoogleButton.setOnClickListener {
            googleLogin()
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            Log.d("Login", "user = ${viewModel.user.value}" )
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToHomeFragment())
            }
        })



// Configure Google Sign In



        return binding.root
    }

    fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val user = viewModel.auth.currentUser
        Log.i("Login","currentUser = $user")
        if (user != null) {
            UserManager.userId = user.uid
            findNavController().navigate(NavigationDirections.navigateToHomeFragment())
        } else {
            // No user is signed in
            Log.i("Login","No user is signed in")
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            data?.let {
                viewModel.signIn(it)
            }
        }
    }



}