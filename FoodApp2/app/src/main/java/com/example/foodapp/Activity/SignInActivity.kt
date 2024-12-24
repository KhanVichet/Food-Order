package com.example.foodapp.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignInActivity : AppCompatActivity() {
    private val binding : ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth

    override fun onStart(){
        super.onStart()
        val currentUser : FirebaseUser? = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        binding.signUpText.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }

        binding.btnSignIn.setOnClickListener{
            Login()
        }
    }

    private fun Login(){
        val email = binding.emailField.text.toString()
        val password  = binding.passwordField.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please Fill All The Detail",Toast.LENGTH_SHORT).show()
        }
        val emailPattern = "[a-zA-Z0-9._%+-]+@gmail\\.com"
        if (!email.matches(Regex(emailPattern))) {
            Toast.makeText(this, "Please enter a valid Gmail address", Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {

                    Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show()
                }
            }
    }
}