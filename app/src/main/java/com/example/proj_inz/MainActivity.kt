package com.example.proj_inz

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import com.example.proj_inz.databinding.*

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)

        val pref = getSharedPreferences("ApplicationPREF", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()

        setContentView(bindingMain.root)
        buttonsFunctionality(editor)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.helpButton -> {
                val pref = getSharedPreferences("ApplicationPREF", Context.MODE_PRIVATE)
                val ed: SharedPreferences.Editor = pref.edit()
                ed.putBoolean("help_activity_executed", false)
                ed.apply()
                startActivity(Intent(this, HelpActivity::class.java))
            }
            R.id.homeButton -> { }
            R.id.barcodeButton -> { startActivity(Intent(this, BarcodeReaderActivity::class.java)) }
            R.id.recognizerButton -> { startActivity(Intent(this, TextRecognizerActivity::class.java)) }
            R.id.cartButton -> { startActivity(Intent(this, CartActivity::class.java)) }
            R.id.cartDetailsButton -> { startActivity(Intent(this, CartActivity::class.java).apply { putExtra("MESSAGE","toCartDetails") }) }
            else -> { }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun buttonsFunctionality(ed: SharedPreferences.Editor) {
        bindingMain.menuConfirm.setOnClickListener {
            if(bindingMain.etFor.text.toString() == "") {
                Toast.makeText(this, "Podaj liczbę dni", Toast.LENGTH_SHORT).show()
            } else {
                ed.putFloat("shopping_for", bindingMain.etFor.text.toString().toFloat()) }
                ed.apply()
            }
        bindingMain.buttonBarcodeReader.setOnClickListener { startActivity(Intent(this, BarcodeReaderActivity::class.java)) }
        bindingMain.buttonTextRecognizer.setOnClickListener { startActivity(Intent(this, TextRecognizerActivity::class.java)) }
    }
}