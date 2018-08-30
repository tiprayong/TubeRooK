package com.comtip.tip.tuberook.Youtube

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by TipRayong on 18/4/2561 11:49
 * TubeRooK
 */
class KsonSearchPlayListYoutube {

    @SerializedName("items")
    var items: List<Items> = ArrayList()

    data class Items(
            @SerializedName("id") val id: Id?,
            @SerializedName("snippet")val snippet: Snippet?
    )

    data class Id(
            @SerializedName("playlistId")val playlistId: String?
    )

    data class Snippet(
            @SerializedName("title")val title: String?
    )

}

