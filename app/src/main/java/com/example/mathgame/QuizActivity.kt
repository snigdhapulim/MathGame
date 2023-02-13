package com.example.mathgame

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.text.set
import androidx.core.widget.addTextChangedListener
import com.example.mathgame.databinding.ActivityQuizBinding
import com.example.mathgame.databinding.ActivitySelectOperatorBinding

private const val TAG = "QuizActivity"

public const val EXTRA_TEXT_LIST="com.example.mathgame.EXTRA_TEXT_LIST"

private lateinit var builder: AlertDialog.Builder

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding

    private var operator=""

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        operator=intent.getStringExtra(EXTRA_TEXT).toString()

        builder=AlertDialog.Builder(this)

        //checking the last question incase of rotation
        if(quizViewModel.getQuestionSize==0){
            generateQuestion();
        }
        else{
            existingQuestion();
        }

        //checking if the answer is empty
        binding.answer.addTextChangedListener(object : TextWatcher {

            @Override
            override fun afterTextChanged(s:Editable) {}

            @Override
            override fun beforeTextChanged(s:CharSequence, start:Int,
                                           count:Int, after:Int) {
            }

            @Override
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(binding.answer.text.isNotBlank()){
                    binding.submit.isEnabled=true
                }
                else{
                    binding.submit.isEnabled=false
                }
            }
        })

        binding.submit.setOnClickListener{view: View->
            checkAnswer();
            nextQuestion();
        }
    }
    private fun existingQuestion(){
        binding.question.text=quizViewModel.getCurrentQuestion
    }
    private fun generateQuestion(){
        val number1=(0..100).random()
        val number2=(0..100).random()

        val opt=if(operator.equals("Addition")){"+"}
        else{"-"}

        quizViewModel.addQuestion(number1.toString()+" "+opt+" "+number2.toString())
        existingQuestion()
    }
    private fun checkAnswer(){
        //should add if ans is right or wrong!
        quizViewModel.setAnswered(binding.answer.text.toString().toInt())
        //accordingly toster value!
        Toast.makeText(this, "Answer!", Toast.LENGTH_SHORT).show()

    }
    private fun nextQuestion(){
        if(quizViewModel.getQuestionSize==10){
            //pop up
            builder.setTitle("Quiz over! You Scored ${"insert score "}%!")
                .setMessage("Do you want to check your answers?")
                .setCancelable(true)
                .setPositiveButton("Yes"){dialogInterface ,it->
                    Log.d("TAG",quizViewModel.getListOfQuestion().toString())
                    val intent = Intent(this, ReviewActivity::class.java)
                    intent.putExtra(EXTRA_TEXT_LIST,quizViewModel.getListOfQuestion())
                    startActivity(intent)
                }
                .setNegativeButton("No"){dialogInterface ,it->
                    dialogInterface.cancel()
                }
                .setNeutralButton("re-take quiz"){dialogInterface ,it->
                    finish()
                }
                .show()
        }
        else{
            binding.answer.text=null;
            binding.submit.isEnabled=false;
            generateQuestion()
        }
    }
}