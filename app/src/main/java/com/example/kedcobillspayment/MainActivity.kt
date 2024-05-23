package com.example.kedcobillspayment


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {

    private val paymentRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = "Kedco Payment"


        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, PaymentFragment())
            }
        }
    }


    override fun onResume() {
        super.onResume()
        supportFragmentManager.commit {
            replace(R.id.fragment_container, PaymentFragment())
        }
    }
}

