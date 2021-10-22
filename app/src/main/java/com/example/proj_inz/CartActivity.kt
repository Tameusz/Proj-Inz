package com.example.proj_inz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.proj_inz.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding :ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.cartToBarcode.setOnClickListener {
            val intent = Intent(this, BarcodeReaderActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.cartToRecognizer.setOnClickListener {
            val intent = Intent(this, TextRecognizerActivity::class.java)
            startActivity(intent)
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
                //val intent = Intent(this, MainActivity::class.java)
                //startActivity(intent)
                //finish()
            }
            R.id.cartButton -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.cartDetailsButton -> {
                val intent = Intent(this, CartDetailsActivity::class.java)
                startActivity(intent)
                finish()
            }
            else -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }
}