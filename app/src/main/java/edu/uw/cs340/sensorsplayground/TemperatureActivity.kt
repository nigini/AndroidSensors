package edu.uw.cs340.sensorsplayground

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import edu.uw.cs340.sensorsplayground.ui.theme.SensorsPlaygroundTheme

class MainActivity : ComponentActivity(), SensorEventListener{
    private val LOG_TAG = "SENSOR_TEMP"
    private var sensorManager: SensorManager? = null
    private var tempSensor: Sensor? = null
    private var temperature = mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        tempSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        setContent {
            SensorsPlaygroundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TemperaturePresenter(temperatureCelcius = temperature.value)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        tempSensor?.also {sensor ->
            var success = sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d(LOG_TAG, "${sensor.name}: REGISTERED SENSOR: $success")
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        Log.d(LOG_TAG, "${event.sensor.name}: SENSOR READING CHANGED!")
        temperature.value = event.values[0].toInt()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        Log.d(LOG_TAG, "${sensor.name}: ACCURACY CHANGED!")
    }
}

@Composable
fun TemperaturePresenter(temperatureCelcius: Int, modifier: Modifier = Modifier) {
    Text(
        text = "$temperatureCelcius °C",
        modifier = modifier
    )
}