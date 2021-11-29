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
    private var standardWeight: Int = 130
    private var standardHeight: Int = 170
    private var standardAge: Int = 22
    private var standardSex: Int = 1

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

        progressBarAdjustment(calculateCalories(standardWeight,standardHeight,standardAge,standardSex))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.helpButton -> {
                startActivity(Intent(this, HelpActivity::class.java))
            }
            R.id.homeButton -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.barcodeButton -> {
                startActivity(Intent(this, BarcodeReaderActivity::class.java))
            }
            R.id.recognizerButton -> {
                startActivity(Intent(this, TextRecognizerActivity::class.java))
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

    private fun progressBarAdjustment(calories: Int) {
        //calories
        bindingCartDetails.progressBarKCAL.max = calories
        bindingCartDetails.textViewKCAL.text = calories.toString()
        //protein
        bindingCartDetails.progressBarProtein.max = ((0.15*calories)/4).toInt()
        bindingCartDetails.textViewProtein.text = ((0.15*calories)/4).toInt().toString()
        //fat
        bindingCartDetails.progressBarFat.max = ((0.30*calories)/9).toInt()
        bindingCartDetails.textViewFat.text = ((0.30*calories)/9).toInt().toString()
        //carbohydrates
        bindingCartDetails.progressBarCarbohydrates.max = ((0.55*calories)/4).toInt()
        bindingCartDetails.textViewCarbohydrates.text = ((0.55*calories)/4).toInt().toString()
        //fiber
        bindingCartDetails.progressBarFiber.max = 40
        bindingCartDetails.textViewFiber.text = "40"
        //salt
        bindingCartDetails.progressBarSalt.max = 5
        bindingCartDetails.textViewSalt.text = "5"
    }

    private fun calculateCalories(weight:Int,  height:Int, age:Int, sex:Int) :Int {
        //for now 1 - male ,2 - female
        return if(sex == 1) {
            ((9.99*weight) + (6.25*height) - (4.92*age) + 5).toInt()
        } else {
            ((9.99*weight) + (6.25*height) - (4.92*age) - 161).toInt()
        }
    }
}