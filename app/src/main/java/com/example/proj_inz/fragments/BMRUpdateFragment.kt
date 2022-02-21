package com.example.proj_inz.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.proj_inz.R
import com.example.proj_inz.activities.MainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BMRUpdateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BMRUpdateFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_b_m_r_update,container,false)
        val pref = this.activity?.getSharedPreferences("ApplicationPREF", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref!!.edit()
        view.findViewById<Button>(R.id.confirmBMRFragment).setOnClickListener {
            if(view.findViewById<EditText>(R.id.uWeight).text.toString() == ""
                || view.findViewById<EditText>(R.id.uHeight).text.toString() == ""
                || view.findViewById<EditText>(R.id.uAge).text.toString() == ""
                || view.findViewById<RadioGroup>(R.id.radioGroupUpdate).checkedRadioButtonId == -1) {
                Toast.makeText(activity, "Uzupełnij dane!!!", Toast.LENGTH_SHORT).show()
            } else {
                updateSharedPreferences(editor)
                dismiss()
            }
        }
        return view
    }

    companion object {
        @JvmStatic fun newInstance(param1: String, param2: String) =
                BMRUpdateFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun updateSharedPreferences(ed: SharedPreferences.Editor) {
        ed.putInt("user_weight", view?.findViewById<EditText>(R.id.uWeight)?.text.toString().toInt())
        ed.putInt("user_height", view?.findViewById<EditText>(R.id.uHeight)?.text.toString().toInt())
        ed.putInt("user_age", view?.findViewById<EditText>(R.id.uAge)?.text.toString().toInt())
        //female-2131231283, male-2131231284
        if(view?.findViewById<RadioGroup>(R.id.radioGroupUpdate)?.checkedRadioButtonId == 2131231283) {
            ed.putInt("user_gender", 2)
        } else if(view?.findViewById<RadioGroup>(R.id.radioGroupUpdate)?.checkedRadioButtonId == 2131231284) {
            ed.putInt("user_gender", 1)
        }
        ed.apply()
    }
}