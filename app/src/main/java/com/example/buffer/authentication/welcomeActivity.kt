package com.example.buffer.authentication

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.example.buffer.R
import com.example.buffer.adapters.dashboardAdapter
import com.example.buffer.ui.MainActivity
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.*
import kotlin.collections.ArrayList

class welcomeActivity : AppCompatActivity() {
    private lateinit var dashboardAdapter: dashboardAdapter
    lateinit var  dots : ArrayList<TextView>
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar!!.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        next.setOnClickListener {
            if(getItem(0)<2){
                viewPager.setCurrentItem(getItem(1),true)
            }
            else{
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }

        }
        back.setOnClickListener {
            if(getItem(0)>0){
                viewPager.setCurrentItem(getItem(-1),true)
            }

        }
        skipLayout.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        dashboardAdapter = dashboardAdapter(this)
        viewPager.adapter=dashboardAdapter



        viewPager.setOnPageChangeListener( object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onPageSelected(position: Int) {
                setUpIndicator(position)
                if(position>0){
                    back.visibility= View.VISIBLE
                }
                else{
                    back.visibility=View.GONE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    fun getItem(i:Int) :Int{
        return viewPager.currentItem +i
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun setUpIndicator(position:Int){
        dots = ArrayList()
        indicatorLayout.removeAllViews()
        for(i in 0 until 3){
            dots.add( TextView(this))
            dots[i].setText(Html.fromHtml("&#8226"))
            dots[i].textSize=35F
            dots[i].setTextColor(resources.getColor(R.color.purple_500,applicationContext.theme))
            indicatorLayout.addView(dots[i])
        }
        dots[position].setTextColor(resources.getColor(R.color.purple_200,applicationContext.theme))
    }
}