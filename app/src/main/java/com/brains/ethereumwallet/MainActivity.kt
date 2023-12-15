package com.brains.ethereumwallet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // If not onboarded, navigate to OnboardActivity
        val sharedPreferences = getSharedPreferences("com.brains.ethereumwallet", MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("onboarded", false)) {
            val intent = Intent(this, OnboardActivity::class.java)
            startActivity(intent)
        }

        val sendButton = findViewById<Button>(R.id.button_send)
        val receiveButton = findViewById<Button>(R.id.button_receive)
        val transactionHistoryButton = findViewById<Button>(R.id.button_transaction_history)

        //Set the Ethereum price text view
        InfuraService().getCurrentEthereumPrice { price ->
            runOnUiThread {
                val ethereumPriceTextView = findViewById<TextView>(R.id.text_view_eth_price)
                ethereumPriceTextView.text = "Current ETH Price: $" + price
            }
        }

        // Get private key from shared preferences
        val publicKey = sharedPreferences.getString("publicKey", "0x7CF8F2065aC0530ce73386d7875aeCf9439782c2")
        // Get the address from the private key
        //Set the Ethereum balance text view
        InfuraService().getBalance(publicKey!!) { balance ->
            runOnUiThread {
                val ethereumBalanceTextView = findViewById<TextView>(R.id.text_view_eth_balance)
                val balanceInFloat = balance.toDouble() / 100
                ethereumBalanceTextView.text = "Current ETH Balance: " + balanceInFloat + " ETH"
            }
        }

        sendButton.setOnClickListener {
            // Navigate to SendActivity
            val intent = Intent(this, SendActivity::class.java)
            startActivity(intent)
        }
        receiveButton.setOnClickListener {
            // Handle receive button click
            val intent = Intent(this, ReceiveActivity::class.java)
            startActivity(intent)
        }
        transactionHistoryButton.setOnClickListener {
            // Handle transaction history button click
            openWebPage("https://sepolia.etherscan.io/address/$publicKey");
        }
    }

    private fun openWebPage(url: String) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }
}