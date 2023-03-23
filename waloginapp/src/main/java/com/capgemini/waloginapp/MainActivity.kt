package com.capgemini.waloginapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.capgemini.walogin.Login
import com.cg.lib.base.CGCallback
import com.cg.lib.base.model.CGUserData

class MainActivity : Login() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn).setOnClickListener(View.OnClickListener {
            loginWithWhatsApp("https://capgemini.authlink.me","ae8qc1zz","xfxmjs79qqz5ovyt",object :
                CGCallback {
                override fun onSuccess(user: CGUserData) {
                    Log.v("RESULT", user.token)
                }

                override fun onError(th: Throwable) {
                    Log.e("ERROR", th.message.toString())
                }
            })
        })
    }
}