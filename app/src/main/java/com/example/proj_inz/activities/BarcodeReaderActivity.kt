package com.example.proj_inz.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.proj_inz.R
import com.example.proj_inz.data.Product
import com.example.proj_inz.fragments.BMRUpdateFragment
import com.example.proj_inz.fragments.HelpFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception


private const val CAMERA_REQUEST_CODE = 101

class BarcodeReaderActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    private lateinit var bindingBarcode: BarcodeReaderBinding
    private lateinit var bindingBarcodeDetails: BarcodeReaderDetailsBinding
    private lateinit var bindingBarcodeError: BarcodeReaderErrorBinding
    lateinit var scannedCode: String
    var energy100g: Float = 0.0f
    var proteins100g: Float = 0.0f
    var fat100g: Float = 0.0f
    var carbohydrates100g: Float = 0.0f
    var fiber100g: Float = 0.0f
    var salt100g: Float = 0.0f
    var productName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingBarcode = BarcodeReaderBinding.inflate(layoutInflater)
        bindingBarcodeDetails = BarcodeReaderDetailsBinding.inflate(layoutInflater)
        bindingBarcodeError = BarcodeReaderErrorBinding.inflate(layoutInflater)

        setContentView(bindingBarcode.root)
        buttonsFunctionality()
        setupPermissions()
        codeScanner()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.helpButton -> { HelpFragment().show(supportFragmentManager,"helpDialog") }
            R.id.updateBMRButton -> { BMRUpdateFragment().show(supportFragmentManager,"updateDialog")}
            R.id.homeButton -> { startActivity(Intent(this, MainActivity::class.java)) }
            R.id.barcodeButton -> { }
            R.id.recognizerButton -> { startActivity(Intent(this, TextRecognizerActivity::class.java)) }
            R.id.cartButton -> { startActivity(Intent(this, CartActivity::class.java)) }
            R.id.cartDetailsButton -> { startActivity(Intent(this, CartActivity::class.java).apply { putExtra("MESSAGE","toCartDetails") }) }
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
                    scannedCode = it.text

                    val apiResponse = "https://openfoodfacts.org/api/v0/product/$scannedCode.json"
                    val request = StringRequest(Request.Method.GET, apiResponse, { s ->
                        if(JSONObject(s).get("status").toString().toInt()==0)
                        { setContentView(bindingBarcodeError.root)
                        }
                        else {
                            setContentView(bindingBarcodeDetails.root)
                            response(s)
                        }
                         }
                    ) { bindingBarcodeDetails.productDetailsBarcodeReader.text = "Some error occurred!!" }
                    val rQueue = Volley.newRequestQueue(this@BarcodeReaderActivity)
                    rQueue.add(request)
                }
            }
            errorCallback = ErrorCallback { runOnUiThread { Log.e("Main", "Camera initialization error: ${it.message}") } }
        }
        bindingBarcode.scannerView.setOnClickListener { codeScanner.startPreview() }
    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    private fun setupPermissions() { if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { makeRequest() } }
    private fun makeRequest() { ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE) }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {CAMERA_REQUEST_CODE -> { if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) { Toast.makeText(this, "Daj dostęp by korzystać", Toast.LENGTH_SHORT).show() } } }
    }
    @SuppressLint("SetTextI18n")
    private fun response(stringJson: String) {
        try {
            if(isThereNutrimentsData(stringJson,"energy-kcal_100g")) { energy100g = fetchNutrimentsData(stringJson, "energy-kcal_100g") } else { energy100g = 0f }
            if(isThereNutrimentsData(stringJson, "proteins_100g")) { proteins100g = fetchNutrimentsData(stringJson, "proteins_100g") } else { proteins100g = 0f }
            if(isThereNutrimentsData(stringJson, "fat_100g")) { fat100g = fetchNutrimentsData(stringJson, "fat_100g") } else { fat100g = 0f }
            if(isThereNutrimentsData(stringJson,"carbohydrates_100g")) { carbohydrates100g = fetchNutrimentsData(stringJson, "carbohydrates_100g") } else { carbohydrates100g = 0f }
            if(isThereNutrimentsData(stringJson, "fiber_100g")) { fiber100g = fetchNutrimentsData(stringJson, "fiber_100g") } else { fiber100g = 0f }
            if(isThereNutrimentsData(stringJson, "salt_100g")) { salt100g = fetchNutrimentsData(stringJson, "salt_100g") } else { salt100g = 0f }
            if(isThereValidProductName(stringJson, "product_name")) { productName = fetchProductName(stringJson,"product_name") } else { productName = "product" }
            bindingBarcodeDetails.productDetailsBarcodeReader.text = updateTextView(energy100g, proteins100g, fat100g, carbohydrates100g, fiber100g, salt100g, productName)
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }
    private fun buttonsFunctionality() {
        bindingBarcodeDetails.buttonBarcodeDetailsConfirm.setOnClickListener {
            if(bindingBarcodeDetails.editTextBarcodeDetails.text.toString() == "") {
                Toast.makeText(this, "Podaj liczbę gramów", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, CartActivity::class.java))
                updateSharedPreferences(bindingBarcodeDetails.editTextBarcodeDetails.text.toString().toFloat())
            }
        }
        bindingBarcodeDetails.buttonBarcodeDetailsToBarcode.setOnClickListener { setContentView(bindingBarcode.root) }
        bindingBarcodeError.errorBarcodeToBarcode.setOnClickListener { setContentView(bindingBarcode.root) }
        bindingBarcodeError.errorBarcodeToRecognizer.setOnClickListener { startActivity(Intent(this, TextRecognizerActivity::class.java)) }
    }
    fun isThereNutrimentsData(s: String, checkedNutriment: String): Boolean { return JSONObject(JSONObject(JSONObject(s).get("product").toString()).get("nutriments").toString()).has(checkedNutriment) }
    private fun fetchNutrimentsData(s: String, nutrimentPer100g: String): Float { return JSONObject(JSONObject(JSONObject(s).get("product").toString()).get("nutriments").toString()).get(nutrimentPer100g).toString().toFloat() }
    private fun isThereValidProductName(s: String, productName: String): Boolean { return JSONObject(JSONObject(s).get("product").toString()).has(productName) }
    private fun fetchProductName(s: String, productName: String): String { return JSONObject(JSONObject(s).get("product").toString()).get(productName).toString() }
    private fun updateTextView(energy: Float, proteins:Float, fat: Float, carbohydrates: Float, fiber: Float, salt: Float, name: String): String {
        return "Wybrałeś $name\n" +
                "To są dawki makroskładników na 100 gram:\n" +
                "Energia: $energy\n" +
                "Białko: $proteins\n" +
                "Tłuszcze: $fat\n" +
                "Weglowodany: $carbohydrates\n" +
                "Błonnik: $fiber\n" +
                "Sól: $salt\n\n\n" +
                "Ile zamierzasz spożyć produktu w określonym czasie"
    }
    private fun updateSharedPreferences(howMuch: Float) {
        val gson = Gson()
        val pref = getSharedPreferences("ApplicationPREF", Context.MODE_PRIVATE)
        var listOfProductsPREF = pref.getString("listOfProductsPREF", "")
        val itemType = object : TypeToken<List<Product>>() {}.type
        val newProduct = Product(productName,
            (howMuch*energy100g)/100,
            (howMuch*proteins100g)/100,
            (howMuch*fat100g)/100,
            (howMuch*carbohydrates100g)/100,
            (howMuch*fiber100g)/100,
            (howMuch*salt100g)/100,
            howMuch)
        var listOfProducts = mutableListOf<Product>()
        if(gson.fromJson<List<Product>>(listOfProductsPREF, itemType) != null) {
            listOfProducts = gson.fromJson<List<Product>>(listOfProductsPREF, itemType).toMutableList()
        }
        listOfProducts += newProduct
        val editor: SharedPreferences.Editor = pref.edit()
        listOfProductsPREF = gson.toJson(listOfProducts)
        editor.putString("listOfProductsPREF", listOfProductsPREF)
        editor.apply()
    }

}