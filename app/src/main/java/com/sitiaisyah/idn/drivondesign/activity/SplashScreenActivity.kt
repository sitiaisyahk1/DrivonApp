package com.sitiaisyah.idn.drivondesign.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.sitiaisyah.idn.drivondesign.MainActivity
import com.sitiaisyah.idn.drivondesign.R
import org.jetbrains.anko.startActivity

class SplashScreenActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash__screen)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        Handler().postDelayed(Runnable {
            if (auth?.currentUser?.displayName != null){
                startActivity<MainActivity>()
            } else startActivity<LoginActivity>()
        }, 3000)
    }
}