package com.cg.lib.socialsignup

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cg.lib.base.CGCallback
import com.cg.lib.base.model.CGUserData
import com.cg.lib.socialloginlib.ui.login.Login
import com.cg.lib.socialloginlib.ui.login.LoginScreen
import com.cg.lib.socialloginlib.ui.theme.CGLibTheme

class MainActivity : Login() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(name = "") { value ->
                //socialLoginClickListener(value)
                when (value) {
                    0 -> {
                        Log.v("Click", "0")

                    }
                    1 -> {
                        Log.v("Click", "1")
                        loginWithGoogle(object : CGCallback {
                            override fun onSuccess(user: CGUserData) {
                                Log.v("RESULT", user.name)
                            }

                            override fun onError(th: Throwable) {
                                Log.e("ERROR", th.message.toString())

                            }
                        })
                    }
                    2 -> {
                        Log.v("Click", "2")
                        loginWithFacebook(object : CGCallback {
                            override fun onSuccess(user: CGUserData) {
                                Log.v("RESULT", user.token)
                            }

                            override fun onError(th: Throwable) {
                                Log.e("ERROR", th.message.toString())
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
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CGLibTheme {
        LoginScreen("Android", { })
    }
}