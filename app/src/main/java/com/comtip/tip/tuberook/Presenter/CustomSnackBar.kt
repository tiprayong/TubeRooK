package com.comtip.tip.tuberook.Presenter

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.design.widget.Snackbar
import android.view.Gravity
import android.widget.TextView
import com.comtip.tip.tuberook.R

/**
 * Created by TipRayong on 17/4/2561 14:01
 * TubeRooK
 */
abstract class CustomSnackBar {
    companion object {
        private lateinit var  snackBar: Snackbar
        fun setMessage(activity: Activity,message:String){
            snackBar = Snackbar.make(activity.currentFocus,message,Snackbar.LENGTH_LONG)
                    .setAction("✘"){
                        snackBar.dismiss()
                    }.setActionTextColor(Color.RED)

            val view = snackBar.view
            view.setBackgroundColor(Color.WHITE)
            val textView = view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
            textView.setTextColor(Color.BLUE)
            textView.gravity = Gravity.CENTER_VERTICAL or Gravity.CLIP_HORIZONTAL

            val textAction = view.findViewById<TextView>(android.support.design.R.id.snackbar_action)

            val iconImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.resources.getDrawable(R.mipmap.ic_launcher,null)
            } else {
                activity.resources.getDrawable(R.mipmap.ic_launcher)
            }
            iconImage.setBounds(0, 0, iconImage.intrinsicWidth, iconImage.intrinsicHeight);
            textAction.setCompoundDrawables(null,null,iconImage,null)
            snackBar.show()
        }
    }
}

