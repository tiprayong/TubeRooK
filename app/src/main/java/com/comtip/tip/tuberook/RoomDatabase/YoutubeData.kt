package com.comtip.tip.tuberook.RoomDatabase

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by TipRayong on 14/4/2561 13:26
 * TubeRooK
 */

@Entity
data class YoutubeData(
        @PrimaryKey(autoGenerate = true) var primaryKey: Int? = null,
        var type: String? = null,
        var name: String? = null,
        var id: String? = null
)
