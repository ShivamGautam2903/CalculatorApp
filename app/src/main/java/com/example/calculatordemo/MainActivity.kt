package com.example.calculatordemo

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.room.Room
import com.example.calculatordemo.Database.CalcHistory
import com.example.calculatordemo.Database.CalculationDatabase
import com.example.calculatordemo.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var database: CalculationDatabase

    var usingOperation = true
    var usingDecimal = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.tvDisplay.movementMethod = ScrollingMovementMethod()
        binding.tvLast.movementMethod = ScrollingMovementMethod()
        binding.history.movementMethod = ScrollingMovementMethod()

        binding.apply {

            btnback.setOnClickListener {
                backspace(tvDisplay)
            }
            btnback.setOnLongClickListener {
                tvDisplay.text = ""
                tvLast.text = ""
                usingDecimal = false
                true
            }
            btnequal.setOnClickListener {
                if (usingOperation == false) // handles the case where the expression is like "5+"
                {
                    var str = tvDisplay.text.toString()
                    var e = ExpressionBuilder(str).build()

                    try {
                        var result = e.evaluate()
                        tvLast.text = result.toString()
                    } catch (e: ArithmeticException) {
                        tvLast.text = "ERROR"
                        println(e.message)
                    }
                    GlobalScope.launch {
                        database.calculationDao()
                            .insertCalc(CalcHistory(calc = "${tvDisplay.text.toString()}"))
                        runOnUiThread(){
                            tvDisplay.text = ""
                        }
                    }
                    usingOperation = true
                    usingDecimal = false
                }
            }
        }

        database = Room.databaseBuilder(
            applicationContext,
            CalculationDatabase::class.java,
            "calculationDB"
        ).build()


        database.calculationDao().getAll().observe(this) {
            val sb = StringBuilder()
            for (calc in it) {
                sb.append(calc.calc)
                sb.append("\n")
            }
            binding.history.text = sb.toString()
        }

    }

    fun backspace(tvDisplay: TextView): Unit {
        val length = tvDisplay.length()
        if (usingOperation)  //checks the condition where we delete an operator
            usingOperation = false
        if (length > 0) {
            if (tvDisplay.text[length - 1] == '.') {
                usingDecimal = false
            }
            tvDisplay.text = tvDisplay.text.subSequence(0, length - 1)
        }
    }

    fun numberAction(view: View) {
        if (view is Button) {
            binding.tvDisplay.append(view.text)
            usingOperation = false
        }
    }

    fun operatorAction(view: View) {
        if (view is Button && !usingOperation) {
            binding.tvDisplay.append(view.text)
            usingOperation = true
            usingDecimal = false
        }
    }

    fun decimalAction(view: View) {
        if (view is Button && !usingDecimal) {
            binding.tvDisplay.append(view.text)
            usingDecimal = true
        }
    }

}