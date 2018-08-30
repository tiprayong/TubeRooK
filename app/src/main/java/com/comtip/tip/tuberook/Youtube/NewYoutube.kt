package com.comtip.tip.tuberook.Youtube

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.comtip.tip.tuberook.*
import com.comtip.tip.tuberook.Presenter.CustomSnackBar
import com.comtip.tip.tuberook.Presenter.emptyListView
import com.comtip.tip.tuberook.Presenter.setupListView
import com.comtip.tip.tuberook.RoomDatabase.YoutubeData


/**
 * Created by TipRayong on 17/4/2561 12:31
 * TubeRooK
 */
/*---------------------------------------------
     Method: addNew(final boolean isPlayList)
     Description: Dialog ADD new youtubeData.
 ---------------------------------------------*/
@SuppressLint("SetTextI18n")
fun addNewYoutube(activity: Activity, isPlaylist: Boolean) {
    val mDialog = Dialog(activity)
    mDialog.setContentView(R.layout.temp_layout)
    if (isPlaylist) {
        mDialog.setTitle("Add PlayList");
    } else {
        mDialog.setTitle("Add Video");
    }
    mDialog.setCancelable(false)

    val mLayout = mDialog.findViewById<LinearLayout>(R.id.tempLayout)

    val nameET = EditText(activity)
    nameET.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    mLayout.addView(nameET)

    val idET = EditText(activity)
    idET.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    mLayout.addView(idET)

    val row = LinearLayout(activity)
    row.orientation = LinearLayout.HORIZONTAL

    val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    )
    params.weight = 1F

    val cancelDialogBT = Button(activity)
    cancelDialogBT.layoutParams = params
    cancelDialogBT.text = "Cancel"
    cancelDialogBT.setBackgroundResource(R.drawable.button_shape)

    val cancelImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.ic_menu_close_clear_cancel, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.ic_menu_close_clear_cancel)
    }

    cancelImage.setBounds(0,0,cancelImage.intrinsicWidth,cancelImage.intrinsicHeight)
    cancelDialogBT.setCompoundDrawables(cancelImage,null,null,null)
    cancelDialogBT.setOnClickListener {
        mDialog.dismiss()
    }

    val addDialogBT = Button(activity)
    addDialogBT.layoutParams = params
    addDialogBT.text = "Add"
    addDialogBT.setBackgroundResource(R.drawable.button_shape)

    val addImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.ic_menu_add, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.ic_menu_add)
    }

    addImage.setBounds(0,0,addImage.intrinsicWidth,addImage.intrinsicHeight)
    addDialogBT.setCompoundDrawables(addImage,null,null,null)
    addDialogBT.setOnClickListener {
       if(nameET.text.toString().isNotEmpty() && idET.text.toString().isNotEmpty()){
           val youtubeData = YoutubeData()
           if(isPlaylist){
               youtubeData.type = PLAYLIST
           } else {
               youtubeData.type = VIDEO
           }
           youtubeData.name = nameET.text.toString()
           youtubeData.id = idET.text.toString()
           youtubeDataView.addYoutubeData(youtubeData)

           if(isPlaylist){
               CustomSnackBar.setMessage(activity, "Add PlayList : " + youtubeData.name);

               val temp = youtubeDataView.showFavoriteYoutube(PLAYLIST, LAST)

               if(temp.isEmpty()){
                   emptyListView(activity, PLAYLIST)
               } else {
                   setupListView(activity,false,temp, PLAYLIST)
               }
           } else {
               CustomSnackBar.setMessage(activity, "Add Video : " + youtubeData.name);

               val temp = youtubeDataView.showFavoriteYoutube(VIDEO, LAST)

               if(temp.isEmpty()){
                   emptyListView(activity, VIDEO)
               } else {
                   setupListView(activity,false,temp, VIDEO)
               }
           }

           mDialog.dismiss()
       }  else {
           CustomSnackBar.setMessage(activity, "Empty Data");
       }
    }
    row.addView(cancelDialogBT)
    row.addView(addDialogBT)
    mLayout.addView(row)
    mDialog.show()
}

