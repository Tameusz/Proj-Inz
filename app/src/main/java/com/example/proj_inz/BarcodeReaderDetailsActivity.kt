package com.example.proj_inz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.proj_inz.databinding.ActivityBarcodeReaderDetailsBinding

class BarcodeReaderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBarcodeReaderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBarcodeReaderDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonBarcodeDetailsConfirm.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            finish()
        }
        //ktoś może zrezygnować
        binding.buttonBarcodeDetailsToBarcode.setOnClickListener {
            val intent = Intent(this, BarcodeReaderActivity::class.java)
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