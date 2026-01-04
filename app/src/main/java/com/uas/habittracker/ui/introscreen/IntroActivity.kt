package com.uas.habittracker.ui.introscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.uas.habittracker.MainActivity
import com.uas.habittracker.R
import com.uas.habittracker.data.models.IntroView
import me.relex.circleindicator.CircleIndicator3

class IntroActivity : AppCompatActivity() {

    private lateinit var introView: List<IntroView>
    private lateinit var viewPager2: ViewPager2
    private lateinit var buttonStartApp: Button
    private lateinit var circleIndicator: CircleIndicator3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro)

        viewPager2 = findViewById(R.id.viewPager2)
        buttonStartApp = findViewById(R.id.btn_start_app)
        circleIndicator = findViewById(R.id.circleIndicator)

        addToIntroView()

        viewPager2.adapter = ViewPagerAdapter(introView)
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        circleIndicator.setViewPager(viewPager2)

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == 2) {
                    animationBuffer()
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })
    }

    private fun animationBuffer() {
        buttonStartApp.visibility = View.VISIBLE

        buttonStartApp.animate().apply {
            duration = 1400
            alpha(1f)

            buttonStartApp.setOnClickListener {
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }.start()
    }

    private fun addToIntroView() {
        introView = listOf(
            IntroView("Welcome to habit tracker!", R.drawable.ic_tea_filled),
            IntroView("This is second page", R.drawable.ic_smoke_filled),
            IntroView("This is final page", R.drawable.ic_fastfood_filled),
        )
    }
}