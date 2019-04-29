package com.example.fblab.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.fblab.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var fbAuth: FirebaseAuth? = null


    companion object {

        fun redirect(context: Context) {
            val intent = Intent(context, RegistrationActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_registration)

        initVars()
        initUI()
    }

    private fun initVars() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        fbAuth = FirebaseAuth.getInstance()
    }

    private fun initUI() {
        backButton.setOnClickListener { redirectToLoginPage() }
        signUpButton.setOnClickListener { signUp() }
    }

    private fun redirectToLoginPage() {
        LoginActivity.redirect(this)
    }

    private fun signUp() {
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        val name = nameInput.text.toString()
        val age = ageInput.text.toString()


        fbAuth!!
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = fbAuth!!.currentUser!!.uid

                    val user = mDatabaseReference!!.child(userId)
                    user.child("name").setValue(name)
                    user.child("age").setValue(age)

                    redirectToLoginPage()
                } else {
                    Toast.makeText(this, getString(R.string.registration_error), Toast.LENGTH_SHORT).show()
                }

            }
    }


}
