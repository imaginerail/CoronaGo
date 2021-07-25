package com.aneeq.coronago.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.aneeq.coronago.R

class SignInActivity : AppCompatActivity() {
    lateinit var etPass: EditText
    lateinit var etEmail: EditText
    lateinit var btnSignIn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        etPass = findViewById(R.id.etPass)
        etEmail = findViewById(R.id.etEmail)
        btnSignIn = findViewById(R.id.btnSignIn)

        btnSignIn.setOnClickListener {
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }
}
