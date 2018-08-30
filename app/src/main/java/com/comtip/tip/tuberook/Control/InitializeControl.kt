package com.comtip.tip.tuberook.Control

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.speech.RecognizerIntent
import com.comtip.tip.tuberook.*
import com.comtip.tip.tuberook.Presenter.CustomSnackBar
import com.comtip.tip.tuberook.Presenter.setupListView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by TipRayong on 16/4/2561 12:18
 * TubeRooK
 */

fun initialize(activity: Activity) {
    presentMode = VIDEO
    activity.modeBT.setTextColor(Color.BLACK)
    activity.searchBT.setTextColor(Color.BLACK)
    activity.voiceBT.setTextColor(Color.BLACK)
    activity.keywordET.setTextColor(Color.BLACK)

    activity.setSleepBT.setOnClickListener {
      setSleepDialog(activity)
    }

    activity.modeBT.setOnClickListener {
        when (presentMode) {
            VIDEO -> {
                presentMode = PLAYLIST
                activity.modeBT.setTextColor(Color.BLUE)
                activity.searchBT.setTextColor(Color.BLUE)
                activity.voiceBT.setTextColor(Color.BLUE)
                activity.keywordET.setTextColor(Color.BLUE)
            }

            PLAYLIST ->{
                presentMode = LIVE
                activity.modeBT.setTextColor(Color.RED);
                activity.searchBT.setTextColor(Color.RED);
                activity.voiceBT.setTextColor(Color.RED);
                activity.keywordET.setTextColor(Color.RED);
            }

            LIVE ->{
                presentMode = VIDEO;
                activity.modeBT.setTextColor(Color.BLACK);
                activity.searchBT.setTextColor(Color.BLACK);
                activity.voiceBT.setTextColor(Color.BLACK);
                activity.keywordET.setTextColor(Color.BLACK);
            }

        }
        activity.modeBT.text = presentMode
    }

    activity.modeBT.setOnLongClickListener {
        alertBackup(activity)
        false
    }

    // Search  Execute.
    activity.searchBT.setOnClickListener {
        if(activity.keywordET.text.toString().isEmpty()){
            CustomSnackBar.setMessage(activity, "Empty Keyword !!!");
            //Toast.makeText(activity,"Empty Keyword !!!",Toast.LENGTH_SHORT).show()
        } else {
            searchOption(activity,activity.keywordET.text.toString())
        }
    }

    //Speech to Text
    activity.voiceBT.setOnClickListener {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"th-TH")

        try{
            activity.startActivityForResult(intent,77)
        } catch (a: ActivityNotFoundException){
            CustomSnackBar.setMessage(activity, "Hardware Problem !!!");
            //Toast.makeText(activity,"Hardware Error !!! "+a.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    // Favorite Video Tab
    activity.favVideoBT.setOnClickListener {
        sortOption(activity,VIDEO)
    }

    // Favorite Playlist Tab
    activity.favPlaylistBT.setOnClickListener {
        sortOption(activity, PLAYLIST)
    }

    val youtubeDataList = youtubeDataView.showFavoriteYoutube(VIDEO, LAST)
    if(youtubeDataList.isNotEmpty()){
          setupListView(activity,false,youtubeDataList,VIDEO)
    }
}

