package com.cg.lib.google_login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.cg.lib.base.CGCallback
import com.cg.lib.base.GoogleLoginManager
import com.cg.lib.base.RC_SIGN_IN
import com.cg.lib.base.model.CGUserData
import com.cg.lib.google_login.ui.theme.SocialSignupTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch

/**
 * Login activity with composable
 * By extending this activity you can configure the google login simplyfied
 * Created by Ikram on 22-03-2023.
 * * Copyright (c) 2022 Siemens. All rights reserved.
 * */
open class Login : ComponentActivity(), GoogleLoginManager {
    private var socialLoginResultCallback: CGCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocialSignupTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            Log.v("GOOGLE RESULT", "Result....................")
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            lifecycleScope.launch {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }
    }

    /**
     * Method to get logged in with google platform
     * @param callback -> callback responsible to return the result as logged in with google
     * */
    override fun loginWithGoogle(callback: CGCallback) {
        if (socialLoginResultCallback == null)
            socialLoginResultCallback = callback
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {

            val loginResult = CGUserData(
                id = account.id,
                token = account.idToken ?: "",
                name = account.displayName ?: "",
                birthday = "",
                email = account.email ?: "",
                profilePic = account.photoUrl,
                address = ""
            )
            returnUserData(loginResult)
        } else {
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            val gso: GoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken("533681224412-ntk5u60aa7e1ml55hn4ve3pad7tq6mdm.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
            // Build a GoogleSignInClient with the options specified by gso.
            val mGoogleSignInClient = GoogleSignIn.getClient(baseContext, gso);
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    /**
     * This method to handle the google loging result
     * And process the return data in callback
     * @param completedTask->Task<GoogleSignInAccount> coming from google signing
     * */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            if (completedTask.isSuccessful) {
                val account = completedTask.getResult(ApiException::class.java)
                account?.let {
                    // Signed in successfully, show authenticated UI.
                    Log.v("GOOGLE RESULT2", account.displayName.toString())
                    val loginResult = CGUserData(
                        id = account.id,
                        token = account.idToken ?: "",
                        name = account.displayName ?: "",
                        birthday = "",
                        email = account.email ?: "",
                        profilePic = account.photoUrl,
                        address = ""
                    )
                    returnUserData(loginResult)
                }
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
        }
    }

    /**
     * This method will return the result to chield activity using the socialLoginResultCallback
     * @param user-> UserData model having loggedin user details
     * */
    private fun returnUserData(user: CGUserData) {

        socialLoginResultCallback?.onSuccess(user)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SocialSignupTheme {
        Greeting("Android")
    }
}