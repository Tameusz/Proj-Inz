package com.example.proj_inz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proj_inz.databinding.ActivityQuestionsBinding

import android.content.SharedPreferences
import android.widget.Toast
import androidx.core.view.get


class QuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.supportActionBar?.hide()
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = getSharedPreferences("ApplicationPREF", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()

        binding.confirmButtonQuestions.setOnClickListener {
            if(binding.qWeight.text.toString() == ""
                && binding.qHeight.text.toString() == ""
                && binding.qAge.text.toString() == ""
                && binding.radioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Uzupełnij dane!!!", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, HelpActivity::class.java))
                addSharedPreferences(editor)
                val ed: SharedPreferences.Editor = pref.edit()
                ed.putBoolean("questions_activity_executed", true)
                ed.apply()
            }
        }

        if (pref.getBoolean("questions_activity_executed", false)) {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun addSharedPreferences(ed: SharedPreferences.Editor) {
        ed.putInt("user_weight", binding.qWeight.text.toString().toInt())
        ed.putInt("user_height", binding.qHeight.text.toString().toInt())
        ed.putInt("user_age", binding.qAge.text.toString().toInt())
        //female-2131231106, male-2131231107
        if(binding.radioGroup.checkedRadioButtonId == 2131231106) {
            ed.putInt("user_gender", 2)
        } else if(binding.radioGroup.checkedRadioButtonId == 2131231107) {
            ed.putInt("user_gender", 1)
        }
        ed.apply()
    }
}