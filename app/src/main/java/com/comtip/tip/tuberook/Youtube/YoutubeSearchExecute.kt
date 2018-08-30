package com.comtip.tip.tuberook.Youtube

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import com.comtip.tip.tuberook.*
import com.comtip.tip.tuberook.Presenter.CustomSnackBar
import com.comtip.tip.tuberook.Presenter.setupListView
import com.comtip.tip.tuberook.RoomDatabase.YoutubeData
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * Created by TipRayong on 14/4/2561 15:49
 * TubeRooK
 */
class YoutubeSearchExecute(private val activity: Activity) {
    private var typeSearch = ""
    private var tempStatus = ""
    /*-----------------------------------------------------------
   Method: search(String type, String keyword, String order)
   Description: Search data from Youtube.
   -----------------------------------------------------------*/
    fun search(type:String, keyword:String, order:String) {
        val googleapisSearch = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="
        var isPlayListMode = true
        when (type) {
            VIDEO -> {
                typeSearch = "&type=video&maxResults=50&key="
                isPlayListMode = false
            }
            PLAYLIST -> {
                typeSearch = "&type=playlist&maxResults=50&key="
                isPlayListMode = true
            }
            LIVE -> {
                typeSearch = "&eventType=live&type=video&maxResults=50&key="
                isPlayListMode = false
            }
        }
        tempStatus = "Search '$keyword' $type."
        val query = googleapisSearch + keyword + order + typeSearch + YOUTUBE_API_KEY
        try
        {
            val searchYoutubeAsyncTask = SearchYoutubeAsyncTask(isPlayListMode, query)
            searchYoutubeAsyncTask.execute()
        }
        catch (e:Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class SearchYoutubeAsyncTask(isPlayListMode:Boolean, query:String?): AsyncTask<Void, Void, Void>() {

        private var isPlayListMode = true
        private var query = ""
        private var temp:ArrayList<YoutubeData> = ArrayList()
        internal lateinit var pd:ProgressDialog
        init{
            this.isPlayListMode = isPlayListMode
            this.query = query!!
        }
        override fun onPreExecute() {
            super.onPreExecute()
            pd = ProgressDialog(activity, R.style.PdSpinnerTheme)
            pd.setCancelable(false)
            pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small)
            pd.show()
        }
        override fun doInBackground(vararg voids:Void?): Void? {
            val okHttpClient = OkHttpClient()
            val builder = Request.Builder()
            val request = builder.url(query).build()
            try
            {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful)
                {
                    val searchPage = response.body().string()
                    val gsonSearch = Gson()
                    if (isPlayListMode)
                    { // Search Playlist

                        val playlistObject = gsonSearch.fromJson(searchPage, KsonSearchPlayListYoutube::class.java)
                        for (i in 0 until playlistObject.items.size)
                        {
                            val youtubeData:YoutubeData = YoutubeData()
                            youtubeData.type = PLAYLIST
                            youtubeData.name = playlistObject.items[i].snippet!!.title
                            youtubeData.id = playlistObject.items[i].id!!.playlistId

                            temp.add(youtubeData)
                        }
                    }
                    else
                    { // Search Video
                        val videoObject = gsonSearch.fromJson(searchPage, KsonSearchVideoYoutube::class.java)
                        for (i in 0 until videoObject.items.size)
                        {
                            val youtubeData = YoutubeData()
                            youtubeData.type = VIDEO
                            youtubeData.name = videoObject.items[i].snippet!!.title
                            youtubeData.id = videoObject.items[i].id!!.videoId
                            temp.add(youtubeData)
                        }

                    }
                }
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
        override fun onPostExecute(aVoid:Void?) {
            super.onPostExecute(aVoid)
            pd.dismiss()

            if (temp.isEmpty())
            {
                CustomSnackBar.setMessage(activity, "Not Found !!!")
            }
            else
            {
                setupListView(activity,true,temp,tempStatus)
            }

        }
    }

}