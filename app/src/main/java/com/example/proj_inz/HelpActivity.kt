package com.example.proj_inz

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.proj_inz.databinding.HelpBinding

class HelpActivity : AppCompatActivity() {

    private lateinit var bindingHelp: HelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.supportActionBar?.hide()

        bindingHelp = HelpBinding.inflate(layoutInflater)
        setContentView(bindingHelp.root)

        val pref = getSharedPreferences("ApplicationPREF", Context.MODE_PRIVATE)

        bindingHelp.helpToMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            val ed: SharedPreferences.Editor = pref.edit()
            ed.putBoolean("help_activity_executed", true)
            ed.apply()
        }

        if (pref.getBoolean("help_activity_executed", false)) {
            val intent = Intent(this, MainActivity::class.java)
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
            R.id.helpButton -> { }
            R.id.homeButton -> { startActivity(Intent(this, MainActivity::class.java)) }
            R.id.barcodeButton -> { startActivity(Intent(this, BarcodeReaderActivity::class.java)) }
            R.id.recognizerButton -> { startActivity(Intent(this, TextRecognizerActivity::class.java)) }
            R.id.cartButton -> { startActivity(Intent(this, CartActivity::class.java)) }
            R.id.cartDetailsButton -> { startActivity(Intent(this, CartActivity::class.java).apply { putExtra("MESSAGE","toCartDetails") }) }
            else -> { }
        }

        return super.onOptionsItemSelected(item)
    }
}