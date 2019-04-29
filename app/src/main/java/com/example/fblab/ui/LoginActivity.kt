package com.example.fblab.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.fblab.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var fbAuth = FirebaseAuth.getInstance()

    companion object {
        fun redirect(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUI()
    }

    private fun initUI() {
        signInButton.setOnClickListener {
            signIn()
        }
        intentSignUpButton.setOnClickListener { redirectToSignUpPage() }
    }

    private fun redirectToSignUpPage() {
        RegistrationActivity.redirect(this)
    }

    private fun signIn() {
        val email = email.text.toString()
        val password = password.text.toString()
        fbAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    HomeActivity.redirect(this)
                } else {
                    Toast.makeText(this, "Error while trying to login", Toast.LENGTH_SHORT).show()
                }
            })
    }


}