package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    var p_num = 3
    var k = 1
    val point_list = mutableListOf<Float>()
    var isBlind = false

    fun start() {
        setContentView(R.layout.activity_start)

        MobileAds.initialize(this) {}
        val adview : AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adview.loadAd(adRequest)

        val tv_pnum: TextView = findViewById(R.id.tv_pnum)
        val btn_minus: Button = findViewById(R.id.btn_minus)
        val btn_plus: Button = findViewById(R.id.btn_plus)
        val btn_start: Button = findViewById(R.id.btn_start)
        val btn_blind: Button = findViewById(R.id.btn_blind)

        btn_blind.setOnClickListener {
            isBlind = !isBlind
            if (isBlind == true) {
                btn_blind.text = "BLIND 모드 ON"
            }
            else {
                btn_blind.text = "BLIND 모드 OFF"
            }
        }

        tv_pnum.text = p_num.toString()
        btn_minus.setOnClickListener {
            p_num--
            if (p_num == 0) {
                p_num = 1
            }
            tv_pnum.text = p_num.toString()
        }
        btn_plus.setOnClickListener {
            p_num++
            tv_pnum.text = p_num.toString()
        }
        btn_start.setOnClickListener{
            main()
        }
    }

    fun main() {
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}
        val adview2 : AdView = findViewById(R.id.adView2)
        val adRequest = AdRequest.Builder().build()
        adview2.loadAd(adRequest)

        var timerTask: Timer? = null
        var stage = 1
        var sec: Int = 0
        val tv: TextView = findViewById(R.id.tv_random)
        val tv_t: TextView = findViewById(R.id.tv_timer)
        val tv_p: TextView = findViewById(R.id.tv_point)
        val tv_people: TextView = findViewById(R.id.tv_people)
        val btn: Button = findViewById(R.id.btn_start)
        val btn_i: Button = findViewById(R.id.btn_i)
        val random_box = Random()
        val num = random_box.nextInt(1001)
        val bg_main : ConstraintLayout = findViewById(R.id.bg_main)
        val color_list = mutableListOf<String>("#32E9321E", "#32E98E1E", "#32E9C41E", "#3287E91E", "#321EBDE9", "#321E79E9", "#32651EE9")
        var color_index = k%7-1
        if (color_index == -1){
            color_index = 6
        }

        val color_sel = color_list.get(color_index)
        bg_main.setBackgroundColor(Color.parseColor(color_sel))

        tv.text = ((num.toFloat()) / 100).toString()
        btn.text = "시작"
        tv_people.text = "참가자 $k"

        btn_i.setOnClickListener {
            point_list.clear()
            k = 1
            start()
        }

        btn.setOnClickListener {
            stage++
            if (stage == 2) {
                timerTask = kotlin.concurrent.timer(period = 10) {
                    sec++
                    runOnUiThread {
                        if (isBlind == false) {
                            tv_t.text = (sec.toFloat() / 100).toString()
                        }
                        else if (isBlind == true && stage == 2) {
                            tv_t.text = "???"
                        }
                    }
                }
                btn.text = "정지"
            } else if (stage == 3) {
                tv_t.text = (sec.toFloat() / 100).toString()
                timerTask?.cancel()
                val point = abs(sec - num).toFloat() / 100
                point_list.add(point)
                tv_p.text = "시간차 " + point.toString()
                btn.text = "다음"
                stage = 0
            } else if (stage == 1) {
                if (k < p_num) {
                    k++
                    main()
                } else {
                    end()
                }
            }
        }
    }

        fun end() {
            setContentView(R.layout.activity_end)

            MobileAds.initialize(this) {}
            val adview3 : AdView = findViewById(R.id.adView3)
            val adRequest = AdRequest.Builder().build()
            adview3.loadAd(adRequest)

            val tv_last: TextView = findViewById(R.id.tv_last)
            val tv_first: TextView = findViewById(R.id.tv_first)
            val tv_lpoint: TextView = findViewById(R.id.tv_lpoint)
            val tv_fpoint: TextView = findViewById(R.id.tv_fpoint)
            val btn_init: Button = findViewById(R.id.btn_init)
            tv_lpoint.text = "시간차 " + (point_list.maxOrNull()).toString()
            tv_fpoint.text = "시간차 " + (point_list.minOrNull()).toString()
            var index_last = point_list.indexOf(point_list.maxOrNull())
            var index_first = point_list.indexOf(point_list.minOrNull())
            tv_first.text = "참가자 "+(index_first + 1).toString()
            tv_last.text = "참가자 "+(index_last + 1).toString()
            btn_init.setOnClickListener {
                point_list.clear()
                k = 1
                start()
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            start()
        }
    }



