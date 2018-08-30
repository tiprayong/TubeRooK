package com.comtip.tip.tuberook.Presenter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.comtip.tip.tuberook.*
import com.comtip.tip.tuberook.Control.intentPlayListYoutube
import com.comtip.tip.tuberook.Control.intentVideoYoutube
import com.comtip.tip.tuberook.RoomDatabase.YoutubeData
import com.comtip.tip.tuberook.Youtube.ExpandPlaylist
import com.comtip.tip.tuberook.Youtube.addNewYoutube
import com.comtip.tip.tviewk.Adapter.CustomListViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by TipRayong on 15/4/2561 8:57
 * TubeRooK
 */
/*----------------------------------------------------------
    Method: setupListView
    Description:  Setup ListView Show Data.
----------------------------------------------------------*/
fun setupListView(activity: Activity,
                  isSearchResult: Boolean,
                  youtubeDataList: List<YoutubeData>,
                  status: String) {

    showStatus(activity, status, youtubeDataList[0].type!!)

    val adapter = CustomListViewAdapter(activity, youtubeDataList)
    activity.resultLV.adapter = adapter

    activity.resultLV.setOnItemClickListener { _, _, i, l ->
        if (youtubeDataList[i].type.equals(PLAYLIST, true)) {
            playlistMode(activity, isSearchResult, youtubeDataList[i])
        } else if (youtubeDataList[i].type.equals(VIDEO, true)) {
            videoMode(activity, isSearchResult, i, youtubeDataList[i], youtubeDataList)
        }
    }

    //Delete
    activity.resultLV.setOnItemLongClickListener { _, _, i, l ->
        if (isSearchResult) {
            CustomSnackBar.setMessage(activity, "Search Result can't Delete !!!")
        } else {
            deleteAlert(activity, youtubeDataList[i])
        }
        false
    }
}
/*----------------------------------------------------------*/


/*----------------------------------------------
       Method: showStatus
       Description: Display Status and current tab.
   -----------------------------------------------*/
@SuppressLint("SetTextI18n")
fun showStatus(activity: Activity, status: String, type: String) {
    if (lastPlay.isEmpty()) {
        activity.statusTV.text = status // Display what type of data.
    } else {
        activity.statusTV.text = "✎ $status ✎"  // Remind  Memorize Video
    }

    if (type.equals(PLAYLIST, true)) {
        activity.favPlaylistBT.setBackgroundResource(R.drawable.select_shape)
        activity.favVideoBT.setBackgroundResource(R.drawable.button_shape)
    } else if (type.equals(VIDEO, true)) {
        activity.favPlaylistBT.setBackgroundResource(R.drawable.button_shape)
        activity.favVideoBT.setBackgroundResource(R.drawable.select_shape)
    }
}
/*------------------------------------------------*/

/*------------------------------------------------------
        Method: deleteAlert
        Description:  Delete Alert Dialog.
------------------------------------------------------*/
fun deleteAlert(activity: Activity, youtubeData: YoutubeData) {
    val alertDelete = AlertDialog.Builder(activity)
    alertDelete.setTitle("Delete " + youtubeData.type + "/" + youtubeData.name + " ?")

    alertDelete.setPositiveButton("✔ Yes ✔") { _, _ ->
        val deleteData = youtubeData.name!!
        val tempType: String = youtubeData.type!!
        youtubeDataView.deleteYoutubeData(youtubeData.primaryKey!!)
        Toast.makeText(activity, "Delete $deleteData From Database !!!", Toast.LENGTH_SHORT).show()

        val temp = youtubeDataView.showFavoriteYoutube(tempType, LAST)
        if (temp.isEmpty()) {
            emptyListView(activity, tempType)
        } else {
            setupListView(activity, false, temp, tempType)
        }
    }

    alertDelete.setNegativeButton("✘ No ✘") { _, _ ->
        //Cancel
    }

    val alertView = alertDelete.create()
    alertView.show()
}
/*------------------------------------------------------*/

/*-----------------------------------------------------
   Method: emptyListView
   Description: Protect ListView from any possible bug.
------------------------------------------------------*/
fun emptyListView(activity: Activity, type: String) {
    showStatus(activity, type, type)
    val dummy: List<YoutubeData> = ArrayList<YoutubeData>()
    val adapter = CustomListViewAdapter(activity, dummy)
    adapter.clear()
    activity.resultLV.adapter = adapter
    CustomSnackBar.setMessage(activity, "Data not found !!!");
}
/*------------------------------------------------------*/

