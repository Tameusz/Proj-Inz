package com.example.proj_inz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import com.example.proj_inz.databinding.*

class MainActivity : AppCompatActivity() {


    private lateinit var bindingMain: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)

        setContentView(bindingMain.root)


        bindingMain.buttonBarcodeReader.setOnClickListener {
            startActivity(Intent(this, BarcodeReaderActivity::class.java))
        }
        bindingMain.buttonTextRecognizer.setOnClickListener {
            startActivity(Intent(this, TextRecognizerActivity::class.java))
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
            R.id.homeButton -> { }
            R.id.barcodeButton -> {
                startActivity(Intent(this, BarcodeReaderActivity::class.java))
                finish()
            }
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