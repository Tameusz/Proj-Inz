package com.example.proj_inz

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.proj_inz.databinding.BarcodeReaderBinding
import com.example.proj_inz.databinding.BarcodeReaderDetailsBinding
import com.example.proj_inz.databinding.BarcodeReaderErrorBinding
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.toolbox.Volley


private const val CAMERA_REQUEST_CODE = 101

class BarcodeReaderActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    //todo implement barcode reader
    //todo implement possible scenarios for barcode (product found or error)
    //todo implement putextras for cart
    private lateinit var bindingBarcode: BarcodeReaderBinding
    private lateinit var bindingBarcodeDetails: BarcodeReaderDetailsBinding
    private lateinit var bindingBarcodeError: BarcodeReaderErrorBinding
    private lateinit var scannedCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingBarcode = BarcodeReaderBinding.inflate(layoutInflater)
        bindingBarcodeDetails = BarcodeReaderDetailsBinding.inflate(layoutInflater)
        bindingBarcodeError = BarcodeReaderErrorBinding.inflate(layoutInflater)

        setContentView(bindingBarcode.root)

        //bindingBarcode.buttonBarcodeConfirm.setOnClickListener {
        //    setContentView(bindingBarcodeDetails.root)
        //}
        bindingBarcodeDetails.buttonBarcodeDetailsConfirm.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
            finish()
        }
        bindingBarcodeDetails.buttonBarcodeDetailsToBarcode.setOnClickListener {
            setContentView(bindingBarcode.root)
        }
        bindingBarcodeError.errorBarcodeToBarcode.setOnClickListener {
            setContentView(bindingBarcode.root)
        }
        bindingBarcodeError.errorBarcodeToRecognizer.setOnClickListener {
            startActivity(Intent(this, TextRecognizerActivity::class.java))
            finish()
        }

        setupPermissions()
        codeScanner()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.helpButton -> {
                startActivity(Intent(this, HelpActivity::class.java))
            }
            R.id.homeButton -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.barcodeButton -> { }
            R.id.recognizerButton -> {
                startActivity(Intent(this, TextRecognizerActivity::class.java))
            }
            R.id.cartButton -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
            R.id.cartDetailsButton -> {
                startActivity(Intent(this, CartActivity::class.java).apply {
                    putExtra("MESSAGE","toCartDetails")
                })
            }
            else -> { }
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun codeScanner() {
        codeScanner = CodeScanner(this, bindingBarcode.scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false
            decodeCallback = DecodeCallback {
                runOnUiThread{
                    //Toast.makeText(this@BarcodeReaderActivity, "${it.text}", Toast.LENGTH_SHORT).show()
                    //code that is scanned by scanner
                    scannedCode = it.text

                    val apiResponse = "https://openfoodfacts.org/api/v0/product/$scannedCode.json"
                    val request = StringRequest(Request.Method.GET, apiResponse, { s ->
                        if(JSONObject(s).get("status").toString().toInt()==0)
                        {
                            setContentView(bindingBarcodeError.root)
                        }
                        else
                        {
                            //todo fix fiber issues (maybe own database or change api to fatsecret or just change it to sugar)
                            setContentView(bindingBarcodeDetails.root)
                            val energy100g = JSONObject(JSONObject(JSONObject(s).get("product").toString()).get("nutriments").toString()).get("energy-kcal_100g").toString().toDouble()
                            val proteins100g = JSONObject(JSONObject(JSONObject(s).get("product").toString()).get("nutriments").toString()).get("proteins_100g").toString().toDouble()
                            val fat100g = JSONObject(JSONObject(JSONObject(s).get("product").toString()).get("nutriments").toString()).get("fat_100g").toString().toDouble()
                            val carbohydrates100g = JSONObject(JSONObject(JSONObject(s).get("product").toString()).get("nutriments").toString()).get("carbohydrates_100g").toString().toDouble()
                            //val fiber100g = JSONObject(JSONObject(JSONObject(s).get("product").toString()).get("nutriments").toString()).get("fiber_100g").toString().toDouble()
                            val salt100g = JSONObject(JSONObject(JSONObject(s).get("product").toString()).get("nutriments").toString()).get("salt_100g").toString().toDouble()
                            val productName = JSONObject(JSONObject(s).get("product").toString()).get("product_name").toString()
                            bindingBarcodeDetails.productDetailsBarcodeReader.text = "Wybrałeś $productName \nTo są dawki makroskładników na 100 gram:\nEnergia: $energy100g\nBiałko: $proteins100g\nTłuszcze: $fat100g\n" +
                                    "Weglowodany: $carbohydrates100g\n" +
                                    //"Błonnik: $fiber100g" +
                                    "\nSól: $salt100g\n\n\nIle zamierzasz spożyć produktu w określonym czasie"

                        }
                         }
                    ) { bindingBarcodeDetails.productDetailsBarcodeReader.text = "Some error occurred!!" }
                    val rQueue = Volley.newRequestQueue(this@BarcodeReaderActivity)
                    rQueue.add(request)
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera initialization error: ${it.message}")
                }
            }
        }

        bindingBarcode.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Daj dostęp by korzystać", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}