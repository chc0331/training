package com.example.myapplication.lottoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.airbnb.lottie.LottieAnimationView

class SplashActivity : AppCompatActivity() {

    private lateinit var animationView: LottieAnimationView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        animationView = findViewById(R.id.animationView)

        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }

        handler.postDelayed(runnable, 3000)

        animationView.setOnClickListener {
            handler.removeCallbacks(runnable)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }

    }
}