package edu.uw.cs340.sensorsplayground

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

abstract class SensorApp(sensorType: Int): ComponentActivity(), SensorEventListener {
    private var LOG_TAG = "SENSOR_APP"
    private val sensorType = sensorType
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        logSensorList()
        sensor = sensorManager.getDefaultSensor(sensorType)
    }

    override fun onResume() {
        super.onResume()
        sensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        var sensorState = p0?.values?.joinToString(
            prefix = "[",
            postfix = "]"
        )
        Log.d(LOG_TAG, "onSensorChanged: $sensorState")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d(LOG_TAG, "onAccuracyChanged: $p1")
    }

    private fun logSensorList() {
        val sensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        val sensorsNames = sensors.joinToString()
        Log.d(LOG_TAG, "Available Sensors: $sensorsNames")
    }
}