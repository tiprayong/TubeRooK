package com.comtip.tip.tuberook.RoomDatabase
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query
/**
 * Created by TipRayong on 14/4/2561 13:37
 * TubeRooK
 */
@Dao
interface YoutubeDataDAO {
    @Query("SELECT * FROM YoutubeData WHERE type = :type ORDER BY primaryKey DESC")
    fun orderByLast(type:String):List<YoutubeData>

    @Query("SELECT * FROM YoutubeData WHERE type = :type ORDER BY primaryKey ASC")
    fun orderByFirst(type:String):List<YoutubeData>

    @Query("SELECT * FROM YoutubeData WHERE type = :type ORDER BY name ASC")
    fun orderByAlphabet(type:String):List<YoutubeData>

    @Insert(onConflict = IGNORE)
    fun insertYoutubeData(youtubeData: YoutubeData)

    @Query("DELETE FROM YoutubeData WHERE primaryKey = :primaryKey")
    fun deleteYouyubeData(primaryKey:Int)
}
