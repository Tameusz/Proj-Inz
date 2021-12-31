package com.example.proj_inz

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.drawToBitmap
import com.canhub.cropper.*
import com.example.proj_inz.databinding.TextRecognizerBinding
import com.example.proj_inz.databinding.TextRecognizerDetailsBinding
import com.example.proj_inz.databinding.TextRecognizerErrorBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class TextRecognizerActivity : AppCompatActivity() {

    private lateinit var bindingRecognizer: TextRecognizerBinding
    private lateinit var bindingRecognizerDetails: TextRecognizerDetailsBinding
    private lateinit var bindingRecognizerError: TextRecognizerErrorBinding
    private var cropUriContent: Uri? = null
    val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            cropUriContent = result.uriContent
            bindingRecognizer.ivPhotoToRecognize.setImageURI(cropUriContent)
            textRecognizer(InputImage.fromBitmap(bindingRecognizer.ivPhotoToRecognize.drawToBitmap(),0))
        } else {
            println(result.error)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingRecognizer = TextRecognizerBinding.inflate(layoutInflater)
        bindingRecognizerDetails = TextRecognizerDetailsBinding.inflate(layoutInflater)
        bindingRecognizerError = TextRecognizerErrorBinding.inflate(layoutInflater)

        setContentView(bindingRecognizer.root)
        buttonsFunctionality()
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
            R.id.recognizerButton -> { }
            R.id.cartButton -> { startActivity(Intent(this, CartActivity::class.java)) }
            R.id.cartDetailsButton -> { startActivity(Intent(this, CartActivity::class.java).apply { putExtra("MESSAGE","toCartDetails") }) }
            else -> { }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dispatchTakePictureIntent() {
        try {
            // start picker to get image for cropping and then use the image in cropping activity
            cropImage.launch(options { setGuidelines(CropImageView.Guidelines.ON) })
        } catch (e: ActivityNotFoundException) {
            println(e.printStackTrace())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun textRecognizer(image: InputImage) {
        bindingRecognizerDetails.tvProductDetailsTextRecognizer.text = "Dane o produkcie\n"
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val blocks: List<Text.TextBlock> = visionText.textBlocks
                for (block in visionText.textBlocks) {
                    val boundingBox = block.boundingBox
                    val cornerPoints = block.cornerPoints
                    val text = block.text
                    for (line in block.lines) {
                            bindingRecognizerDetails.tvProductDetailsTextRecognizer.text = "${bindingRecognizerDetails.tvProductDetailsTextRecognizer.text} ${line.text}\n"
                        for (element in line.elements) {
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@TextRecognizerActivity, "${e.printStackTrace()}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun buttonsFunctionality() {
        bindingRecognizer.buttonRecognizerConfirm.setOnClickListener { setContentView(bindingRecognizerDetails.root) }
        bindingRecognizerDetails.buttonRecognizerDetailsConfirm.setOnClickListener { startActivity(Intent(this, CartActivity::class.java)) }
        bindingRecognizerDetails.buttonRecognizerDetailsToRecognizer.setOnClickListener { setContentView(bindingRecognizer.root) }
        bindingRecognizerError.errorBarcodeToBarcode.setOnClickListener { startActivity(Intent(this, BarcodeReaderActivity::class.java)) }
        bindingRecognizerError.errorBarcodeToRecognizer.setOnClickListener { setContentView(bindingRecognizer.root) }
        bindingRecognizer.buttonRecognizerToCamera.setOnClickListener {
            dispatchTakePictureIntent()
            bindingRecognizer.ivPhotoToRecognize.setImageResource(0)
        }
    }
}
