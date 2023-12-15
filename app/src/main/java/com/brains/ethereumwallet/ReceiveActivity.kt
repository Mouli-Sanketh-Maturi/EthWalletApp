package com.brains.ethereumwallet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity


class ReceiveActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive)

        val receiveAddressTextView = findViewById<TextView>(R.id.address_display)
        //Get the address from the shared preferences
        val sharedPreferences = getSharedPreferences("com.brains.ethereumwallet", MODE_PRIVATE)
        val publicKey = sharedPreferences.getString("publicKey", "0x7CF8F2065aC0530ce73386d7875aeCf9439782c2")
        receiveAddressTextView.text = publicKey

        val copyButton = findViewById<Button>(R.id.button_copy)
        copyButton.setOnClickListener {
            // Copy the address to the clipboard
            copyAddressToClipboard(publicKey!!)
        }

    }

    private fun copyAddressToClipboard(address: String) {
        // Get the Clipboard Manager
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // Create a ClipData object with the text you want to copy
        val clip = ClipData.newPlainText("address", address)

        // Set the ClipData to the Clipboard Manager
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Public address copied to clipboard", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error copying address to clipboard", Toast.LENGTH_SHORT).show()
        }
    }


}