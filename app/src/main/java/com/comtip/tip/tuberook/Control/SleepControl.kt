package com.comtip.tip.tuberook.Control

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Handler
import android.widget.Button
import android.widget.NumberPicker
import com.comtip.tip.tuberook.R
import com.comtip.tip.tuberook.setSleep
import com.comtip.tip.tuberook.sleepTime
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by TipRayong on 17/4/2561 10:16
 * TubeRooK
 */
/*---------------------------------------------------------------------------------------------
    Method: setSleepDialog
    Description:  set up time to sleep (turn off application)
---------------------------------------------------------------------------------------------*/

fun setSleepDialog(activity: Activity) {
    val timerDialog = Dialog(activity)
    timerDialog.setTitle("Sleep Setup");
    timerDialog.setContentView(R.layout.set_sleep_layout);
    timerDialog.setCanceledOnTouchOutside(false)

    val sleepNP = timerDialog.findViewById<NumberPicker>(R.id.sleepNP)
    sleepNP.maxValue = 60
    sleepNP.minValue = 1
    sleepNP.value = sleepTime

    val confirmButton = timerDialog.findViewById<Button>(R.id.confirmBT)
    confirmButton.setOnClickListener {
        sleepTime = sleepNP.value

        Handler().postDelayed( {
            activity.finish();
        }, (sleepTime * 60 * 1000).toLong())

        activity.setSleepBT.setTextColor(Color.MAGENTA)
        activity.setSleepBT.isEnabled = false
        setSleep = true
        timerDialog.dismiss()
    }

    val cancelButton = timerDialog.findViewById<Button>(R.id.cancelBT)
    cancelButton.setOnClickListener {
        timerDialog.dismiss()
    }
    timerDialog.show()
}
