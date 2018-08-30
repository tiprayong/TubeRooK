package com.comtip.tip.tuberook.RoomDatabase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by TipRayong on 14/4/2561 13:49
 * TubeRooK
 */
@Database(entities = arrayOf(YoutubeData::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun youtubeDataModel(): YoutubeDataDAO

    companion object {
        const val DATABASE = "youtubeDBK"
        private var INSTANCE: AppDatabase? = null

        fun destroyInstance() {
            INSTANCE = null
        }

        fun getInDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE)
                        .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}