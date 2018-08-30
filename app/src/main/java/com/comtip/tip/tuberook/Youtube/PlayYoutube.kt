package com.comtip.tip.tuberook.Youtube

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.comtip.tip.tuberook.*
import com.comtip.tip.tuberook.Control.isInternetConnectionAvailable
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.gson.Gson
import kotlinx.android.synthetic.main.youtube_layout.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.*


/**
 * Created by TipRayong on 14/4/2561 13:23
 * TubeRooK
 */
class PlayYoutube : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {


    private var playlistID = ""
    private var playlist: ArrayList<String>? = ArrayList()
    private var selectShuffle = false
    private var isPlayListMode = false
    private var targetVideo = 0  // Target video  must  play first.
    private var mLastPlay = ""

    private val setting = settingPlaylistEventListener()
    private val change = settingPlayerStateChange()

    private var mYouTubePlayer: YouTubePlayer? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.youtube_layout)

        if (setSleep) {
            Handler().postDelayed(Runnable {
                setSleep = false
                finish()
            }, (sleepTime * 60 * 1000).toLong())
        }

    }


    override fun onStart() {
        super.onStart()

        val bundle = intent.extras

        isPlayListMode = bundle!!.getBoolean("isPlayListMode", false)
        selectShuffle = bundle.getBoolean("selectShuffle", false)

        if (isPlayListMode) {  // Playlist List
            playlistID = bundle.getString("playlistID", "")
            if (playlistID.isEmpty()) {
                finish()
            } else {
                if (selectShuffle) {
                    GetYoutubePlaylistAllPage().execute()
                } else {
                    youtubeView.initialize(YOUTUBE_API_KEY, this@PlayYoutube)
                }
            }

        } else {  // Video List
            targetVideo = bundle.getInt("targetVideo")
            playlist = bundle.getStringArrayList("playlist")
            if (playlist!!.size < 1) {
                finish()
            } else {
                youtubeView.initialize(YOUTUBE_API_KEY, this@PlayYoutube)
            }
        }

    }

    private fun customToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        val toastView = toast.view
        val toastMessage = toastView.findViewById(android.R.id.message) as TextView
        toastMessage.textSize = 40f
        toastMessage.setTextColor(Color.YELLOW)
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0)
        toastMessage.gravity = Gravity.BOTTOM
        toastMessage.compoundDrawablePadding = 16
        toastView.setBackgroundColor(Color.TRANSPARENT)
        toast.show()
    }


    override fun onBackPressed() {


        val alertBack = AlertDialog.Builder(this@PlayYoutube)
        alertBack.setTitle("Back to Main Menu ?")
        alertBack.setPositiveButton("✔ Yes ✔", DialogInterface.OnClickListener { dialog, which -> finish() })

        alertBack.setNeutralButton("✎ Memorize ✎", DialogInterface.OnClickListener { dialogInterface, i ->
            lastPlay = mLastPlay
            finish()
        })

        alertBack.setNegativeButton("✘ No ✘", DialogInterface.OnClickListener { dialog, which ->
            //no Action
        })
        val alertB = alertBack.create()
        alertB.show()

    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider,
                                         youTubePlayer: YouTubePlayer, b: Boolean) {
        youTubePlayer.setFullscreen(true)
        youTubePlayer.setPlaylistEventListener(setting)
        youTubePlayer.setPlayerStateChangeListener(change)

        mYouTubePlayer = youTubePlayer


        if (selectShuffle) {
            playlist!!.shuffle()   // shuffle PlayList
            mYouTubePlayer!!.loadVideos(playlist)
        } else {
            if (isPlayListMode) {
                mYouTubePlayer!!.loadPlaylist(playlistID)  // Direct Play
            } else {  // ของ Video Mode
                if (targetVideo == 0) {
                    mYouTubePlayer!!.loadVideos(playlist)   // Direct Play
                } else {
                    mYouTubePlayer!!.loadVideos(playlist, targetVideo, 0)  // play target video
                }
            }
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider,
                                         youTubeInitializationResult: YouTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError) {
            youTubeInitializationResult.getErrorDialog(this, 1).show()
        } else {
            Toast.makeText(this, "Unknown Error", Toast.LENGTH_LONG).show()
        }

        finish()
    }

    private inner class settingPlaylistEventListener : YouTubePlayer.PlaylistEventListener {

        override fun onPrevious() {

            if (isInternetConnectionAvailable(this@PlayYoutube)) {

            } else {
                finish()
            }
        }

        override fun onNext() {

            if (isInternetConnectionAvailable(this@PlayYoutube)) {


            } else {
                finish()
            }
        }

        override fun onPlaylistEnded() {

        }
    }

    private inner class settingPlayerStateChange : YouTubePlayer.PlayerStateChangeListener {

        override fun onLoading() {

        }

        override fun onLoaded(s: String) {
            mLastPlay = s

        }

        override fun onAdStarted() {

        }

        override fun onVideoStarted() {
            if (setSleep) {
                customToast("Lullaby Mode.")
            }
        }

        override fun onVideoEnded() {

        }

        override fun onError(errorReason: YouTubePlayer.ErrorReason) {

        }
    }


    // Get Video ID from PlayList
    @SuppressLint("StaticFieldLeak")
    private inner class GetYoutubePlaylistAllPage : AsyncTask<Void, String, Void>() {
        internal val googleapis = "https://www.googleapis.com/youtube/v3/playlistItems?"
        internal var pageToken = ""
        internal var pageTokenBuffer: String? = ""
        internal val snippet = "part=snippet&playlistId="
        internal val maxResults = "&maxResults=50&key="
        internal var queryYTPL = ""
        internal var playlistPage = ""
        internal lateinit var pd: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            pageTokenBuffer = "YOUTUBE"

            pd = ProgressDialog(this@PlayYoutube)
            pd.setTitle("Loading PlayList")
            pd.setMessage("Please Wait. . .")
            pd.setCancelable(false)
            pd.show()
        }

        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)
            pd.setMessage(values[0])
        }

        override fun doInBackground(vararg params: Void): Void? {

            while (pageTokenBuffer != null) {

                queryYTPL = (googleapis
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
                        val obj = gsonYoutube.fromJson(playlistPage, KsonGetPlaylistYoutube::class.java)

                        // Next Pge
                        pageTokenBuffer = obj.nextPageToken
                        if (pageTokenBuffer != null) {
                            pageToken = "pageToken=$pageTokenBuffer&"
                        }

                        // get data
                        for (i in 0 until obj.items.size) {
                            playlist!!.add(obj.items[i].snippet!!.resourceId!!.videoId!!)
                            publishProgress(obj.items[i].snippet!!.title!!)
                        }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            pd.dismiss()
            youtubeView.initialize(YOUTUBE_API_KEY, this@PlayYoutube)
        }
    }

}
