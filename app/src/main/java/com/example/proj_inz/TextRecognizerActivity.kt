package com.example.proj_inz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.proj_inz.databinding.TextRecognizerBinding
import com.example.proj_inz.databinding.TextRecognizerDetailsBinding
import com.example.proj_inz.databinding.TextRecognizerErrorBinding

class TextRecognizerActivity : AppCompatActivity() {

    private lateinit var bindingRecognizer: TextRecognizerBinding
    private lateinit var bindingRecognizerDetails: TextRecognizerDetailsBinding
    private lateinit var bindingRecognizerError: TextRecognizerErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingRecognizer = TextRecognizerBinding.inflate(layoutInflater)
        bindingRecognizerDetails = TextRecognizerDetailsBinding.inflate(layoutInflater)
        bindingRecognizerError = TextRecognizerErrorBinding.inflate(layoutInflater)

        setContentView(bindingRecognizer.root)

        bindingRecognizer.buttonRecognizerConfirm.setOnClickListener {
            setContentView(bindingRecognizerDetails.root)
        }
        bindingRecognizerDetails.buttonRecognizerDetailsConfirm.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
            finish()
        }
        bindingRecognizerDetails.buttonRecognizerDetailsToRecognizer.setOnClickListener {
            setContentView(bindingRecognizer.root)
        }
        bindingRecognizerError.errorBarcodeToBarcode.setOnClickListener {
            startActivity(Intent(this, BarcodeReaderActivity::class.java))
            finish()
        }
        bindingRecognizerError.errorBarcodeToRecognizer.setOnClickListener {
            setContentView(bindingRecognizer.root)
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
            R.id.barcodeButton -> {
                startActivity(Intent(this, BarcodeReaderActivity::class.java))
                finish()
            }
            R.id.recognizerButton -> { }
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