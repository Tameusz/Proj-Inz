package com.example.proj_inz

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import com.example.proj_inz.databinding.CartBinding
import com.example.proj_inz.databinding.CartDetailsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


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
        //shared preferences
        val sharedPreferences = getSharedPreferences("ApplicationPREF", MODE_PRIVATE)
        val userCalories = calculateCalories(
            sharedPreferences.getInt("user_weight",0),
            sharedPreferences.getInt("user_height",0),
            sharedPreferences.getInt("user_age",0),
            sharedPreferences.getInt("user_gender",1)
        )
        val forHowManyDays = sharedPreferences.getFloat("shopping_for",1f)
        progressBarFirstAdjustment(userCalories, forHowManyDays)
        buttonsFunctionality(sharedPreferences.edit(), userCalories, forHowManyDays)
        adjustProductList()
        notifyAboutMax(bindingCartDetails.progressBarKCAL,"dostarczanych kalorii")
        notifyAboutMax(bindingCartDetails.progressBarProtein,"dostarczanego białka")
        notifyAboutMax(bindingCartDetails.progressBarFat,"dostarczanych tłuszczy")
        notifyAboutMax(bindingCartDetails.progressBarCarbohydrates,"dostarczanych weglowodanów")
        notifyAboutMax(bindingCartDetails.progressBarFiber,"dostarczanego błonnika")
        notifyAboutMax(bindingCartDetails.progressBarSalt,"dostarczanej soli")
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
            R.id.homeButton -> { startActivity(Intent(this, MainActivity::class.java)) }
            R.id.barcodeButton -> { startActivity(Intent(this, BarcodeReaderActivity::class.java)) }
            R.id.recognizerButton -> { startActivity(Intent(this, TextRecognizerActivity::class.java)) }
            R.id.cartButton -> { setContentView(bindingCart.root) }
            R.id.cartDetailsButton -> { setContentView(bindingCartDetails.root) }
            else -> { }
        }

        return super.onOptionsItemSelected(item)
    }
    private fun progressBarFirstAdjustment(calories: Int, forHowManyDays: Float) {
        progressMax(calories,forHowManyDays)
        progressTextAdjust("0/${(forHowManyDays*calories)}", "0/${(forHowManyDays*((0.15*calories)/4)).toInt()}", "0/${(forHowManyDays*((0.30*calories)/9)).toInt()}", "0/${(forHowManyDays*((0.55*calories)/4)).toInt()}", "0/${(forHowManyDays*40)}", "0/${(forHowManyDays*5)}")
    }
    private fun calculateCalories(weight:Int,  height:Int, age:Int, sex:Int) :Int {
        //for now 1 - male ,2 - female
        return if(sex == 1) {
            ((9.99*weight) + (6.25*height) - (4.92*age) + 5).toInt()
        } else {
            ((9.99*weight) + (6.25*height) - (4.92*age) - 161).toInt()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun buttonsFunctionality(editor: SharedPreferences.Editor, calories: Int, forHowManyDays: Float) {
        bindingCart.cartToBarcode.setOnClickListener { startActivity(Intent(this, BarcodeReaderActivity::class.java)) }
        bindingCart.cartToRecognizer.setOnClickListener { startActivity(Intent(this, TextRecognizerActivity::class.java)) }
        bindingCart.cartToCartDetails.setOnClickListener { setContentView(bindingCartDetails.root) }
        bindingCartDetails.cartDetailsToBarcode.setOnClickListener { startActivity(Intent(this, BarcodeReaderActivity::class.java)) }
        bindingCartDetails.cartDetailsToRecognizer.setOnClickListener { startActivity(Intent(this, TextRecognizerActivity::class.java)) }
        bindingCartDetails.cartDetailsToCart.setOnClickListener { setContentView(bindingCart.root) }
        bindingCart.cartClearButton.setOnClickListener {
            editor.putString("listOfProductsPREF", "")
            editor.apply()
            bindingCart.shopItemList.text = "Produkty w wózku"
            progressAdjust(0,0,0,0,0,0)
            progressTextAdjust("0/${(forHowManyDays*calories)}", "0/${(forHowManyDays*((0.15*calories)/4)).toInt()}", "0/${(forHowManyDays*((0.30*calories)/9)).toInt()}", "0/${(forHowManyDays*((0.55*calories)/4)).toInt()}", "0/${(forHowManyDays*40)}", "0/${(forHowManyDays*5)}")
        }
    }
    @SuppressLint("SetTextI18n")
    private fun adjustProductList() {
        progressAdjust(0,0,0,0,0,0)
        val pref = getSharedPreferences("ApplicationPREF", Context.MODE_PRIVATE)
        val listPREF = pref.getString("listOfProductsPREF", "")
        val gson = Gson()
        val itemType = object : TypeToken<ArrayList<Product>>() {}.type
        val list = gson.fromJson<ArrayList<Product>>(listPREF, itemType)

        if(gson.fromJson<ArrayList<Product>>(listPREF, itemType) != null) {
            for(listElement in list) {
                bindingCart.shopItemList.text = "${bindingCart.shopItemList.text} \n${listElement.productName} ${listElement.productAmount}g"
                progressAdjust(bindingCartDetails.progressBarKCAL.progress + listElement.energy100g.toInt(), bindingCartDetails.progressBarProtein.progress + listElement.proteins100g.toInt(), bindingCartDetails.progressBarFat.progress + listElement.fat100g.toInt(), bindingCartDetails.progressBarCarbohydrates.progress + listElement.carbohydrates100g.toInt(), bindingCartDetails.progressBarFiber.progress + listElement.fiber100g.toInt(), bindingCartDetails.progressBarSalt.progress + listElement.salt100g.toInt())
            }
            progressTextAdjust("${bindingCartDetails.progressBarKCAL.progress}/${bindingCartDetails.progressBarKCAL.max}", "${bindingCartDetails.progressBarProtein.progress}/${bindingCartDetails.progressBarProtein.max}", "${bindingCartDetails.progressBarFat.progress}/${bindingCartDetails.progressBarFat.max}", "${bindingCartDetails.progressBarCarbohydrates.progress}/${bindingCartDetails.progressBarCarbohydrates.max}", "${bindingCartDetails.progressBarFiber.progress}/${bindingCartDetails.progressBarFiber.max}", "${bindingCartDetails.progressBarSalt.progress}/${bindingCartDetails.progressBarSalt.max}")
        }
    }
    private fun progressAdjust(valKCAL: Int, valProtein: Int, valFat: Int, valCarbohydrates: Int, valFiber: Int, valSalt: Int) {
        bindingCartDetails.progressBarKCAL.progress = valKCAL
        bindingCartDetails.progressBarProtein.progress = valProtein
        bindingCartDetails.progressBarFat.progress = valFat
        bindingCartDetails.progressBarCarbohydrates.progress = valCarbohydrates
        bindingCartDetails.progressBarFiber.progress = valFiber
        bindingCartDetails.progressBarSalt.progress = valSalt
    }
    private fun progressTextAdjust(textKCAL: String, textProtein: String, textFat: String, textCarbohydrates: String, textFiber: String, textSalt: String) {
        bindingCartDetails.textViewKCAL.text = textKCAL
        bindingCartDetails.textViewProtein.text = textProtein
        bindingCartDetails.textViewFat.text = textFat
        bindingCartDetails.textViewCarbohydrates.text = textCarbohydrates
        bindingCartDetails.textViewFiber.text = textFiber
        bindingCartDetails.textViewSalt.text = textSalt
    }
    private fun progressMax(calories: Int, forHowManyDays: Float) {
        bindingCartDetails.progressBarKCAL.max = (forHowManyDays*calories).toInt()
        bindingCartDetails.progressBarProtein.max = (forHowManyDays*((0.15*calories)/4)).toInt()
        bindingCartDetails.progressBarFat.max = (forHowManyDays*((0.30*calories)/9)).toInt()
        bindingCartDetails.progressBarCarbohydrates.max = (forHowManyDays*((0.55*calories)/4)).toInt()
        bindingCartDetails.progressBarFiber.max = (forHowManyDays*40).toInt()
        bindingCartDetails.progressBarSalt.max = (forHowManyDays*5).toInt()
    }
    private fun notifyAboutMax(progressBar: ProgressBar, text: String) {
        if(progressBar.max == progressBar.progress) {
            Toast.makeText(this, "Osiągąłeś limit $text", Toast.LENGTH_SHORT).show()
        }
    }
}