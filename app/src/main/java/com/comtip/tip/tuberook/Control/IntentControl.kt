package com.comtip.tip.tuberook.Control

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.comtip.tip.tuberook.Presenter.CustomSnackBar
import com.comtip.tip.tuberook.RoomDatabase.YoutubeData
import com.comtip.tip.tuberook.Youtube.PlayYoutube

/**
 * Created by TipRayong on 16/4/2561 11:45
 * TubeRooK
 */

//Check Internet Available
fun isInternetConnectionAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}

  /*-----------------------------------------------------------------------------------------
      Method: convertObjectToArrayList
      Description: Convert Object to ArrayList<String>.
  ------------------------------------------------------------------------------------------*/
fun convertObjectToArrayList(youtubeDataList: List<YoutubeData>): ArrayList<String> {
    val playList: ArrayList<String> = ArrayList<String>()
    for (i in 0 until youtubeDataList.size) {
        playList.add(i, youtubeDataList[i].id!!)
    }
    return playList
}

/*---------------------------------------------------------------------------------------------
    Method: intentVideoYoutube
    Description:  Intent Video Youtube.
---------------------------------------------------------------------------------------------*/
fun intentVideoYoutube(activity: Activity, targetVideo: Int, selectShuffle: Boolean, youtubeDataList: List<YoutubeData>) {
    if (isInternetConnectionAvailable(activity)) {
        val intent = Intent(activity, PlayYoutube::class.java)
        intent.putExtra("isPlayListMode", false);
        intent.putExtra("targetVideo", targetVideo);
        intent.putExtra("selectShuffle", selectShuffle);
        intent.putStringArrayListExtra("playlist", convertObjectToArrayList(youtubeDataList));
        activity.startActivity(intent);
    } else {
        CustomSnackBar.setMessage(activity, "Network Error !!!!");
    }
}

/*--------------------------------------------------------------------------------------------
        Method: intentPlayListYoutube
        Description:  Intent Playlist Youtube
--------------------------------------------------------------------------------------------*/
fun intentPlayListYoutube(activity: Activity, playlistID: String, selectShuffle: Boolean) {
    if (isInternetConnectionAvailable(activity)) {
        val intent = Intent(activity, PlayYoutube::class.java)
        intent.putExtra("isPlayListMode", true);
        intent.putExtra("selectShuffle", selectShuffle);
        intent.putExtra("playlistID", playlistID);
        activity.startActivity(intent);
    } else {
        CustomSnackBar.setMessage(activity, "Network Error !!!!");
    }
}
