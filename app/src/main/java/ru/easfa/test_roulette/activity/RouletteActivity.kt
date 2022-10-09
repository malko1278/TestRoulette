
package ru.easfa.test_roulette.activity

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import ru.easfa.test_roulette.databinding.ActivityRouletteBinding

class RouletteActivity : AppCompatActivity(), Animation.AnimationListener {

    private lateinit var binding: ActivityRouletteBinding
    
    private var counter = 0
    private var flag = false

    var prizes = intArrayOf(200, 1000, 600, 1000, 500, 400, 200, 700, 3000, 400, 1000, 1200)
    private var mSpinDuration: Long = 0
    private var mSpinRevolution = 0f
    var prizeText = "N/A"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouletteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Spin Game"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.powerButton.setOnClickListener {
            spinAction()
        }
        binding.powerButton.setOnTouchListener(PowerTouchListener())
    }

    override fun onAnimationStart(animation: Animation?) {
        binding.infoText.text = "Подождите ..."// "Spinning..."
    }

    override fun onAnimationEnd(animation: Animation?) {
        binding.infoText.text = prizeText
    }

    override fun onAnimationRepeat(animation: Animation?) {}

    private inner class PowerTouchListener : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when(event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    flag = true
                    counter = 0
                    Thread{
                        while (flag) {
                            counter++
                            if (counter == 100) {
                                try {
                                    Thread.sleep(100)
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                                counter = 0
                            }
                            try {
                                Thread.sleep(10)
                            }
                            catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                    }.start()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    flag = false
                    spinAction()
                    return false
                }
            }
            return false
        }
    }

    private fun spinAction() {
        mSpinRevolution = 3600f
        mSpinDuration = 5000

        if (counter >= 30) {
            mSpinDuration = 1000
            mSpinRevolution = (3600 * 2).toFloat()
        }
        if (counter >= 60) {
            mSpinDuration = 15000
            mSpinRevolution = (3600 * 3).toFloat()
        }

        // random : 0-360
        val end = Math.floor(Math.random() * 3600).toInt()
        // quantity of prize
        val numOfPrizes = prizes.size
        // size of sector per prize in degrees
        val degreesPerPrize = 360/numOfPrizes
        // shit where the arrow points
        val shift = 0
        val prizeIndex = (shift + end) % numOfPrizes

        prizeText = "Цена : ${prizes[prizeIndex]}"

        val rotateAnim = RotateAnimation(
            0f,mSpinRevolution + end,
            Animation.RELATIVE_TO_SELF,
            0.5f,Animation.RELATIVE_TO_SELF,0.5f
        )
        rotateAnim.interpolator = DecelerateInterpolator()
        rotateAnim.repeatCount = 0
        rotateAnim.duration = mSpinDuration
        rotateAnim.setAnimationListener(this)
        rotateAnim.fillAfter = true
        binding.imageWheel.startAnimation(rotateAnim)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("counterSaved", counter)
        outState.putBoolean("flagSaved", flag)


    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        counter = savedInstanceState.getInt("counterSaved")
        flag = savedInstanceState.getBoolean("flagSaved")
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("FIRST_KEY", "FIRST_FINISH")
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }
}