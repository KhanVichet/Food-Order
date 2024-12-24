package com.example.foodapp.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.foodapp.R
import kotlin.jvm.java

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // Initialize startBtn using findViewById
        val startBtn: ConstraintLayout = findViewById(R.id.startBtn)

        // Set an OnClickListener using Kotlin's lambda
        startBtn.setOnClickListener {
            // Start MainActivity
            this.startActivity(Intent(this@IntroActivity,SignInActivity::class.java))
        }
    }
}