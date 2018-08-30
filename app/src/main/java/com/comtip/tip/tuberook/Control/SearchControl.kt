package com.comtip.tip.tuberook.Control

import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AlertDialog
import com.comtip.tip.tuberook.*
import com.comtip.tip.tuberook.Youtube.YoutubeSearchExecute

/**
 * Created by TipRayong on 17/4/2561 10:35
 * TubeRooK
 */
/*-------------------------------------------------
    Method: searchVideoOption
    Description: Select Option for Search Process.
-------------------------------------------------*/
fun searchOption(activity: Activity, keyword:String){
    val alertSearch = AlertDialog.Builder(activity)
    alertSearch.setTitle("Search  $keyword Order By ? ")
    alertSearch.setNegativeButton("New Release"){_,_->
        YoutubeSearchExecute(activity).search(presentMode,keyword, BY_DATE)
    }

    if(presentMode.equals(PLAYLIST,true)){
        alertSearch.setNeutralButton("Video Count"){_,_->
            YoutubeSearchExecute(activity).search(presentMode,keyword, BY_COUNT)
        }
    }else {
        alertSearch.setNeutralButton("Popular"){_,_->
            YoutubeSearchExecute(activity).search(presentMode,keyword, BY_VIEW)
        }
    }

    alertSearch.setPositiveButton("Normal"){_,_->
        YoutubeSearchExecute(activity).search(presentMode,keyword,"")
    }

    val alertView = alertSearch.create()
    alertView.show()

    val newImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(R.drawable.newrelease, null)
    } else {
        activity.resources.getDrawable(R.drawable.newrelease)
    }
    newImage.setBounds(0, 0, newImage.intrinsicWidth, newImage.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setCompoundDrawables(newImage, null, null, null)

    val normalImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(R.drawable.normal, null)
    } else {
        activity.resources.getDrawable(R.drawable.normal)
    }
    normalImage.setBounds(0, 0, normalImage.intrinsicWidth, normalImage.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_POSITIVE)
            .setCompoundDrawables(normalImage, null, null, null)

    if(presentMode.equals(PLAYLIST,true)){
        val playlistImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.resources.getDrawable(R.drawable.viewcount, null)
        } else {
            activity.resources.getDrawable(R.drawable.viewcount)
        }
        playlistImage.setBounds(0, 0, playlistImage.intrinsicWidth, playlistImage.intrinsicHeight)
        alertView.getButton(DialogInterface.BUTTON_NEUTRAL)
                .setCompoundDrawables(playlistImage, null, null, null)
    } else {
        val videoImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.resources.getDrawable(R.drawable.pop, null)
        } else {
            activity.resources.getDrawable(R.drawable.pop)
        }
        videoImage.setBounds(0, 0, videoImage.intrinsicWidth, videoImage.intrinsicHeight)
        alertView.getButton(DialogInterface.BUTTON_NEUTRAL)
                .setCompoundDrawables(videoImage, null, null, null)
    }
}

