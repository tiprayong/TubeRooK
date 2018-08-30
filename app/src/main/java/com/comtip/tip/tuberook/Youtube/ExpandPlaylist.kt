package com.comtip.tip.tuberook.Youtube

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import com.comtip.tip.tuberook.Presenter.CustomSnackBar
import com.comtip.tip.tuberook.Presenter.setupListView
import com.comtip.tip.tuberook.RoomDatabase.YoutubeData
import com.comtip.tip.tuberook.VIDEO
import com.comtip.tip.tuberook.YOUTUBE_API_KEY
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * Created by TipRayong on 15/4/2561 12:01
 * TubeRooK
 */
class ExpandPlaylist(private val activity: Activity) {
    private var tempStatus = ""

    fun expand(playlistID:String,playlistName:String){
        tempStatus = "Expand '$playlistName' Playlist."
        val expandTask = GetYoutubePlaylistAllPage(playlistID)
        expandTask.execute()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetYoutubePlaylistAllPage( playlistID: String)
        : AsyncTask<Void, String, Void>() {

        internal val googleapis = "https://www.googleapis.com/youtube/v3/playlistItems?"
        internal val snippet = "part=snippet&playlistId="
        internal val maxResults = "&maxResults=50&key="
        internal var pageToken = ""
        internal var pageTokenBuffer:String? = null
        internal var playlistPage = ""
        internal var temp:ArrayList<YoutubeData> = ArrayList()
        private var playlistID = ""
        internal lateinit var pd: ProgressDialog
        init{
            this.playlistID = playlistID
        }
        override fun onPreExecute() {
            super.onPreExecute()
            pageTokenBuffer = "YOUTUBE"; // Just Dummy Data for First Page Process.
            pd = ProgressDialog(activity)
            pd.setTitle("Expand PlayList");
            pd.setMessage("Please Wait . . .");
            pd.setCancelable(false)
            pd.show()
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            pd.setMessage(values[0])
        }

        override fun doInBackground(vararg voids:Void?): Void? {
            while (pageTokenBuffer != null) {

                val queryYTPL = (googleapis
                        + pageToken
                        + snippet
                        + playlistID
                        + maxResults
                        + YOUTUBE_API_KEY)

                val okHttpClient = OkHttpClient()
                val builder = Request.Builder()
                val request = builder.url(queryYTPL).build()

                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        playlistPage = response.body().string()

                        val gsonYoutube = Gson()
                        val obj = gsonYoutube.fromJson(
                                playlistPage, KsonGetPlaylistYoutube::class.java)

                        // Next Page
                        pageTokenBuffer = obj.nextPageToken
                        if (pageTokenBuffer != null) {
                            pageToken = "pageToken=$pageTokenBuffer&"
                        }

                        // Get Data
                        for (i in 0 until obj.items.size) {

                            val youtubeData = YoutubeData()
                            youtubeData.type = VIDEO
                            youtubeData.name = obj.items[i].snippet!!.title
                            youtubeData.id = obj.items[i].snippet!!.resourceId!!.videoId
                            temp.add(youtubeData)

                            // Show Loading Progress
                            publishProgress(obj.items[i].snippet!!.title)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }
        override fun onPostExecute(aVoid:Void?) {
            super.onPostExecute(aVoid)
            pd.dismiss()
            if (temp.size < 1)
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