package com.comtip.tip.tuberook.Control

import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AlertDialog
import com.comtip.tip.tuberook.ALPHABET
import com.comtip.tip.tuberook.FIRST
import com.comtip.tip.tuberook.LAST
import com.comtip.tip.tuberook.Presenter.emptyListView
import com.comtip.tip.tuberook.Presenter.setupListView
import com.comtip.tip.tuberook.youtubeDataView

/**
 * Created by TipRayong on 17/4/2561 10:35
 * TubeRooK
 */
   /*---------------------------------------------
        Method: sortOption
        Description: Sort Data
    ---------------------------------------------*/
fun sortOption(activity: Activity, type:String){
    val alertSort = AlertDialog.Builder(activity)
    alertSort.setTitle("$type Sort By ?");

    alertSort.setNeutralButton("ORDER"){_,_->
        val temp = youtubeDataView.showFavoriteYoutube(type, FIRST)
        if(temp.isEmpty()){
            emptyListView(activity, type)
        } else {
            setupListView(activity,false,temp,type)
        }
    }

    alertSort.setPositiveButton("RECENT"){_,_->
        val temp = youtubeDataView.showFavoriteYoutube(type, LAST)
        if(temp.isEmpty()){
            emptyListView(activity,type)
        } else {
            setupListView(activity,false,temp,type)
        }
    }

    alertSort.setNegativeButton("ALPHABET"){_,_->
        val temp = youtubeDataView.showFavoriteYoutube(type, ALPHABET)
        if(temp.isEmpty()){
            emptyListView(activity, type)
        } else {
            setupListView(activity,false,temp,type)
        }
    }

    val alertView = alertSort.create()
    alertView.show()

    val orderImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.ic_menu_sort_by_size, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.ic_menu_sort_by_size)
    }
    orderImage.setBounds(0, 0, orderImage.intrinsicWidth, orderImage.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_NEUTRAL)
            .setCompoundDrawables(orderImage, null, null, null)

    val recentImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.ic_menu_recent_history, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.ic_menu_recent_history)
    }
    recentImage.setBounds(0, 0, recentImage.intrinsicWidth, recentImage.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_POSITIVE)
            .setCompoundDrawables(recentImage, null, null, null)

    val alphabetImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.ic_menu_sort_alphabetically, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.ic_menu_sort_alphabetically)
    }
    alphabetImage.setBounds(0, 0, alphabetImage.intrinsicWidth, alphabetImage.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setCompoundDrawables(alphabetImage, null, null, null)
}
