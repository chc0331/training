package com.example.myapplication.lottoapp

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lotteryButton : LottieAnimationView = findViewById(R.id.animationView)

        val lotteryNumbers = arrayListOf(
            (findViewById(R.id.number1) as TextView), (findViewById(R.id.number2) as TextView), (findViewById(R.id.number3) as TextView),
            (findViewById(R.id.number4) as TextView), (findViewById(R.id.number5) as TextView), (findViewById(R.id.number6) as TextView)
        )

        val countDownTimer = object : CountDownTimer(3000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                lotteryNumbers.forEach {
                    val randomNumber = (Math.random() * 45 + 1).toInt()
                    it.text = "$randomNumber"
                }
            }

            override fun onFinish() {
            }
        }


        lotteryButton.setOnClickListener {
            if (lotteryButton.isAnimating) {
                lotteryButton.cancelAnimation()
                countDownTimer.cancel()
            } else {
                lotteryButton.playAnimation()
                countDownTimer.start()
            }
        }

    }
}