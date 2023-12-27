package com.example.lesson3.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lesson3.data.Invoice
import com.example.lesson3.databinding.FragmentStudentInput2Binding
import com.example.lesson3.repository.AppRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val ARG_PARAM1 = "student_param"

class FoodInputFragment : Fragment() {
    private lateinit var invoice: Invoice
    private lateinit var _binding : FragmentStudentInput2Binding
    var flag_validation = true
    var flag = false
    val binding
        get()=_binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val param1 = it.getString(ARG_PARAM1)
            if (param1==null)
                invoice=Invoice()
            else{
                val paramType= object : TypeToken<Invoice>(){}.type
                invoice = Gson().fromJson<Invoice>(param1, paramType)
            }
        }
    }

    fun validation (et1: String, et2: String, et3: String, et4: String, et5: String, et6: String, et7: String ){
        flag_validation = true
        if (et1.isBlank() or ! et1.matches(Regex("^[a-zA-Z_]+$"))) {
            binding.etName.requestFocus()
            binding.etName.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et2.isBlank() or !(et5.toString().toInt() > 1)) {
            binding.etWeight.requestFocus()
            binding.etWeight.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et3.isBlank() or !(et5.toString().toInt() > 1)) {
            binding.etPrice.requestFocus()
            binding.etPrice.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et4.isBlank()) {
            binding.etInfo.requestFocus()
            binding.etInfo.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et5.isBlank() or !(et5.toString().toInt() > 1)) {
            binding.editCalories.requestFocus()
            binding.editCalories.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et6.isBlank()) {
            binding.etComp.requestFocus()
            binding.etComp.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et7.isBlank() or !(et7.toString().toInt() > 1)) {
            binding.etPrep.requestFocus()
            binding.etPrep.setError("Введите корректные данные")
            flag_validation = false
        }
    }
    @SuppressLint("SimpleDateFormat")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        flag = !(invoice.shortName.isBlank())
        _binding = FragmentStudentInput2Binding.inflate(inflater, container, false)
        binding.btSave.text = if (flag) "Изменить" else "Добавить"
        binding.etName.setText(invoice.name)
        binding.etWeight.setText(invoice.weight.toString())
        binding.etPrice.setText(invoice.price.toString())
        binding.etInfo.setText(invoice.info)
        binding.editCalories.setText(invoice.calories.toString())
        binding.etComp.setText(invoice.comp)
        binding.etPrep.setText(invoice.prep.toString())
        binding.btCancel.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btSave.setOnClickListener {
            validation(
                binding.etName.text.toString(),
                binding.etWeight.text.toString(),
                binding.etPrice.text.toString(),
                binding.etInfo.text.toString(),
                binding.editCalories.text.toString(),
                binding.etComp.text.toString(),
                binding.etPrep.text.toString()
            )
            if (flag_validation) {
                invoice.name = binding.etName.text.toString()
                invoice.weight = binding.etWeight.text.toString().toInt()
                invoice.price = binding.etPrice.text.toString().toInt()
                invoice.info = binding.etInfo.text.toString()
                invoice.calories = binding.editCalories.text.toString().toInt()
                invoice.comp = binding.etComp.text.toString()
                invoice.prep = binding.etPrep.text.toString().toInt()
                if (flag)
                    AppRepository.getInstance().updateFood(invoice)
                else
                    AppRepository.getInstance().addFood(invoice)
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance(invoice: Invoice) =
            FoodInputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, Gson().toJson(invoice))
                }
            }
    }
}