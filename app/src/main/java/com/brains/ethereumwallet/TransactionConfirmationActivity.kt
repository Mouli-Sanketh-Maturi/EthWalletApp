package com.brains.ethereumwallet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity


class TransactionConfirmationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transactionHash = if (savedInstanceState == null) {
            val extras = intent.extras
            extras?.getString("transactionHash")
        } else {
            savedInstanceState.getSerializable("transactionHash") as String?
        }

        setContentView(R.layout.activity_transaction_confirmation)

        val transactionIdTextView = findViewById<TextView>(R.id.transaction_id_display)
        transactionIdTextView.text = transactionHash

        val etherscanButton = findViewById<Button>(R.id.button_visit_transaction)
        etherscanButton.setOnClickListener {
            // Open EtherScan in the browser
            openWebPage("https://sepolia.etherscan.io/tx/$transactionHash");
        }

    }

    private fun openWebPage(url: String) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }

}