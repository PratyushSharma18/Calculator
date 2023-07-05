package com.pratyushvkp.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.pratyushvkp.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false
    private lateinit var expression: Expression
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClear(view: View) {
        binding.dataEntryView.text =""
        lastNumeric = false
    }
    fun onback(view: View) {
        binding.dataEntryView.text = binding.dataEntryView.text.toString().dropLast(1)
        try {
            val lastChar = binding.dataEntryView.text.toString().last()
            if (lastChar.isDigit()){
                onEqual()
            }
        }catch (e:Exception){
            binding.resultView.text=""
            binding.resultView.visibility = View.GONE
            Log.e("lastCharError",e.toString())
        }
    }
    fun onOperatorClick(view: View) {
        if(!stateError && lastNumeric){
            binding.dataEntryView.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }

    }
    fun onAllClear(view: View) {
        binding.dataEntryView.text =""
        binding.resultView.text=""
        binding.resultView.visibility = View.GONE
        lastNumeric = false
        stateError = false
        lastDot = false
    }
    fun onDigitClick(view: View) {
        if(stateError){
            binding.dataEntryView.text = (view as Button).text
            stateError = false
        }else{
            binding.dataEntryView.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()

    }
    fun onEqualClick(view: View) {
        onEqual()
        binding.dataEntryView.text = binding.resultView.text.toString().drop(1)
    }

    fun onEqual(){
        if(lastNumeric && !stateError){
            val txt = binding.dataEntryView.text.toString()
            expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                binding.resultView.visibility = View.VISIBLE
                binding.resultView.text = "= " + result.toString()
            } catch (ex: ArithmeticException){
                Log.e("evaluateError",ex.toString())
                binding.resultView.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}