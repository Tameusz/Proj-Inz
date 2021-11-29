package com.example.proj_inz

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
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
    private lateinit var temporaryBitmapImage: Bitmap
    private var cropUriContent: Uri? = null
    val REQUEST_IMAGE_CAPTURE = 1
    val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            cropUriContent = result.uriContent
            bindingRecognizer.ivPhotoToRecognize.setImageURI(cropUriContent)
        } else {
            // an error occurred
            val exception = result.error
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingRecognizer = TextRecognizerBinding.inflate(layoutInflater)
        bindingRecognizerDetails = TextRecognizerDetailsBinding.inflate(layoutInflater)
        bindingRecognizerError = TextRecognizerErrorBinding.inflate(layoutInflater)

        setContentView(bindingRecognizer.root)

        bindingRecognizer.buttonRecognizerConfirm.setOnClickListener {
            setContentView(bindingRecognizerDetails.root)
            //recognizeTextFromImage()
        }
        bindingRecognizerDetails.buttonRecognizerDetailsConfirm.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        bindingRecognizerDetails.buttonRecognizerDetailsToRecognizer.setOnClickListener {
            setContentView(bindingRecognizer.root)
        }
        bindingRecognizerError.errorBarcodeToBarcode.setOnClickListener {
            startActivity(Intent(this, BarcodeReaderActivity::class.java))
        }
        bindingRecognizerError.errorBarcodeToRecognizer.setOnClickListener {
            setContentView(bindingRecognizer.root)
        }

        bindingRecognizer.buttonRecognizerToCamera.setOnClickListener {
            dispatchTakePictureIntent()
            bindingRecognizer.ivPhotoToRecognize.setImageResource(0)
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
            R.id.recognizerButton -> { }
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

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {

            //CropImage.activity()
            //    .setGuidelines(CropImageView.Guidelines.ON)
            //    .start(this)
            //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

            // start picker to get image for cropping and then use the image in cropping activity
            cropImage.launch(
                options {
                    setGuidelines(CropImageView.Guidelines.ON)
                }
            )
            // start cropping activity for pre-acquired image saved on the device and customize settings
            //cropImage.launch(
            //    options(uri = imageUri) {
            //        setGuidelines(CropImageView.Guidelines.ON)
            //        setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            //    }
            //)


        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //bindingRecognizer.ivPhotoToRecognize.setImageURI(cropUriContent)
                temporaryBitmapImage = bindingRecognizer.ivPhotoToRecognize.drawToBitmap()
                val imageToRecognize = InputImage.fromBitmap(temporaryBitmapImage,0)
                textRecognizer(imageToRecognize)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun textRecognizer(image: InputImage) {
        bindingRecognizerDetails.tvProductDetailsTextRecognizer.text = "Dane o produkcie\n"
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val blocks: List<Text.TextBlock> = visionText.getTextBlocks()
                for (block in visionText.textBlocks) {
                    val boundingBox = block.boundingBox
                    val cornerPoints = block.cornerPoints
                    val text = block.text


                    for (line in block.lines) {
                        // ...
                        for (element in line.elements) {
                            bindingRecognizerDetails.tvProductDetailsTextRecognizer.text = "${bindingRecognizerDetails.tvProductDetailsTextRecognizer.text} ${element.text}"
                            // ...
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@TextRecognizerActivity, "${e.printStackTrace()}",Toast.LENGTH_SHORT).show()
            }
    }

    //cropping image

    private fun startCrop() {
    val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
        } else {
            // an error occurred
            val exception = result.error
        }
    }
        // start picker to get image for cropping and then use the image in cropping activity
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
            }
        )
        //start picker to get image for cropping from only gallery and then use the image in
        //cropping activity
        cropImage.launch(
            options {
                setImagePickerContractOptions(
                    PickImageContractOptions(includeGallery = true, includeCamera = false)
                )
            }
        )
        // start cropping activity for pre-acquired image saved on the device and customize settings
        //cropImage.launch(
        //    options(uri = imageUri) {
        //        setGuidelines(CropImageView.Guidelines.ON)
        //        setOutputCompressFormat(Bitmap.CompressFormat.PNG)
        //    }
        //)
    }
}
