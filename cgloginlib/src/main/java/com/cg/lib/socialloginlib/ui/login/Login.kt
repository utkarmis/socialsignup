package com.cg.lib.socialloginlib.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.cg.lib.base.CGCallback
import com.cg.lib.base.GFLoginManager
import com.cg.lib.base.RC_SIGN_IN
import com.cg.lib.base.SocialLoginManager
import com.cg.lib.base.model.CGUserData
import com.cg.lib.socialloginlib.R
import com.cg.lib.socialloginlib.ui.theme.CGLibTheme
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch

/**
 * Login activity with composable
 * By extending this activity you can configure the google and facebook login simplyfied
 * Created by Ikram on 22-03-2023.
 * * Copyright (c) 2022 Siemens. All rights reserved.
 * */
open class Login : ComponentActivity(), GFLoginManager {
    lateinit var callbackManager: CallbackManager
    private var socialLoginResultCallback: CGCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(name = "Android") { value ->
                //socialLoginClickListener(value)
                when (value) {
                    0 -> {
                        Log.v("Click", "0")

                    }
                    1 -> {
                        Log.v("Click", "1")
                        loginWithGoogle(object : CGCallback {
                            override fun onSuccess(user: CGUserData) {

                            }

                            override fun onError(th: Throwable) {

                            }
                        })
                    }
                    2 -> {
                        Log.v("Click", "2")
                        loginWithFacebook(object : CGCallback {
                            override fun onSuccess(user: CGUserData) {

                            }

                            override fun onError(th: Throwable) {

                            }
                        })
                    }
                    3 -> {
                        Log.v("Click", "3")
                        //initTwitter()
                    }
                    else -> {

                        Log.v("Click", "other")
                    }
                }
            }
        }

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

    override fun returnUserData(user: CGUserData) {

        socialLoginResultCallback?.onSuccess(user)
    }

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
}

@Composable
fun LoginScreen(name: String, socialLogin: (Int) -> Unit) {
    CGLibTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    BrandLogo()
                }
                LoginCard(socialLogin)
                SocialLogin(socialLogin)
            }
        }
    }
}

@Composable
fun BrandLogo() {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
            .padding(8.dp),
        shape = CircleShape,
        elevation = 8.dp
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.logo),
            contentDescription = "Image Description",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Crop,

            )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CGLibTheme {
        LoginScreen("Android", { })
    }
}