package com.cg.lib.fb_login

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
import com.cg.lib.base.CGCallback
import com.cg.lib.base.FBLoginManager
import com.cg.lib.base.model.CGUserData
import com.cg.lib.fb_login.ui.theme.SocialSignupTheme
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
/**
 * Login activity with composable
 * By extending this activity you can configure the facebook login simplified
 * Created by Ikram on 22-03-2023.
 * * Copyright (c) 2022 Siemens. All rights reserved.
 * */
open class Login : ComponentActivity(), FBLoginManager {
    lateinit var callbackManager: CallbackManager
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
        callbackManager = CallbackManager.Factory.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
/**
 * Method to get logged in with facebook platform
 * @param callback -> callback responsible to return the result as logged in with facebook
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