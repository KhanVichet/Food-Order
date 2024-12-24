package com.example.foodapp.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.databinding.ActivitySingupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySingupBinding by lazy {
        ActivitySingupBinding.inflate(layoutInflater)
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnSignUp.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val progressDialog = createProgressDialog("Sign Up", "Please wait...")

        val name = binding.nameField.text.toString()
        val email = binding.emailField.text.toString()
        val password = binding.passwordField.text.toString()


        if (!validateInputs(email, password, progressDialog)) return

        createUserWithEmailAndPassword(email, password, name, progressDialog)

    }

    private fun validateInputs(email: String, password: String,  progressDialog: ProgressDialog): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the details", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
            return false
        }



        if (password.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
            return false
        }

        val (isStrong, message) = isPasswordStrong(password)
        if (!isStrong) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
            return false
        }

        val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        if (!email.matches(Regex(emailPattern))) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
            return false
        }

        return true
    }

    private fun isEmailAvailable(email: String, onResult: (Boolean) -> Unit) {
        db.collection("users").whereEqualTo("email", email).get()
            .addOnSuccessListener { onResult(it.isEmpty) }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error checking email availability", exception)
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                onResult(false)
            }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String, userName: String, progressDialog: ProgressDialog) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    saveUserInfo(userName, email)
                } else {
                    val errorMessage = when ((task.exception as? FirebaseAuthException)?.errorCode) {
                        "ERROR_WEAK_PASSWORD" -> "The password is too weak."
                        "ERROR_EMAIL_ALREADY_IN_USE" -> "This email address is already in use."
                        "ERROR_INVALID_EMAIL" -> "The email address is invalid."
                        else -> "Registration failed: ${task.exception?.message}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserInfo(userName: String, email: String) {
        val currentUserID = mAuth.currentUser!!.uid
        val userMap = hashMapOf(
            "name" to userName,
            "uid" to currentUserID,
            "email" to email
        )

        db.collection("users").document(currentUserID).set(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
                    navigateToSignIn()
                } else {
                    Toast.makeText(this, "Error saving user info: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error saving user info", exception)
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun createProgressDialog(title: String, message: String): ProgressDialog {
        return ProgressDialog(this).apply {
            setTitle(title)
            setMessage(message)
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    private fun isPasswordStrong(password: String): Pair<Boolean, String> {
        val lengthRegex = ".{8,}".toRegex()
        val uppercaseRegex = ".*[A-Z].*".toRegex()
        val lowercaseRegex = ".*[a-z].*".toRegex()
        val digitRegex = ".*[0-9].*".toRegex()
        val specialCharRegex = ".*[!@#\$%^&*(),.?\":{}|<>].*".toRegex()

        return when {
            !password.matches(lengthRegex) -> Pair(false, "Password must be at least 8 characters long.")
            !password.matches(uppercaseRegex) -> Pair(false, "Password must contain at least one uppercase letter.")
            !password.matches(lowercaseRegex) -> Pair(false, "Password must contain at least one lowercase letter.")
            !password.matches(digitRegex) -> Pair(false, "Password must contain at least one digit.")
            !password.matches(specialCharRegex) -> Pair(false, "Password must contain at least one special character.")
            else -> Pair(true, "Password is strong.")
        }
    }
}
