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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import edu.uw.cs340.sensorsplayground.ui.theme.SensorsPlaygroundTheme

class AccelerometerDemo: SensorApp(Sensor.TYPE_ACCELEROMETER) {
    private var velocityX = mutableStateOf(0)
    private var velocityY = mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SensorsPlaygroundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BallGame(
                        modifier = Modifier,
                        ballColor = MaterialTheme.colorScheme.onBackground,
                        velocityX = velocityX.value,
                        velocityY = velocityY.value)
                }
            }
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        super.onSensorChanged(p0)
        var tempVel = p0?.values?.get(0)?.toInt()
        velocityX.value = tempVel ?: velocityX.value
        tempVel = p0?.values?.get(1)?.toInt()
        velocityY.value = tempVel ?: velocityY.value
    }
}

@Composable
fun BallGame(modifier: Modifier, ballColor: Color, velocityX: Int, velocityY: Int) {
    //Local Canvas state!
    var circlePos by remember { mutableStateOf(Offset.Zero) }
    Canvas(modifier = modifier.fillMaxSize()){
        var tempX = circlePos.x - velocityX
        var tempY = circlePos.y + velocityY

        //Moving algorithm
        if (tempX <= 0) {
            tempX = 0f
        } else if (tempX >= size.width) {
            tempX = size.width
        }

        if (tempY <= 0) {
            tempY = 0f
        } else if (tempY >= size.height) {
            tempY = size.height
        }
        circlePos = Offset(tempX, tempY)

        drawCircle(
            color = ballColor,
            center = circlePos,
            radius = size.minDimension/10
        )
    }
}