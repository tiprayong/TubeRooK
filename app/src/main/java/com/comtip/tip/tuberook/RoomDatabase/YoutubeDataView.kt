package com.comtip.tip.tuberook.RoomDatabase

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.AsyncTask
import com.comtip.tip.tuberook.ALPHABET
import com.comtip.tip.tuberook.FIRST
import com.comtip.tip.tuberook.LAST


/**
 * Created by TipRayong on 14/4/2561 14:05
 * TubeRooK
 */
class YoutubeDataView(application: Application) : AndroidViewModel(application) {
    private val mDatabase = AppDatabase.getInDatabase(application)

    fun closeRoom() {
        mDatabase.close()
        AppDatabase.destroyInstance()
    }

    /*--------------------------------------------------------------------------
        Method: showFavoriteYoutube
        Description:  Select data form Table.
    ---------------------------------------------------------------------------*/
    fun showFavoriteYoutube(type: String, order: String): List<YoutubeData> {
        var data: List<YoutubeData> = ArrayList()
        try {
            val getYoutubeDataTask = GetYoutubeDataAsyncTask(mDatabase, type, order)
            data = getYoutubeDataTask.execute().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    private class GetYoutubeDataAsyncTask
    internal constructor(private val mDatabase: AppDatabase,
                         private val mType: String,
                         private val mOrder: String)
        : AsyncTask<Void, Void, List<YoutubeData>>() {

        override fun doInBackground(vararg voids: Void): List<YoutubeData> {
            var temp: List<YoutubeData> = ArrayList()
            when (mOrder) {
                ALPHABET -> temp = mDatabase.youtubeDataModel().orderByAlphabet(mType)
                FIRST -> temp = mDatabase.youtubeDataModel().orderByFirst(mType)
                LAST -> temp = mDatabase.youtubeDataModel().orderByLast(mType)
            }
            return temp
        }
    }
    /*--------------------------------------------------------------------------*/

    /*--------------------------------------------------
        Method:  addYoutubeData
        Description:  Insert data to Table.
    --------------------------------------------------*/
    fun addYoutubeData(youtubeData: YoutubeData) {
        val addTask = AddYoutubeDataAsyncTask(mDatabase, youtubeData)
        addTask.execute()
    }

    private class AddYoutubeDataAsyncTask
    internal constructor(private val mDatabase: AppDatabase,
                         private val youtubeData: YoutubeData)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg void: Void): Void? {
            mDatabase.youtubeDataModel().insertYoutubeData(youtubeData)
            return null
        }
    }
    /*--------------------------------------------------------------------------*/

    /*---------------------------------------------
        Method: deleteYoutubeData
        Description:  Delete data from Table.
     ---------------------------------------------*/
    fun deleteYoutubeData(primaryKey: Int) {
        val deleteTask = DeleteYoutubeDataAsyncTask(mDatabase, primaryKey)
        deleteTask.execute()
    }

    private class DeleteYoutubeDataAsyncTask
    internal constructor(private val mDatabase: AppDatabase,
                         private val primaryKey: Int)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg void: Void): Void? {
            mDatabase.youtubeDataModel().deleteYouyubeData(primaryKey)
            return null
        }
    }
    /*--------------------------------------------------------------------------*/
}
