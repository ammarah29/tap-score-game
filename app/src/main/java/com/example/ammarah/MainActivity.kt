package com.example.ammarah

import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainActivity : AppCompatActivity() {

    internal lateinit var tapMeImageView: ImageView
    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal var score = 0
    internal var gameStarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 10000
    internal val initialCountDownInterval: Long = 1000
    internal val TAG = MainActivity::class.java.simpleName
    internal var timeLeftOnTimer: Long = 10000

    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        Timber.e("onCreate called. Score is: $score")
        tapMeImageView = findViewById(R.id.tap_me_button)
        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        resetGame()

        val bounce = ScaleAnimation(1.2f, 1f, 1.2f, 1f, 50f, 50f)
        bounce.duration = 600
        bounce.interpolator = BounceInterpolator()

        tapMeImageView.setOnClickListener{ view ->
            incrementScore()
            tapMeImageView.startAnimation(bounce)
        }
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()
        Timber.e("onSaveInstanceState: Saving Score: $score & Time Left: $timeLeftOnTimer")
    }

    override fun onDestroy(){
        super.onDestroy()

        Timber.e("onDestroy called.")
    }

    private fun resetGame(){
        score = 0
        gameScoreTextView.text = getString(R.string.your_score, score.toString())
        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.time_left, initialTimeLeft.toString())

        countDownTimer = object:CountDownTimer(initialCountDown, initialCountDownInterval){
            override fun onTick(millisUnitFinished: Long) {
                timeLeftOnTimer = millisUnitFinished
                val timeLeft = millisUnitFinished / 1000
                timeLeftTextView.text = getString(R.string.time_left, timeLeft.toString())
            }

            override  fun onFinish(){
                endGame()

            }
        }
        gameStarted = false
    }


    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }
    private fun endGame() {
        Toast.makeText(
            this,
            getString(R.string.game_over_message, score.toString()),
            Toast.LENGTH_SHORT
        ).show()
        resetGame()
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }
        score = score + 1
        val newScore = getString(R.string.your_score, score.toString())
        gameScoreTextView.text = newScore
    }
}