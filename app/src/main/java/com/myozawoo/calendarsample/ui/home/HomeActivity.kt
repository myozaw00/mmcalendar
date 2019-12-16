package com.myozawoo.calendarsample.ui.home

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.myozawoo.calendarsample.R
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this, "ca-app-pub-7334296160561770/1003614772")
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        val viewPagerAdapter = CalendarViewPagerAdapter(this, getList())
        viewPager.adapter = viewPagerAdapter

        val fullScreenAdRequest = AdRequest.Builder().build()
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-7334296160561770/8940942409"
        mInterstitialAd.loadAd(fullScreenAdRequest)

        Handler().postDelayed({
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            }else{
                showToast("AD wasn't loaded yet.")
            }
        }, 10000)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getList(): List<Date> {
        val list = arrayListOf<Date>()
        list.add(Date(2019, 12, 1))
        list.add(Date(2020, 1, 1))
        list.add(Date(2020,2,1))
        list.add(Date(2020,3,1))
        list.add(Date(2020,4,1))
        list.add(Date(2020,5,1))
        list.add(Date(2020,6,1))
        list.add(Date(2020,7,1))
        list.add(Date(2020,8,1))
        list.add(Date(2020,9,1))
        list.add(Date(2020,10,1))
        list.add(Date(2020,11,1))
        list.add(Date(2020,12,1))
        return list
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}