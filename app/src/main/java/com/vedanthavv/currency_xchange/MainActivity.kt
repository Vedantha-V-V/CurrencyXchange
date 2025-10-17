package com.vedanthavv.currency_xchange

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Spinner
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            var source = ""
            var dest = ""
            //val amount = 0.0
            var hasSubmit = false
            val image: ImageView
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            // Adapter Initialization
            val currencies = listOf("USD", "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "FOK", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SLL", "SOS", "SRD", "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TVD", "TWD", "TZS", "UAH", "UGX", "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XCD", "XCG", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWL")
            val amount = findViewById<EditText>(R.id.edAmount)
            val sourceSpin = findViewById<Spinner>(R.id.spSource)
            val destSpin = findViewById<Spinner>(R.id.spDest)
            val convertBtn = findViewById<Button>(R.id.btConvert)
            val result = findViewById<TextView>(R.id.tvResult)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,currencies)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sourceSpin.setAdapter(adapter)
            destSpin.setAdapter(adapter)
            //Fetching selected dropdown value
            sourceSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent:AdapterView<*>,view:View?,position:Int,id:Long){
                    source = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    hasSubmit = false
                }
            }
            destSpin.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent:AdapterView<*>,view:View?, position:Int,id:Long){
                    dest = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    hasSubmit = false
                }
            }

            //Button Click and Conversion
            convertBtn.setOnClickListener {
                val inputValue = amount.text.toString()
                val number = inputValue.toDoubleOrNull()
                if(number !=null){
                    result.text = "Currency conversion underway..."
                    fetchExchangeRates(number,result,source,dest)
                }
            }

            insets
        }
    }

    private fun fetchExchangeRates(amount:Double,result:TextView,source:String,dest:String) {
        if(source.isBlank() or dest.isBlank()){
            return
        }
        val request = Volley.newRequestQueue(this)

        val url = "https://v6.exchangerate-api.com/v6/4bf8d9a7a0303575439ff6e9/latest/${dest}"

        val conversionResponse = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
                val map = mutableMapOf<String, Any>()
                response.keys().forEach { key ->
                    map[key] = response.get(key)
                }
                val conversionRates = response.getJSONObject("conversion_rates")
                val rate = conversionRates.getDouble(source)
                val converted = amount*rate
                result.text = "${amount} ${dest} is ${converted} ${source}"
            },
            { error ->
                error.printStackTrace()
            })
        request.add(conversionResponse)
    }
}
