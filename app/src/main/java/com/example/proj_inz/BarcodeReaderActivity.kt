package com.example.proj_inz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.proj_inz.databinding.BarcodeReaderBinding
import com.example.proj_inz.databinding.BarcodeReaderDetailsBinding
import com.example.proj_inz.databinding.BarcodeReaderErrorBinding

class BarcodeReaderActivity : AppCompatActivity() {


    //todo implement barcode reader
    //todo implement possible scenarios for barcode (product found or error)
    //todo implement putextras for cart
    private lateinit var bindingBarcode: BarcodeReaderBinding
    private lateinit var bindingBarcodeDetails: BarcodeReaderDetailsBinding
    private lateinit var bindingBarcodeError: BarcodeReaderErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingBarcode = BarcodeReaderBinding.inflate(layoutInflater)
        bindingBarcodeDetails = BarcodeReaderDetailsBinding.inflate(layoutInflater)
        bindingBarcodeError = BarcodeReaderErrorBinding.inflate(layoutInflater)

        setContentView(bindingBarcode.root)

        bindingBarcode.buttonBarcodeConfirm.setOnClickListener {
            setContentView(bindingBarcodeDetails.root)
        }
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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.helpButton -> {
                startActivity(Intent(this, HelpActivity::class.java))
                finish()
            }
            R.id.homeButton -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.barcodeButton -> { }
            R.id.recognizerButton -> {
                startActivity(Intent(this, TextRecognizerActivity::class.java))
                finish()
            }
            R.id.cartButton -> {
                startActivity(Intent(this, CartActivity::class.java))
                finish()
            }
            R.id.cartDetailsButton -> {
                startActivity(Intent(this, CartActivity::class.java).apply {
                    putExtra("MESSAGE","toCartDetails")
                })
                finish()
            }
            else -> { }
        }

        return super.onOptionsItemSelected(item)
    }
}