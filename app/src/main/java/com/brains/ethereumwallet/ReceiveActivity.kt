package com.brains.ethereumwallet

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class ReceiveActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive)

        val receiveAddressTextView = findViewById<TextView>(R.id.address_display)
        receiveAddressTextView.text = "0x1234567890"
    }

}