package helloworld.zq.com.kotlindemo.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import helloworld.zq.com.kotlindemo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //private var name : String? = ""
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_test.setText(sum(3,6))
    }

    fun sum(a : Int,b : Int) : String{
        sum1(3,6)
        return (a + b).toString()
    }

    fun sum1(a : Int,b: Int){
        Log.i(TAG,"a+b = " + (a+b))
        val sumLambda : (Int , Int) -> Int ={x,y -> x + y}
        Log.i(TAG,"sumLambda = " + sumLambda(3,6))

        var a = 1
        val s1 = "a is $a"

        a = 2
        val s2 = "${s1.replace("is","was")},but now is $a"
        Log.i(TAG,"s1 = " + s1)
        Log.i(TAG,"s2 = " + s2)
        for (i in 1..3) {
            for (j in 1..3) {
                if (j == 2) continue
                Log.i(TAG, "i = " + i)
                Log.i(TAG, "j = " + j)
            }
        }

        loop@ for (i in 1..3){
            for (j in 1..3){
                if (j == 2) continue@loop
                Log.i(TAG,"@loop i = "+ i)
                Log.i(TAG,"@loop j = "+ j)
            }
        }
    }
}
