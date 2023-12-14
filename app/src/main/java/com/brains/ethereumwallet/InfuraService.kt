package com.brains.ethereumwallet

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigInteger

class InfuraService {
    private val client = OkHttpClient()
    private val infuraUrl = "https://sepolia.infura.io/v3/8e4d5532beaa4585b77e9a403459a7a4"
    private val web3j = Web3j.build(HttpService(infuraUrl));

    fun getCurrentEthereumPrice(callback: (String) -> Unit) {
        val request = Request.Builder()
            .url("https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=USD")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    callback("Error: ${response.message}")
                    return
                }

                val responseData = response.body?.string()
                // Parse the response data to extract Ethereum price
                // Example: callback("Ethereum Price: $price")
                /* Parse JSON like
                {
                    "USD": 2170.89
                }
                 */
                val price = responseData?.substringAfter(":")?.substringBefore("}")?.toDouble()
                callback("$price")

            }
        })
    }

    fun sendTransaction(fromAddress: String, privateKey: String, toAddress: String, amount: String, gasPrice: BigInteger, gasLimit: BigInteger, callback: (String) -> Unit) {
        val credentials = Credentials.create(privateKey)
        val value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger()

        val rawTransaction = RawTransaction.createEtherTransaction(
            getNonce(credentials.address), gasPrice, gasLimit, toAddress, value
        )

        val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
        val hexValue = Numeric.toHexString(signedMessage)

        // Send the hexValue to Infura to broadcast the transaction

        val transactionData = web3j.ethSendRawTransaction(hexValue).sendAsync().get()

        if (transactionData.hasError()) {
            println("Error: ${transactionData.error.message}")
            return
        }
        val transaction = transactionData.transactionHash

        // Handle the response

        callback("$transaction")

    }

    fun getNonce(address: String): BigInteger {
        // Construct a request to Infura's API to get the nonce of the specified address
        // Handle the response to extract and return the nonce

        return web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING ).sendAsync().get().transactionCount

    }

    fun getBalance(address: String, callback: (BigInteger) -> Unit) {
        // Construct a request to Infura's API to get the balance of the specified address
        // Handle the response to extract and return the balance

        val balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get().balance

        callback((balance/ BigInteger("10000000000000000")))

    }
}