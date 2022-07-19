package com.hl.sun.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.hl.sun.R
import com.hl.sun.bean.HomeAnimValue
import com.hl.sun.bean.HomeFaAnimValue
import kotlinx.android.synthetic.main.activity_anim.*
import kotlinx.android.synthetic.main.rwhomemodule_progress_anim.*
import kotlin.math.roundToInt

class AnimActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim)
    }

    override fun onStop() {
        super.onStop()
        loop_avatar.stopLoop()
    }

    private val valueBean by lazy {
        HomeAnimValue(iv_icon, iv_icon_2, pb, pb_2)
    }

    fun startLoop(view: View) {
        loop_avatar.startLoop()
    }

    fun endLoop(view: View) {
        loop_avatar.stopLoop()
    }

    fun startAnim3(view: View) {
        startFaScrollAnim()
    }

    fun startAnim2(view: View) {
        val inAnim = AnimationUtils.loadAnimation(this, R.anim.rwhomemodule_fafa_say_in)
        tv_fafa_say?.startAnimation(inAnim)

        tv_fafa_say?.postDelayed({
            val outAnim = AnimationUtils.loadAnimation(this, R.anim.rwhomemodule_fafa_say_out)
            tv_fafa_say?.startAnimation(outAnim)
        }, 1500L)
    }

    fun startAnim1(view: View) {
        valueBean.apply {
            progressFrom = 40
            progressTo = 70
        }
        pb.progress = valueBean.progressFrom
        startScaleAnim(true)
    }

    /**
     * 放大缩小动效
     */
    private fun startScaleAnim(enlarge: Boolean) {
        val from = if (enlarge) 0 else 100
        val to = if (enlarge) 100 else 0
        ValueAnimator.ofInt(from, to).apply {
            duration = if (enlarge) valueBean.scaleEnargeDuration else valueBean.scaleShrinkDuration
            addUpdateListener { updatedAnimation ->
                val percentValue = ((updatedAnimation.animatedValue) as? Int ?: 0) / 100F
                valueBean.apply {  //icon
                    val iconSize = (iconWidth + iconWidthDiff * percentValue).roundToInt()
                    iconParams.width = iconSize
                    iconParams.height = iconSize
                    iconParams.leftMargin = (iconLeft + iconLeftDiffer * percentValue).toInt()
                    iconParams.topMargin = (iconTop + iconTopDiffer * percentValue).toInt()
                    iv_icon.layoutParams = iconParams
                    //pb
                    pbMParams.width = (pbWidth + pbWidthDiffer * percentValue).toInt()
                    pbMParams.height = (pbHeight + pbHeightDiffer * percentValue).toInt()
                    pbMParams.leftMargin = (pbLeft + pbLeftDiffer * percentValue).toInt()
                    pbMParams.topMargin = (pbTop + pbTopDiffer * percentValue).toInt()
                    pb.layoutParams = pbMParams
                }
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    if (enlarge) {
                        startProgressAnim(valueBean.progressFrom, valueBean.progressTo)
                    }
                }
            })
            start()
        }
    }

    /**
     *放大后的进度条动效
     */
    private fun startProgressAnim(from: Int, to: Int) {
        ValueAnimator.ofInt(from, to).apply {
            duration = valueBean.progressAnimDuration
            addUpdateListener { updatedAnimation ->
                val value = (updatedAnimation.animatedValue) as? Int ?: 0
                pb.progress = value
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    pb.postDelayed({ startScaleAnim(false) }, 1300)
                }
            })

            start()
        }
    }


    private val faValueBean by lazy {
        HomeFaAnimValue(iv_fa)
    }

    private fun startFaScrollAnim() {
        iv_fa.isVisible = true
        iv_fa_2.isVisible = true

        startScroll()
    }

    /**
     * fa滚入/滚出动效
     */
    private fun startScroll() {
        iv_fa.translationX = 0F
        iv_fa.animate().translationX(-faValueBean.scrollOutLength).setDuration(1000)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    iv_fa.clearAnimation()
                    iv_fa.isVisible = false
                }
            }).start()

        val inAnim =
            ObjectAnimator.ofFloat(iv_fa_2, "translationX", faValueBean.scrollOutLength, 0F)
        inAnim.duration = 1000
        inAnim.start()
    }
}