/*-----------------------------------------------------------------------------------
        Method: playlistMode
        Description: Playlist Control.
-----------------------------------------------------------------------------------*/
fun playlistMode(activity: Activity, isSearchResult: Boolean, youtubeData: YoutubeData) {
    val alertPlaylist = AlertDialog.Builder(activity)
    alertPlaylist.setTitle("PlayList : " + youtubeData.name)
    // Expand Data in Playlist.
    alertPlaylist.setItems(arrayOf<CharSequence>("⇱ Expand Playlist ⇲")) { _, _ ->
        ExpandPlaylist(activity).expand(youtubeData.id!!, youtubeData.name!!)
    }

    if (isSearchResult) { // Search  Page   ->  Favorite Button
        alertPlaylist.setNegativeButton("Favorite") { _, _ ->
            //Favorite Playlist
            youtubeDataView.addYoutubeData(youtubeData)
            CustomSnackBar.setMessage(activity, "Favorite PlayList : " + youtubeData.name);
        }
    } else {// Favorite Page -> Add Button
        alertPlaylist.setNegativeButton("NEW") { _, _ ->
            //add new Playlist (Manual)
            addNewYoutube(activity, true)
        }
    }

    alertPlaylist.setPositiveButton("Shuffle") { _, _ ->
        intentPlayListYoutube(activity, youtubeData.id!!, true)
    }

    alertPlaylist.setNeutralButton("Direct") { _, _ ->
        intentPlayListYoutube(activity, youtubeData.id!!, false)
    }

    val alertView = alertPlaylist.create()
    alertView.show()
    setIconButton(activity, alertView, isSearchResult)
}
/*------------------------------------------------------*/

/*----------------------------------------------------------------------------------------------
        Method: videoMode
        Description: Video Control.
 ---------------------------------------------------------------------------------------------*/
fun videoMode(activity: Activity,
              isSearchResult: Boolean,
              item: Int,
              youtubeData: YoutubeData,
              youtubeDataList: List<YoutubeData>) {

    val alertVideo = AlertDialog.Builder(activity)
    alertVideo.setTitle(youtubeData.name)

    if (isSearchResult) { //Search  Page  ->  Favorite Button
        alertVideo.setNegativeButton("Favorite") { _, _ ->
            //  favorite  video
            youtubeDataView.addYoutubeData(youtubeData)
            CustomSnackBar.setMessage(activity, "Favorite Video : " + youtubeData.name);
        }
    } else { // Favorite Page -> Add Button
        alertVideo.setNegativeButton("NEW") { _, _ ->
            //add new Video (Manual)
            addNewYoutube(activity, false)
        }
    }

    alertVideo.setPositiveButton("Shuffle") { _, _ ->
        intentVideoYoutube(activity, item, true, youtubeDataList)
    }

    alertVideo.setNeutralButton("Play This Video") { _, _ ->
        intentVideoYoutube(activity, item, false, youtubeDataList)
    }
    val alertView = alertVideo.create()
    alertView.show()

    setIconButton(activity, alertView, isSearchResult)
}
/*------------------------------------------------------*/

//Set Icon Button
fun setIconButton(activity: Activity, alertView: AlertDialog, isSearchResult: Boolean) {

    val directImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(R.drawable.play, null)
    } else {
        activity.resources.getDrawable(R.drawable.play)
    }
    directImage.setBounds(0, 0, directImage.intrinsicWidth, directImage.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_NEUTRAL)
            .setCompoundDrawables(directImage, null, null, null)


    val shuffleImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(R.drawable.shuffle, null)
    } else {
        activity.resources.getDrawable(R.drawable.shuffle)
    }
    shuffleImage.setBounds(0, 0, shuffleImage.intrinsicWidth, shuffleImage.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_POSITIVE)
            .setCompoundDrawables(shuffleImage, null, null, null)

    if (isSearchResult) {
        val favoriteImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.resources.getDrawable(android.R.drawable.star_off, null)
        } else {
            activity.resources.getDrawable(android.R.drawable.star_off)
        }
        favoriteImage.setBounds(0, 0, favoriteImage.intrinsicWidth, favoriteImage.intrinsicHeight)
        alertView.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setCompoundDrawables(favoriteImage, null, null, null)

    } else {
        val addImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.resources.getDrawable(R.drawable.logo, null)
        } else {
            activity.resources.getDrawable(R.drawable.logo)
        }
        addImage.setBounds(0, 0, addImage.intrinsicWidth, addImage.intrinsicHeight)
        alertView.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setCompoundDrawables(addImage, null, null, null)
    }
}

