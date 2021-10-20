package com.example.proj_inz

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.proj_inz.databinding.ActivityMainBinding
import com.example.proj_inz.databinding.ActivityQuestionsBinding
import java.text.SimpleDateFormat
import java.util.*

class QuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.datePicker.setOnClickListener { view -> clickDatePicker(view) }
        binding.confirmButtonQuestions.setOnClickListener {
            val intent = Intent(this, CartDetailsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun clickDatePicker(view: View) {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month+1}/$year"
            binding.datePicker.setText(selectedDate)
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val chosenDateSimplified = simpleDateFormat.parse(selectedDate)!!.time / 60000
            val curerntDate = simpleDateFormat.parse(simpleDateFormat.format(System.currentTimeMillis()))!!.time / 60000
        }, year, month, dayOfMonth)
        datePickerDialog.datePicker.setMaxDate(Date().time - 86400000)
        datePickerDialog.show()
    }

}