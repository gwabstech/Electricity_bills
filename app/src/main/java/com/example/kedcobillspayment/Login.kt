package com.example.kedcobillspayment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {




    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var noAccount:TextView



    private fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {


        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception ?: Exception("Unknown error"))
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

       val progressDialog = ProgressDialog(this)

        progressDialog.setMessage("Login in...")
        progressDialog.setCancelable(false)

        usernameEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        noAccount = findViewById(R.id.noAccountTextView)


        noAccount.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()


            // Validate the username and password
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                progressDialog.show()
                loginWithEmailAndPassword(
                    email = email,
                    password = password,
                    onFailure = {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                    },
                    onSuccess = {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        // Redirect to the main activity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                )

            }


        }
    }
}

