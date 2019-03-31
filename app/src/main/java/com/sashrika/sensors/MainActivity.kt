package com.sashrika.sensors

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.Sensor.*
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var gravitySensorValues: FloatArray
    lateinit var accelerometerSensorValues: FloatArray


    val sensorManager :SensorManager  by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @RequiresApi(Build.VERSION_CODES.KITKAT)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//default delay constants for sampling period us in register listener * fastest or game or in microseconds
        val accelerometer  = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val list = sensorManager.getSensorList(Sensor.TYPE_ALL)
//        Log.e("TAG", "no. of sensors is ${list.size}")
//
//        for(s in list){
//            Log.e("TAG", "____________________________________________")
//            Log.e("TAG", "name is ${s.name}")
//            Log.e("TAG", "vendor is ${s.vendor}")
//            Log.e("TAG", "power is ${s.power}")
//            Log.e("TAG", "max count is ${s.fifoMaxEventCount}")
//            Log.e("TAG", "min delay is ${s.minDelay}")
//            Log.e("TAG", "resolution is ${s.resolution}")
//            Log.e("TAG", "version is ${s.version}")
//            Log.e("TAG", "type is ${s.stringType}")
//            Log.e("TAG", "_____________________________________________")
//        }

        val gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
//        val sensorEventListener = object : SensorEventListener{
        sensorManager.registerListener(this, accelerometer, 100000)
        sensorManager.registerListener(this, gravity, 100000)

        }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {


    }
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {sensorEvent->
            when (sensorEvent.sensor.type) {
                TYPE_GRAVITY -> {
                    gravitySensorValues = sensorEvent.values
                    sensorEvent.values.forEachIndexed { index, fl ->
                        Log.e("TAG", "gravity Sensor's value at index $index is $fl")
                    }
                }
                TYPE_ACCELEROMETER -> {
                    accelerometerSensorValues = sensorEvent.values
//                    sensorEvent.values.forEachIndexed { index, fl ->
//                        Log.e("TAG", "Proximity Sensor's value at index $index is $fl")
//                    }

                    val x = Math.abs(accelerometerSensorValues[0])
                    val y = Math.abs(accelerometerSensorValues[1])
                    val z = Math.abs(accelerometerSensorValues[2])

                    // Scale the value of sensors from 0-9.8 to 0-255
                    // i.e. 0 in gravity means 0 in RGB
                    // 9.8 in gravity means 255 in RGB

                    val red : Int = (x*(255/9.8)).toInt()
                    val green : Int = (y*(255/9.8)).toInt()
                    val blue : Int = (z*(255/9.8)).toInt()

                    val color = Color.rgb(red, green, blue)

                    rootLayout.setBackgroundColor(color)

                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        val accelerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

        sensorManager.registerListener(this, accelerometer, 100000)
        sensorManager.registerListener(this, gravity, 100000)//delay ui best for our use case
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(this)
    }
}

//sensors->software relies on third party sensor for its calculations
// is dynamic discovery supported for something like project ara or red hydrogen
//step counter + silent mobi
//
// le phone

//alert box custom alert could not find edit text on main  thus finds in inflated view
//design layout textinputlayout to create a better look
//floating action button