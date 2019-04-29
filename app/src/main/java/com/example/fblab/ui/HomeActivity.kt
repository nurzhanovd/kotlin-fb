package com.example.fblab.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.fblab.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val ARG_USER = "ARG_USER"

        fun redirect(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var user: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        user = intent.getStringExtra(ARG_USER)

        initUI()
    }

    private fun initUI() {
        val (userEmail, _, userName, userAge) = user.split(':')

        email_view.text = userEmail
        name_view.text = userName
        age_view.text = userAge

        logout_btn.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        LoginActivity.redirect(this)
    }
}
