package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() , View.OnClickListener{

    private lateinit var btn_start : Button
    private lateinit var btn_refresh : Button
    private lateinit var tv_minute : TextView
    private lateinit var tv_second : TextView
    private lateinit var tv_millisecond : TextView

    //스톱워치가 실행중인지 아닌지를 나타낼 bool 변수
    private var isRunning = false

    //Timer : 시간을 측정해볼 수 있는 자바에서 제공하는 클래스
    private var timer : Timer? = null
    private var time = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start = findViewById(R.id.btn_start)
        btn_refresh = findViewById(R.id.btn_refresh)
        tv_minute = findViewById(R.id.tv_minute)
        tv_second = findViewById(R.id.tv_second)
        tv_millisecond = findViewById(R.id.tv_millisecond)

        //this : 상속받은 onClickListener를 버튼과 연결시켜줌줌
       btn_start.setOnClickListener(this)
        btn_refresh.setOnClickListener(this)

    }
    override fun onClick(v: View?) {
         when(v?.id){
             R.id.btn_start -> {
                if(isRunning){
                    pause()
                }
                 else{
                     start()
                }
             }
             R.id.btn_refresh -> {
                 refresh()
             }
         }

    }
    private fun start(){
        //스톱워치 시작을 누르면 버튼이 바뀌어야 함
        btn_start.text = getString(R.string.btn_pause)
        btn_start.setBackgroundColor(getColor(R.color.btn_pause))
        isRunning = true

        //시간초가 바뀌는 부분, 코틀린에서 timer부분은 일정 주기로 반복하는 동작을 수행할 때 유용하게 쓰임
        //10millisecond마다 반복됨, Timer 함숭는 항상 백그라운드 스레드에서 실행됨
        //1000ms = 1초 : 0.01 time 1+
        timer = timer(period = 10){
            time++

         //0.01초마다 time++ -> 이걸 분, 초, m초를 구해서 변수에 저장하기
            val milli_second = time % 100
            val second = (time % 6000) / 100
            val minute = time / 6000
        //3개의 text에 시간 넣어주기
            //아래 코드에서 에러 발생 : timer : 백그라운드 스레드에서 실행됨 : 근데 .text로 ui를 변경하려 하고 있음 : runOnUiThread 함수를 활용하기

            //text에 접근하는 부분을 백그라운드 Thread가 아닌 Main Thread에서 처리하도록 구현현
           runOnUiThread{
               //초기화 버튼으로 text가 변경되는 것과 충돌을 막기위해 if문으로 실행중일때만 text가 변경되는것으로 조건문 추가
               if(isRunning){
                   tv_millisecond.text = if(milli_second < 10) ".0${milli_second}" else ".${milli_second}"
                   tv_second.text = if(second < 10) ":0${second}" else ":${second}"
                   tv_minute.text = "${minute}"
               }

            }

        }
    }
    //중단
    private fun pause(){
        //실행 중담 -> 실행으로 버튼을 바꿔주기
        btn_start.text = getString(R.string.btn_start)
        btn_start.setBackgroundColor(getColor(R.color.btn_start))

        //timer를 멈춤, time가 0.01초씩 증가하던 것을 중단, text는 그대로
        isRunning = false
        timer?.cancel()

    }
    //초기화
    private fun refresh(){
        //증가하던 time 변수를 중단
        timer?.cancel()

        btn_start.text = getString(R.string.btn_start)
        btn_start.setBackgroundColor(getColor(R.color.btn_start))

        isRunning = false
        //초기화 버튼을 누르면 time변수와 text가 초기화되도록
        time = 0
        tv_millisecond.text = ",00"
        tv_second.text = ":00"
        tv_minute.text = "00"

    }


}