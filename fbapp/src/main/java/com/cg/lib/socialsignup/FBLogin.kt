package com.cg.lib.socialsignup

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cg.lib.base.CGCallback
import com.cg.lib.base.model.CGUserData
import com.cg.lib.google_login.Login
import com.cg.lib.socialsignup.ui.theme.SocialSignupTheme

class FBLogin : Login() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocialSignupTheme {
                MainScreen { eventType ->
                    when (eventType) {
                        1 -> {
                            Log.v("Click", "2")
                            loginWithGoogle(object : CGCallback {
                                override fun onSuccess(user: CGUserData) {
                                    Log.v("RESULT", user.token)
                                }

                                override fun onError(th: Throwable) {
                                    Log.e("ERROR", th.message.toString())
                                }
                            })
                        }
                        /*2 -> {
                            Log.v("Click", "2")
                            loginWithFacebook(object : CGCallback {
                                override fun onSuccess(user: CGUserData) {
                                    Log.v("RESULT", user.token)
                                }

                                override fun onError(th: Throwable) {
                                    Log.e("ERROR", th.message.toString())
                                }
                            })
                        }*/
                        /*3 -> {
                            Log.v("Click", "3")
                            loginWithWhatsapp("https://capgemini.authlink.me","ae8qc1zz","xfxmjs79qqz5ovyt",object : CGCallback {
                                override fun onSuccess(user: CGUserData) {
                                    Log.v("RESULT", user.token)
                                }

                                override fun onError(th: Throwable) {
                                    Log.e("ERROR", th.message.toString())
                                }
                            })
                        }*/
                        else -> {

                        }
                    }
                }
            }
        }

    }
}

@Composable
fun MainScreen(socialLogin: (Int) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background

    ) {
        Button(
            onClick = {
                socialLogin(1)
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentHeight()
        ) {
            Text(text = "FB Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SocialSignupTheme {
        MainScreen({})
    }
}