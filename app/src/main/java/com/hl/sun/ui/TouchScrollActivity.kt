package com.hl.sun.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import androidx.appcompat.app.AppCompatActivity
import com.hl.sun.R
import kotlinx.android.synthetic.main.activity_touch_scroll.*
import kotlin.math.roundToInt

class TouchScrollActivity : AppCompatActivity(), SensorEventListener {
    private var lastX = 0F
    private var lastY = 0F

    companion object {
        //手势滑动阻尼
        const val SLIDE_RATIO = 0.3

        //后景缩放比
        const val SCALE_BEHIND_SIZE = 1.13f

        //前景缩放比
        const val SCALE_FRONT_SIZE = 1.11f

        //X,Y轴最大偏转角度
        const val DEGREE_MAX = 45f
        //前景Y轴最大偏转角度
//        const val FRONT_Y_DEGREE_MAX = 25f
    }

    private lateinit var scroller: Scroller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch_scroll)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        front.scaleX = SCALE_FRONT_SIZE
        front.scaleY = SCALE_FRONT_SIZE
        behind.scaleX = SCALE_BEHIND_SIZE
        behind.scaleY = SCALE_BEHIND_SIZE

        scroller = Scroller(this, LinearInterpolator())
        container.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.x
                        lastY = event.y
                    }
                    MotionEvent.ACTION_MOVE
                    -> {
                        val xOffset = ((event.x - lastX) / SLIDE_RATIO).toInt()
                        val yOffset = ((event.y - lastY) / SLIDE_RATIO).toInt()
                        println("偏移量xOffset：$xOffset")
                        println("偏移量yOffset：$yOffset")

                        setOffset(xOffset, yOffset)

                        lastX = event.x
                        lastY = event.y
                    }
                }
                return true
            }
        })
    }

    private fun setOffset(xOffset: Int, yOffset: Int) {
        if (slideInContent(xOffset, yOffset)) {
            behind.offsetLeftAndRight(xOffset)
            behind.offsetTopAndBottom(yOffset)

            front.offsetLeftAndRight(-xOffset)
            front.offsetTopAndBottom(-yOffset)
        }
    }

    /*
        private fun scrollToPos(frontX: Int, frontY: Int, behindX: Int, behindY: Int) {
            behind.scrollTo(behindX, -behindY)
            front.scrollTo(-frontX, frontY)

            println("scrollToPos frontX:$frontX - frontY:$frontY - behindX:$behindX - behindY:$behindY")
        }
    * */
    private fun scrollToPos(frontX: Int, frontY: Int, behindX: Int, behindY: Int) {
        behind.scrollTo(behindX, -behindY)
        front.scrollTo(-frontX, frontY)

        println("scrollToPos frontX:$frontX - frontY:$frontY - behindX:$behindX - behindY:$behindY")
    }

    private fun smoothScrollTo(view: View, fx: Int, fy: Int) {
        val dx: Int = fx - scroller.getFinalX()
        val dy: Int = fy - scroller.getFinalY()
        smoothScrollBy(view, dx, dy)
    }

    //调用此方法设置滚动的相对偏移
    fun smoothScrollBy(view: View, dx: Int, dy: Int) {

        //设置mScroller的滚动偏移量
        scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), dx, dy)
        view.invalidate() //这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果

    }

    private var maxFrontOffsetX = -1f
    private var maxFrontOffsetY = -1f
    private var maxBehindOffsetX = -1f
    private var maxBehindOffsetY = -1f
    private fun slideInContent(xOffset: Int, yOffset: Int): Boolean {
        getMaxXYOffset()
        return ((behind.left + xOffset).toFloat()) in -maxBehindOffsetX..maxBehindOffsetX
                && ((behind.top + yOffset).toFloat()) in -maxBehindOffsetY..maxBehindOffsetY
                && ((front.left - xOffset).toFloat()) in -maxFrontOffsetX..maxFrontOffsetX
                && ((front.top - yOffset).toFloat()) in -maxFrontOffsetY..maxFrontOffsetY

    }

    private fun getMaxXYOffset() {
        if (maxFrontOffsetX < 0) {
            maxFrontOffsetX = front.width * (SCALE_FRONT_SIZE - 1) / 2
        }
        if (maxFrontOffsetY < 0) {
            maxFrontOffsetY = front.height * (SCALE_FRONT_SIZE - 1) / 4
        }
        if (maxBehindOffsetX < 0) {
            maxBehindOffsetX = behind.width * (SCALE_BEHIND_SIZE - 1) / 2
        }
        if (maxBehindOffsetY < 0) {
            maxBehindOffsetY = behind.height * (SCALE_BEHIND_SIZE - 1) / 4
        }
    }


    private lateinit var sensorManager: SensorManager
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)


    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    override fun onResume() {
        super.onResume()

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this)
    }

    // Get readings from accelerometer and magnetometer. To simplify calculations,
    // consider storing these readings as unit vectors.
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        updateOrientationAngles()
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    private fun updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        // "mRotationMatrix" now has up-to-date information.


        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        // "mOrientationAngles" now has up-to-date information.


        // x轴的偏转角度
        orientationAngles[1] = Math.toDegrees(orientationAngles[1].toDouble()).toFloat()
        // y轴的偏转角度
        orientationAngles[2] = Math.toDegrees(orientationAngles[2].toDouble()).toFloat()


        println("偏转角度orientationAngles[1]：${orientationAngles[1]}")
        println("偏转角度orientationAngles[2]：${orientationAngles[2]}")

        //if (angleInContent(orientationAngles[2], orientationAngles[1])) {
        getMaxXYOffset()
        /* scrollToPos(
             getOffsetByAngle(orientationAngles[2], maxFrontOffsetX),//54
             getOffsetByAngle(orientationAngles[1], maxFrontOffsetY),//109
             getOffsetByAngle(orientationAngles[2], maxBehindOffsetX),//135
             getOffsetByAngle(orientationAngles[1], maxBehindOffsetY)//273
         ) */

        front.smoothScrollTo(
            -getOffsetByAngle(orientationAngles[2], maxFrontOffsetX),//54
            getOffsetByAngle(orientationAngles[1], maxFrontOffsetY)//109
        )
        behind.smoothScrollTo(
            getOffsetByAngle(orientationAngles[2], maxBehindOffsetX),//135
            -getOffsetByAngle(orientationAngles[1], maxBehindOffsetY)//273
        )
//        }
    }

    private fun angleInContent(xAngle: Float, yAngle: Float): Boolean {
        return xAngle in -DEGREE_MAX..DEGREE_MAX
                && yAngle in -DEGREE_MAX..DEGREE_MAX
    }

    private fun getOffsetByAngle(angle: Float, maxOffset: Float): Int {
       return getOffsetByAngle(angle, maxOffset, DEGREE_MAX)
    }

    private fun getOffsetByAngle(angle: Float, maxOffset: Float, degreeMax: Float): Int {
        var offset = angle / degreeMax * maxOffset * SLIDE_RATIO
        return if (offset > 0 && offset > maxOffset) {
            maxOffset.roundToInt()
        } else if (offset < 0 && offset < -maxOffset) {
            -maxOffset.roundToInt()
        } else {
            offset.roundToInt()
        }
    }
}