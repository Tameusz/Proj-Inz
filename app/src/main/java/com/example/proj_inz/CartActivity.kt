package com.example.proj_inz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.proj_inz.databinding.CartBinding
import com.example.proj_inz.databinding.CartDetailsBinding

class CartActivity : AppCompatActivity() {

    private lateinit var bindingCart: CartBinding
    private lateinit var bindingCartDetails: CartDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingCart = CartBinding.inflate(layoutInflater)
        bindingCartDetails = CartDetailsBinding.inflate(layoutInflater)
        val extras = intent.extras
        if(extras?.getString("MESSAGE").equals("toCartDetails")) {
            setContentView(bindingCartDetails.root)
        } else {
            setContentView(bindingCart.root)
        }

        bindingCart.cartToBarcode.setOnClickListener {
            startActivity(Intent(this, BarcodeReaderActivity::class.java))
            finish()
        }
        bindingCart.cartToRecognizer.setOnClickListener {
            startActivity(Intent(this, TextRecognizerActivity::class.java))
            finish()
        }
        bindingCart.cartToCartDetails.setOnClickListener {
            setContentView(bindingCartDetails.root)
        }

        bindingCartDetails.cartDetailsToBarcode.setOnClickListener {
            startActivity(Intent(this, BarcodeReaderActivity::class.java))
            finish()
        }
        bindingCartDetails.cartDetailsToRecognizer.setOnClickListener {
            startActivity(Intent(this, TextRecognizerActivity::class.java))
            finish()
        }
        bindingCartDetails.cartDetailsToCart.setOnClickListener {
            setContentView(bindingCart.root)
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
            R.id.recognizerButton -> {
                startActivity(Intent(this, TextRecognizerActivity::class.java))
                finish()
            }
            R.id.cartButton -> {
                setContentView(bindingCart.root)
            }
            R.id.cartDetailsButton -> {
                setContentView(bindingCartDetails.root)
            }
            else -> { }
        }

        return super.onOptionsItemSelected(item)
    }
}