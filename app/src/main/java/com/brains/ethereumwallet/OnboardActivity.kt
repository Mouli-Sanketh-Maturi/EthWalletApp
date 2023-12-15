package com.brains.ethereumwallet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.crypto.Wallet
import java.security.Security
import java.util.UUID


class OnboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBouncyCastle()
        setContentView(R.layout.activity_onboard)

        val importButton = findViewById<Button>(R.id.button_input_private_key)
        val createButton = findViewById<Button>(R.id.button_create_new_wallet)

        importButton.setOnClickListener {
            // Get address from Edit Text
            val privateKey = findViewById<EditText>(R.id.private_key_input).text.toString()
            // Validate the address to make sure it is a valid private key
            if (privateKey.length == 64) {
                val credentials = Credentials.create(privateKey)
                // Get the public key from the private key
                val publicKey = credentials.address
                // Save the address to shared preferences
                val sharedPreferences = getSharedPreferences("com.brains.ethereumwallet", MODE_PRIVATE)
                sharedPreferences.edit().putString("privateKey", privateKey).apply()
                sharedPreferences.edit().putString("publicKey", publicKey).apply()
                sharedPreferences.edit().putBoolean("onboarded", true).apply()
                Toast.makeText(applicationContext, "Wallet imported successfully", Toast.LENGTH_SHORT).show()
                // Navigate to MainActivity
                navigateToMainActivity()
            } else {
                Toast.makeText(applicationContext, "Enter valid private key", Toast.LENGTH_SHORT).show()
            }
        }

        createButton.setOnClickListener {
            // Create a new wallet
            val keyPair = Keys.createEcKeyPair()
            val privateKey = keyPair.privateKey.toString(16)
            val credentials = Credentials.create(privateKey)

            // Save the address to shared preferences
            val sharedPreferences = getSharedPreferences("com.brains.ethereumwallet", MODE_PRIVATE)
            sharedPreferences.edit().putString("privateKey", privateKey).apply()
            sharedPreferences.edit().putString("publicKey", credentials.address).apply()
            sharedPreferences.edit().putBoolean("onboarded", true).apply()
            Toast.makeText(applicationContext, "Wallet created successfully", Toast.LENGTH_SHORT).show()
            // Navigate to MainActivity
            navigateToMainActivity()
        }
    }

    // Don't allow the user to go back to the MainActivity
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Do nothing
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }

}