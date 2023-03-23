package com.cg.lib.fbgwalogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.cg.lib.base.CGCallback
import com.cg.lib.base.RC_SIGN_IN
import com.cg.lib.base.SocialLoginManager
import com.cg.lib.base.model.CGUserData
import com.cg.lib.whatsappbase.LoginViewModel
import com.cg.lib.whatsappbase.network.RemoteDataSource
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.otpless.views.OtplessManager
import kotlinx.coroutines.launch
/**Login Can be extended in your own activity to get the feature of
 * Social login like as Google, Facebook & Whatsapp
 * By extending this activity you can configure the google, facebook and whatsapp login simplyfied
 * Created by Ikram on 22-03-2023.
 * * Copyright (c) 2022 Siemens. All rights reserved.
 * */
open class Login : AppCompatActivity(), SocialLoginManager {
    lateinit var callbackManager: CallbackManager
    private var socialLoginResultCallback: CGCallback? = null
    private var waID: String = ""
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        OtplessManager.getInstance().init(this)
        callbackManager = CallbackManager.Factory.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != RC_SIGN_IN)
            callbackManager.onActivityResult(requestCode, resultCode, data)
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

                Log.v("GOOGLE RESULT", "000000")
                handleSignInResult(task)
            }
        }
    }
/**
 * This will handle the signin/login result
 * @param completedTask-> Task<GoogleSignInAccount> while login with google
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
 * This returns the result of login via any of platform like Google/FB
 * @param user-> logged in user model having login details
 * */
    override fun returnUserData(user: CGUserData) {

        socialLoginResultCallback?.onSuccess(user)
    }

    /**
     * Overridden method to login with googl,
     * Simply invoke this method to get user logged in via googl
     * @param callback-> A callback param of class CGCallback
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
 * Overridden method to login with facebook,
 * Simply invoke this method to get user logged in via facebook
 * @param callback-> A callback param of class CGCalleback
 * */
    override fun loginWithFacebook(callback: CGCallback) {
        if (socialLoginResultCallback == null)
            socialLoginResultCallback = callback
        if (!FacebookSdk.isInitialized())
            return
        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
        LoginManager.getInstance().logInWithReadPermissions(this, arrayListOf("email"))
        Log.v("FB RESULT000", "loginResult.accessToken.toString()")
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.v("FB RESULT", loginResult.accessToken.token)
                    val loginResult = CGUserData(
                        id = loginResult.accessToken.userId,
                        token = loginResult.accessToken.token ?: "",
                        name = loginResult.accessToken.source.name,
                        birthday = "",
                        email = "",
                        profilePic = null,
                        address = ""
                    )
                    socialLoginResultCallback?.onSuccess(loginResult)

                }

                override fun onCancel() {
                    Log.v("FB RESULT", "Canceled")

                }

                override fun onError(exception: FacebookException) {
                    Log.v("FB RESULT", exception.message.toString())

                }
            })
    }


    /**
     * Overridden method to login with whatsApp,
     * Simply invoke this method to get user logged in via whatsApp
     * @param authLink -> authlink generated on OTPLess platform as in button
     * @param clientId -> client ID will be received on register email from OTPLess domain
     * @param clientSecret -> client Secret will be received on register email from OTPLess domain
     * @param callback-> A callback param of class CGCallback
     * */
    override fun loginWithWhatsApp(
        authLink: String,
        clientId: String,
        clientSecret: String,
        callback: CGCallback?
    ) {
        if (socialLoginResultCallback == null)
            socialLoginResultCallback = callback
        OtplessManager.getInstance().launch(this, authLink) { response ->
            response?.let {
                waID = it.waId
                Log.v("WhatsAppID", waID)
                val loginService = RemoteDataSource(authLink).buildApi(
                    com.cg.lib.whatsappbase.WAService::class.java,
                    baseContext
                )
                viewModel.getUserDetails(loginService, it.waId, clientId, clientSecret)
            }
        }
    }
/**
 * Button event listener (google, facebook, whatsapp)
 * */
    fun onViewClick(view: View) {
        when (view.id) {
            R.id.btn_google -> {
                loginWithGoogle(object : CGCallback {
                    override fun onSuccess(user: CGUserData) {
                        Log.v("GOOGLE-RESULT", user.token)
                    }

                    override fun onError(th: Throwable) {
                        Log.v("GOOGLE-ERROR", th.message.toString())
                    }

                })
            }
            R.id.btn_fb -> {
                loginWithFacebook(object : CGCallback {
                    override fun onSuccess(user: CGUserData) {
                        Log.v("FACEBOOK-RESULT", user.token)
                    }

                    override fun onError(th: Throwable) {
                        Log.v("FACEBOOK-ERROR", th.message.toString())
                    }

                })
            }
            R.id.btn_wa -> {
                loginWithWhatsApp(
                    "https://capgemini.authlink.me",
                    "ae8qc1zz",
                    "xfxmjs79qqz5ovyt",
                    object : CGCallback {
                        override fun onSuccess(user: CGUserData) {
                            Log.v("WHATSAPP-RESULT", user.token)
                        }

                        override fun onError(th: Throwable) {
                            Log.v("WHATSAPP-ERROR", th.message.toString())
                        }

                    })
            }
        }
    }
}