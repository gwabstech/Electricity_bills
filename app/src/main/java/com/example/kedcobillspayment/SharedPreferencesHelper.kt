package com.example.kedcobillspayment

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("transactions_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveTransaction(transaction: Transaction) {
        val transactions = getTransactions().toMutableList()
        transactions.add(transaction)
        val json = gson.toJson(transactions)
        sharedPreferences.edit().putString("transactions", json).apply()
    }

    fun getTransactions(): List<Transaction> {
        val json = sharedPreferences.getString("transactions", null)
        if (json.isNullOrEmpty()) {
            return emptyList()
        }
        val type = object : TypeToken<List<Transaction>>() {}.type
        return gson.fromJson(json, type)
    }
}
