package com.example.kedcobillspayment
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {

    private val publicKey = "FLWPUBK_TEST-c8ad2e213991832b37e6cf58a0dd5110-X"
    private val encryptionKey = "FLWSECK_TEST2e9e5abd0712"
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        val amount = intent.getStringExtra("amount")?.toDouble() ?: 0.0
        val meterNumber = intent.getStringExtra("meterNumber") ?: ""

        if (amount > 0 && meterNumber.isNotEmpty()) {
            initiatePayment(amount, meterNumber)
        }
    }

    private fun initiatePayment(amount: Double, meterNumber: String) {
        RaveUiManager(this)
            .setAmount(amount)
            .setCurrency("NGN")
            .setEmail("user@example.com")  // Replace with actual user email // Replace with actual last name
            .setNarration("Payment for meter number: $meterNumber")
            .setPublicKey(publicKey)
            .setEncryptionKey(encryptionKey)
            .setTxRef(UUID.randomUUID().toString())
            .acceptAccountPayments(true)
            .acceptCardPayments(true)
            .acceptBankTransferPayments(true)
            .acceptUssdPayments(true)
            .acceptBarterPayments(true)
            .onStagingEnv(true)  // Change to false for production
            .allowSaveCardFeature(true)
            // Optional: apply custom theme
            .initialize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message = data.getStringExtra("response")
            when (resultCode) {
                RavePayActivity.RESULT_SUCCESS -> {
                    Toast.makeText(this, "Payment Successful: $message", Toast.LENGTH_SHORT).show()
                    val amount = intent.getStringExtra("amount") ?: "0.0"
                    val date = getCurrentDate()
                    val token = generateToken()
                    val transaction = Transaction(amount, token, date)
                    // Store the transaction
                    sharedPreferencesHelper.saveTransaction(transaction)
                    // Finish the activity and return to the previous screen
                    finish()
                }
                RavePayActivity.RESULT_ERROR -> {
                    Toast.makeText(this, "Payment Error: $message", Toast.LENGTH_SHORT).show()
                }
                RavePayActivity.RESULT_CANCELLED -> {
                    Toast.makeText(this, "Payment Cancelled: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun generateToken(): String {
        val random = Random()
        return (1..4).joinToString(" ") { (random.nextInt(9000) + 1000).toString() }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}


