package com.example.smartexam

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.smartexam.databinding.ActivityQuestionBinding
import com.example.smartexam.databinding.ScoreDailogBinding

class QuestionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityQuestionBinding
    var currentQuestionIndex = 0
    var selectAnswer = ""
    var score = 0
    var nextButtonClicked = false // Declare the variable

    companion object {
        var questionModelList: List<QuestionModel1> = listOf()
        var time: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Disable notifications and prevent other apps from opening
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        binding.apply {
            btn0.setOnClickListener(this@QuestionActivity)
            btn1.setOnClickListener(this@QuestionActivity)
            btn2.setOnClickListener(this@QuestionActivity)
            btn3.setOnClickListener(this@QuestionActivity)
            nextButton.setOnClickListener(this@QuestionActivity)
        }
        loadQuestions()
        startTimer()
    }

    private fun startTimer() {
        val totalTimeInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.timeIndicatorTextview.text = String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                // Finish the exam
                finishExam()
            }
        }.start()
    }

    private fun loadQuestions() {
        selectAnswer = ""
        if (currentQuestionIndex == questionModelList.size) {
            finishExam()
            return
        }
        binding.apply {
            questionIndicatorTextview.text = "Question ${currentQuestionIndex + 1} / ${questionModelList.size} "
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question

            btn0.text = questionModelList[currentQuestionIndex].option[0]
            btn1.text = questionModelList[currentQuestionIndex].option[1]
            btn2.text = questionModelList[currentQuestionIndex].option[2]
            btn3.text = questionModelList[currentQuestionIndex].option[3]
        }
    }

    override fun onClick(view: View?) {
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }
        val clickedBtn = view as Button
        if (clickedBtn.id == R.id.next_button) {
            // Next button is clicked
            if(selectAnswer.isEmpty()){
                Toast.makeText(applicationContext,"Please Select Answer to Continue",Toast.LENGTH_SHORT).show()
                return
            }
            nextButtonClicked = true // Update the variable
            if (selectAnswer == questionModelList[currentQuestionIndex].correct)
                score++
            Log.i("Score of Exam", score.toString())
            currentQuestionIndex++
            loadQuestions()
        } else {
            // Option button is clicked
            selectAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.orange))
        }
    }

    override fun onBackPressed() {
        if (nextButtonClicked) {
            // Disable back button functionality
            Toast.makeText(this, "You cannot go back!", Toast.LENGTH_SHORT).show()
        } else {
            // Allow normal back button functionality
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        // Abort the exam if the app is sent to the background
        if (!isAppInForeground()) {
            Toast.makeText(this, "Exam aborted due to app going to background!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun isAppInForeground(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        val packageName = packageName
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
                return true
            }
        }
        return false
    }

    private fun finishExam() {
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        val dialogBinding = ScoreDailogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if (percentage > 60) {
                scoreTitle.text = "Congrats! You have passed"
                scoreTitle.setTextColor(Color.GREEN)
            } else {
                scoreTitle.text = "Oops! You have Failed"
                scoreTitle.setTextColor(Color.RED)
            }

            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            finishBtn.setOnClickListener {
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }
}
