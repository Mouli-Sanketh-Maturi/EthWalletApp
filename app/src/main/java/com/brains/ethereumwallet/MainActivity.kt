package com.brains.ethereumwallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sendButton = findViewById<Button>(R.id.button_send)
        val receiveButton = findViewById<Button>(R.id.button_receive)

        //Set the Ethereum price text view
        InfuraService().getCurrentEthereumPrice { price ->
            runOnUiThread {
                val ethereumPriceTextView = findViewById<TextView>(R.id.text_view_eth_price)
                ethereumPriceTextView.text = "Current ETH Price: $" + price
            }
        }

        //Set the Ethereum balance text view
        InfuraService().getBalance("0x7cf8f2065ac0530ce73386d7875aecf9439782c2") { balance ->
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
    }
}