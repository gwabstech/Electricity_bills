package com.example.kedcobillspayment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaymentFragment : Fragment() {

    private val transactions = mutableListOf<Transaction>()
    private lateinit var adapter: TransactionAdapter
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        val amountEditText: EditText = view.findViewById(R.id.et_amount)
        val meterNumberEditText: EditText = view.findViewById(R.id.et_meter_number)
        val payButton: Button = view.findViewById(R.id.btn_pay)
        val transactionsRecyclerView: RecyclerView = view.findViewById(R.id.rv_transactions)

        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())

        adapter = TransactionAdapter(transactions)
        transactionsRecyclerView.adapter = adapter
        transactionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadTransactions()

        payButton.setOnClickListener {
            val amount = amountEditText.text.toString()
            val meterNumber = meterNumberEditText.text.toString()

            if (amount.isNotEmpty() && meterNumber.isNotEmpty()) {
                startPaymentActivity(amount, meterNumber)
            }
        }

        return view
    }

    private fun startPaymentActivity(amount: String, meterNumber: String) {
        val intent = Intent(activity, PaymentActivity::class.java).apply {
            putExtra("amount", amount)
            putExtra("meterNumber", meterNumber)
        }
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == AppCompatActivity.RESULT_OK) {
            loadTransactions()
        }
    }

    private fun loadTransactions() {
        transactions.clear()
        transactions.addAll(sharedPreferencesHelper.getTransactions())
        adapter.notifyDataSetChanged()
    }
}

