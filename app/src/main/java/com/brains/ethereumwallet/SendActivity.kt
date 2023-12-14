package com.brains.ethereumwallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import java.math.BigInteger

class SendActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        val sendButtonConfirmation = findViewById<Button>(R.id.button_send_confirm)
        sendButtonConfirmation.setOnClickListener {
            // Handle send button click
            val address = findViewById<EditText>(R.id.address_input).text.toString()
            val amount = findViewById<EditText>(R.id.amount_input).text.toString()
            handleSendButtonClick(address, amount)
        }
    }

    // Handle the send button click by taking in the address and amount
    // and sending the transaction to the blockchain
    fun handleSendButtonClick(address: String?, amount: String?) {
        // Validate the address and amount
        // If valid, send the transaction
        // If invalid, show an error message
        address?.let {
            amount?.let {
                // Send the transaction
                // Show a success message
                InfuraService().sendTransaction(
                    "0x1234567890",
                    "776cc4cd10393872e5c3b9828e0a4f461aa84676857eac9b96a585772f8fea55",
                    address,
                    amount,
                    BigInteger("20000000000"),
                    BigInteger("21000")
                ) { transactionHash ->
                    // Navigate to the TransactionConfirmationActivity
                    val intent = Intent(this, TransactionConfirmationActivity::class.java)
                    intent.putExtra("transactionHash", transactionHash)
                    startActivity(intent)
                }
            }
        }
    }

}