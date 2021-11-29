package com.example.proj_inz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.proj_inz.databinding.HelpBinding

class HelpActivity : AppCompatActivity() {

    private lateinit var bindingHelp: HelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingHelp = HelpBinding.inflate(layoutInflater)

        setContentView(bindingHelp.root)

        bindingHelp.helpToMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.helpButton -> { }
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